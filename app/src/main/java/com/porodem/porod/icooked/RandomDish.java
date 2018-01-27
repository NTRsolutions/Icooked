package com.porodem.porod.icooked;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class RandomDish extends AppCompatActivity implements SoundPool.OnLoadCompleteListener {

    int soundIdAdd;
    int soundFry;
    int soundBoil;
    int soundCut;

    String timeText = String.valueOf(R.string.cooking_time);
    String min = String.valueOf(R.string.minutes);

    public static final String LOG_TAG = "myLogs";

    //constant for SharedPreferenses for saving random dish to Favorite
    final String SAVED_TEXT = "saved_text";

    TextView tvRandomTitle;
    TextView tvRandomTime;
    ImageView ivRandomImg;
    TextView tvRandomRecipe;

    //LinearLayout linearLayout;
    ScrollView scroll;

    SoundPool sp;

    DB db;

    SharedPreferences sPref;
    SharedPreferences sPrefIndx;

    String thisTitle;
    //for SharedPreferences index
    int p = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_dish);

        //initalize scrollView for focusing on start after "next" button
        scroll = (ScrollView)findViewById(R.id.scrollView2);
        scroll.fullScroll(View.FOCUS_UP);

        //add sound pool for icon "add to favorite"
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
        soundIdAdd = sp.load(this, R.raw.add_to_fav_r, 1);
        soundFry = sp.load(this, R.raw.fry, 1);
        soundBoil = sp.load(this, R.raw.boil, 1);
        soundCut = sp.load(this, R.raw.cut, 1);

        //for left button in actionBar for back
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        //get resurses for all images in original order
        Resources res = getResources();
        TypedArray dishimg = res.obtainTypedArray(R.array.dish_images);

        // find view
        tvRandomTitle = (TextView)findViewById(R.id.tv_title_recipe);
        tvRandomTime = (TextView)findViewById(R.id.tv_random_time);
        ivRandomImg = (ImageView)findViewById(R.id.ivDish);
        tvRandomRecipe = (TextView)findViewById(R.id.tv_pecipe);

        //open DB connection
        db = new DB(this);
        db.open();

        //take first random dish on start Activity
        Dish dish = db.getRandomDish();
        Log.d(LOG_TAG, "Start getting object Dish");
        thisTitle = dish.title;
        tvRandomTitle.setText(thisTitle);
        String soundOfDish = dish.sound;
        if (soundOfDish.equals("fry")) {
            sp.play(soundFry, 1, 1, 0, 0, 1);
        } else if (soundOfDish.equals("boil")) {
            sp.play(soundBoil, 1, 1, 0, 0, 1);
        } else {
            sp.play(soundCut, 1, 1, 0, 0, 1);
        }
        tvRandomTime.setText(getText(R.string.cooking_time) +" " + (String.valueOf(dish.cookedTime))
                + " "+ getText(R.string.minutes));
        //use dish ID as number for image index
        ivRandomImg.setImageResource(dishimg.getResourceId(dish.img, 0));
        tvRandomRecipe.setText(dish.recipe);
        Log.d(LOG_TAG, "-- resipe was set ---");

        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_random_dish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_fav:
                //next code moved from ADD button
                //write current recipe to favorite list
            {
                sPrefIndx = getSharedPreferences("myPrefIndex", MODE_PRIVATE);
                SharedPreferences.Editor edIndx = sPrefIndx.edit();
                p = sPrefIndx.getInt("myPrefIndex", MODE_PRIVATE);
                if (p > -1) {
                    p +=1;
                    if (p < 11) {
                        Toast.makeText(this, (getText(R.string.added_to_favorite)), Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "p = " + p);

                        sp.play(soundIdAdd, 1, 1, 0, 0, 1);

                        sPref = getSharedPreferences("myPref", MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();

                        String indx = Integer.toString(p);
                        ed.putString(SAVED_TEXT + indx, thisTitle);
                        ed.commit();
                        edIndx.putInt("myPrefIndex", p);
                        edIndx.commit();

                        Log.d(LOG_TAG, "saved to SharedPreferences: " + thisTitle + " with index: " + SAVED_TEXT + indx);
                        try {
                            //for prevent using sound of button too much
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, R.string.limit_favorite, Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, "Cant saved to SharedPreferences!!! " + thisTitle + " where p : " + p);
                    }
                } else {
                   //indext +1 - use on begin if saving
                    edIndx.putInt("myPrefIndex", p);
                    edIndx.commit();
                    Log.d(LOG_TAG, "else: p += 1 is true:" + p);
                }
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clickNextDish(View view) {
        //scrollView focus on start
        scroll.fullScroll(View.FOCUS_UP);

        //same code as on start
        Resources res = getResources();
        TypedArray dishimg = res.obtainTypedArray(R.array.dish_images);

        db.open();

        Dish dish = db.getRandomDish();
        thisTitle = dish.title;
        String soundOfDish = dish.sound;
        if (soundOfDish.equals("fry")) {
            sp.play(soundFry, 1, 1, 0, 0, 1);
        } else if (soundOfDish.equals("boil")) {
            sp.play(soundBoil, 1, 1, 0, 0, 1);
        } else {
            sp.play(soundCut, 1, 1, 0, 0, 1);
        }
        tvRandomTitle.setText(thisTitle);
        String dishTime = String.valueOf(dish.cookedTime);
        Log.d(LOG_TAG, "dishTime: " + dishTime);
        Log.d(LOG_TAG, "timeText: " + getText(R.string.cooking_time));
        Log.d(LOG_TAG, "min: " + min);
        tvRandomTime.setText(getText(R.string.cooking_time) + " "+ dishTime + " " + getText(R.string.minutes));

        ivRandomImg.setImageResource(dishimg.getResourceId(dish.img, 0));
        //ivRandomImg.setBackgroundResource(dishimg.getResourceId(dish.img, 0));
        tvRandomRecipe.setText(dish.recipe);

        db.close();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.d(LOG_TAG, "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
    }
}
