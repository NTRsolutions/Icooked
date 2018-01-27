package com.porodem.porod.icooked;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowDish extends AppCompatActivity {

    public static final String LOG_TAG = "myLogs";

    TextView tvShowTitle;
    TextView tvShowTime;
    ImageView ivShowImg;
    TextView tvShowRecipe;

    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dish);

        //For back to homeActivity from action bar
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String favDish = intent.getStringExtra("vaule");
        Log.d(LOG_TAG, "favDish from intent: " + favDish);





        Resources res = getResources();
        TypedArray dishimg = res.obtainTypedArray(R.array.dish_images);

        tvShowTitle = (TextView)findViewById(R.id.tv_show_title);
        tvShowTime = (TextView)findViewById(R.id.tv_show_time);
        ivShowImg = (ImageView)findViewById(R.id.iv_show_Dish);
        tvShowRecipe = (TextView)findViewById(R.id.tv_show_pecipe);

        db = new DB(this);
        db.open();
        Dish dish = db.getFavDish(favDish);
        tvShowTitle.setText(dish.title);
        Log.d(LOG_TAG, "showTitle: " + dish.title);
        tvShowTime.setText(getText(R.string.cooking_time) +" " + (String.valueOf(dish.cookedTime))
                + " "+ getText(R.string.minutes));
        Log.d(LOG_TAG, "showTime: " + dish.cookedTime);
        ivShowImg.setBackgroundResource(dishimg.getResourceId(dish.img, 0));
        Log.d(LOG_TAG, "showImg: " + dish.img);
        tvShowRecipe.setText(dish.recipe);

        db.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add("очистить избранное");
        /*getMenuInflater().inflate(R.menu.menu_show_dish, menu);
        return true;*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
