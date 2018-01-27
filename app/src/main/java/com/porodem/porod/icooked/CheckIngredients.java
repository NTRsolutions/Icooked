package com.porodem.porod.icooked;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class CheckIngredients extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = "myLogs";

    AutoCompleteTextView actv1;
    AutoCompleteTextView actv2;
    AutoCompleteTextView actv3;
    AutoCompleteTextView actv4;

    String[] ingredients;

    ListView lvData;
    DB db;
    SimpleCursorAdapter scAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ingredients);

        //For back to homeActivity from action bar
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        ingredients = getResources().getStringArray(R.array.all_ingredients);

        actv1 = (AutoCompleteTextView)findViewById(R.id.autoCompleteIngr1);
        actv2 = (AutoCompleteTextView)findViewById(R.id.autoCompleteIngr2);
        actv3 = (AutoCompleteTextView)findViewById(R.id.autoCompleteIngr3);
        actv4 = (AutoCompleteTextView)findViewById(R.id.autoCompleteIngr4);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredients);
        actv1.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredients);
        actv2.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredients);
        actv3.setAdapter(adapter3);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredients);
        actv4.setAdapter(adapter4);

        //open DB connection
        db = new DB(this);
        db.open();

        //columns for adapter
        String[] from = new String[] {DB.COLUMN_TITLE};
        int[] to = new int[] {R.id.tvFoundTitle};


        //create adapter and config list
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
        lvData = (ListView)findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);



        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CheckIngredients.this, ShowDish.class);
                Cursor c = ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
                c.moveToPosition(position);
                int thisData = c.getColumnIndex(DB.COLUMN_TITLE);
                String readyData = c.getString(thisData);
                //String selectedFromList = lvData.getItemAtPosition(position).toString();
                //Log.d(LOG_TAG, "selected item: " + selectedFromList + " " + selectedFromList1);
                intent.putExtra("vaule", readyData);
                startActivity(intent);
                Log.d(LOG_TAG, "selected item: " + readyData);
            }
        });

        //add context menu to list
        registerForContextMenu(lvData);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_ingredients, menu);
        return true;
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

// КНОПКА - начать поиск по выбранным ингредиентам
    //BUTTON - start finding by chosen ingredients
    public void clickByIng(View view) {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        String myIngr = actv1.getText().toString();
        String myIngr2 = actv2.getText().toString();
        String myIngr3 = actv3.getText().toString();
        if (myIngr.equals("") && myIngr2.equals("") && myIngr3.equals("")){
            Toast.makeText(this, "введите значение", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(LOG_TAG, "--- clickByIng ---");


            String myIngr4 = "null";
            if (myIngr2.matches("")) {
                myIngr2 = "null";
            }
            if (myIngr3.matches("")) {
                myIngr3 = "null";
            }
            db.getMyIngr(myIngr, myIngr2, myIngr3, myIngr4);
            Log.d(LOG_TAG, "--- getMyIngr complete ---");
            Log.d(LOG_TAG, "--- Strings:" + myIngr + ":" + myIngr2 + ":" + myIngr3 + ":" + myIngr4);
        /*if (myIngr2.equals("") &&  myIngr3.equals("") && myIngr4.equals("")) {
            Log.d(LOG_TAG, "--- 3 last ingredients EMPTY ---");
            getLoaderManager().initLoader(0, null, this);
            getLoaderManager().getLoader(0).forceLoad();
        }*/
            //create loader for data reading
            getLoaderManager().initLoader(0, null, this);
            getLoaderManager().getLoader(0).forceLoad();
        }

    }

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

// код для управления Лоадером для списка найденых блюд
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class MyCursorLoader extends CursorLoader{

        DB db;

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        //metod for log cursor
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

        @Override
        public Cursor loadInBackground() {
//метод который выбирает какие данные отобразить в списке после нажатия на "поиск по ингредиентам"

            Cursor cursor = db.getDishWithMyIng1();
            logCursor(cursor);
            try {
                TimeUnit.SECONDS.sleep(1);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }
}
