package com.porodem.porod.icooked;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Favorite extends AppCompatActivity implements SoundPool.OnLoadCompleteListener {

    public static final String LOG_TAG = "myLogs";

    final String SAVED_TEXT = "saved_text";

    int soundDelFav;

    SharedPreferences sPref;
    SharedPreferences sPrefIndx;
    String[] favDishesList;

    ListView lvFavorite;

    SoundPool sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);

        soundDelFav = sp.load(this, R.raw.del_fav, 1);

        //For back to homeActivity from action bar
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        lvFavorite = (ListView)findViewById(R.id.lvFavorite);

        //извлекаем сохраненные данные из SaredPreferences и заполняем ими массив
        //14-09-2015
        favDishesList = new String[10];
        for (int i = 0; i < 10; i++) {
            sPref = getSharedPreferences("myPref", MODE_PRIVATE);
            String indx = Integer.toString(i+1);
            String favDish = sPref.getString(SAVED_TEXT + indx, "");
            favDishesList[i] = favDish;
            Log.d(LOG_TAG, "added to array: " + favDish);
        }

        Log.d(LOG_TAG, "favDishesList: " + favDishesList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, favDishesList);

        lvFavorite.setAdapter(adapter);

        lvFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Favorite.this, ShowDish.class);
                String selectedFromList = (lvFavorite.getItemAtPosition(position).toString());
                intent.putExtra("vaule",selectedFromList);
                startActivity(intent);
                Log.d(LOG_TAG, "selected item: " + selectedFromList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorite, menu);
        //menu.add("очистить избранное");
        /*getMenuInflater().inflate(R.menu.menu_show_dish, menu);
        return true;*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear: {
                sp.play(soundDelFav, 1, 1, 0, 0, 1);
                //ощичаем Shared Preferences с названиями блюд
                SharedPreferences.Editor ed = sPref.edit();
                ed.clear();
                ed.commit();

                //ощичаем Shared Preferences с индексом избранного блюда
                sPrefIndx = getSharedPreferences("myPrefIndex", MODE_PRIVATE);
                SharedPreferences.Editor edIndex = sPrefIndx.edit();
                edIndex.putInt("myPrefIndex", 0);
                edIndex.commit();
                Log.d(LOG_TAG, "SharedPreferences clear");
                Toast.makeText(this, "Список очищен", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Favorite.this, MainActivity.class);
                startActivity(intent);
            }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }
}
