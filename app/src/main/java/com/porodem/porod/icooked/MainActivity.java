package com.porodem.porod.icooked;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "myLogs";


    final String SAVED_TEXT = "saved_text";

    EditText etIngr;

    DB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        //for ADMOB banner
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //for my Huawei
                .addTestDevice("326CC4AF774C0DEF99FFA64731058CE9")
                .build();
        mAdView.loadAd(adRequest);
*/

//comented 27.01.17 cause of error renderind preview
        //for
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_ico);

/*
        Resources res = getResources();
        TypedArray dishimg = res.obtainTypedArray(R.array.dish_images);
*/
        //open DB connection
        db = new DB(this);
        db.open();

//try take data from JSON object
        //check for existed table, if exist don't parse JSON again
        if (db.checkOnEmpty() == false) {
            try {
                Log.d(LOG_TAG, "start - TRY -" );
                JSONObject obj = new JSONObject(loadJSONFromRaw());
                //get array contained all dishes
                JSONArray m_jArry = obj.getJSONArray("dish");
                //get length of this array. how mant dishes in it?
                for (int i = 0; i < m_jArry.length(); i++) {
            //do it for every object in array
                    //create object for current number(index)
                    JSONObject jo_inside = m_jArry.getJSONObject(i);
                    //take string from just created object, and get title string
                    String dish = jo_inside.getString("title");
                    //get sound from same object
                    String sound = jo_inside.getString("sound");
                    Log.d(LOG_TAG, "----- created dish title: " + dish);
                    // create int for refer to image in future
                    int imgJSON = i;
                    //get time int
                    int time = jo_inside.getInt("time");
                    //get array for ingredients
                    JSONArray ingr = jo_inside.getJSONArray("ingr");
                    String ingredients = "состав: ";
                    String[] ingrpack = new String[4];
                    for (int u = 0; u < ingr.length(); u++) {
                        //get one ingredient for current index
                        String one_ingr = ingr.getString(u);
                        //add this ingredient to String
                        ingredients += one_ingr + " ";
                        ingrpack[u] = one_ingr;
                    }
                    //get recipe text
                    String recipe = jo_inside.getString("recipe");
                    Log.d(LOG_TAG, "----- created String ingredients: " + ingredients);
                    //write all this data to DB_TABLE_DISH
                    db.addDish(dish, sound, imgJSON, time, ingredients, recipe);

                    //take every ingridients from JSONArray "ingr" and write them in DB_TABLE_INGR
                    String ingr1;
                    String ingr2;
                    String ingr3;
                    String ingr4;

                    //check every ing for NULL, if NOT NULL, write to DataBase ingr with index,
                    // if ing = NULL write text "null" to DB_TABLE_INGR
                    if (ingrpack[0] != null) {
                        ingr1 = ingrpack[0];
                    } else {
                        ingr1 = "null";
                    }
                    if (ingrpack[1] != null) {
                        ingr2 = ingrpack[1];
                    } else {
                        ingr2 = "null";
                    }
                    if (ingrpack[2] != null) {
                        ingr3 = ingrpack[2];
                    } else {
                        ingr3 = "null";
                    }
                    if (ingrpack[3] != null) {
                        ingr4 = ingrpack[3];
                    } else {
                        ingr4 = "null";
                    }
                    //erite to base
                    db.addIngr(ingr1, ingr2, ingr3, ingr4);
                    Log.d(LOG_TAG, "----- recipe added ---- |OK|");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Log.d(LOG_TAG, "Try doesnt work" );
        }

        db.close();



    }

    //metod for log cursor (for test)
    void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, "Cursor is null");
    }

//get JSON file from "raw" folder of "res" folder
    public String loadJSONFromRaw() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.dish_json);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            is.close();
        }

        String jsonString = writer.toString();
        return jsonString;
    }

// Main Buttons
    //Get random dish
    public void randomCook(View view) {
        Intent intent = new Intent(MainActivity.this, RandomDish.class);
        startActivity(intent);
    }
    //Start another activity for find by ing
    public void ingrCook(View view) {

        Intent intent2 = new Intent(MainActivity.this, CheckIngredients.class);
        startActivity(intent2);
    }
    //Open saved favorite dishes
    public void clickFavorite(View view) {
        Intent intent = new Intent(MainActivity.this, Favorite.class);
        startActivity(intent);
    }
}
