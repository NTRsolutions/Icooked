package com.porodem.porod.icooked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by porod on 02.09.2015.
 */
public class DB {

    public static final String LOG_TAG = "myLogs";

    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_DISH = "dishtab";
    private static final String DB_TABLE_INGR = "ingrtab";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SOUND = "sound";
    public static final String COLUMN_IMAGE = "img";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_INGR_LIST = "ingr_list";
    public static final String COLUMN_RECIPE = "recipe";

    public static final String COLUMN_INGR_1 = "ingr_1";
    public static final String COLUMN_INGR_2 = "ingr_2";
    public static final String COLUMN_INGR_3 = "ingr_3";
    public static final String COLUMN_INGR_4 = "ingr_4";





    private static final String DB_CREATE_DISH =
            "create table " + DB_TABLE_DISH + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_TITLE + " text, " +
                    COLUMN_SOUND + " sound, " +
                    COLUMN_IMAGE + " integer, " +
                    COLUMN_TIME + " integer, " +
                    COLUMN_INGR_LIST + " text, " +
                    COLUMN_RECIPE + " text" +
                    ");";

    private static final String DB_CREATE_INGR =
            "create table " + DB_TABLE_INGR + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_INGR_1 + " text, " +
                    COLUMN_INGR_2 + " text, " +
                    COLUMN_INGR_3 + " text, " +
                    COLUMN_INGR_4 + " text" +
                    ");";

    private final Context mCtx;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    int quantity;

    public DB (Context ctx) {
        mCtx = ctx;
    }
    Cursor c;

    //open connection
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    //close conection
    public void close(){
        if (mDBHelper != null) mDBHelper.close();
    }

    public boolean checkOnEmpty() {
        Cursor c = mDB.query(DB_TABLE_DISH, null, null, null, null, null, null);
        boolean clerrBase = c.moveToFirst();
        return clerrBase;
    }

    public Cursor getAllDataDish() {
        return mDB.query(DB_TABLE_DISH, null, null, null, null, null, null);
    }

    public Cursor getAllDataIngr() {
        return mDB.query(DB_TABLE_INGR, null, null, null, null, null, null);
    }
// get Dishes with current ingredient
    public Cursor getDishWithMyIng(String myIngr, String myIngr2, String myIngr3, String myIngr4) {
        Log.d(LOG_TAG, "--- INNER JOIN with rawQuery---");
        String sqlQuery = "SELECT dishtab.title "
                + "from dishtab "
                + "inner join ingrtab "
                + "on dishtab._id = ingrtab._id "
                + "where ingrtab.ingr_1 = ? OR ingr_2 = ? OR ingr_3 = ? OR ingr_4 = ?";
         c = mDB.rawQuery(sqlQuery, new String[] {myIngr, myIngr2, myIngr3, myIngr4} );
        return c;
    }

    String ingr1;
    String ingr2;
    String ingr3;
    String ingr4;
    public void getMyIngr(String ...ingr){
        Log.d(LOG_TAG, "--- getMyIngr --- length: " + ingr.length);
        switch (ingr.length) {
            case 1: this.ingr1 = ingr[0];
                    quantity = 1;
                    break;
            case 2: this.ingr1 = ingr[0];
                    this.ingr2 = ingr[1];
                quantity = 2;
                    break;
            case 3: this.ingr1 = ingr[0];
                    this.ingr2 = ingr[1];
                    this.ingr3 = ingr[2];
                quantity = 3;
                    break;
            case 4: this.ingr1 = ingr[0];
                    this.ingr2 = ingr[1];
                    this.ingr3 = ingr[2];
                    this.ingr4 = ingr[3];
                quantity = 4;
                    break;
            default: Log.d(LOG_TAG, "getMyIngr wrong case");

        }
        /*
        this.ingr1 = ingr1;
        this.ingr2 = ingr2;
        if (ingr2.equals("null")) this.ingr2 = "empty";
        this.ingr3 = ingr3;
        if (ingr3.equals("null")) this.ingr3 = "empty";
        this.ingr4 = ingr4;
        if (ingr4.equals("null")) this.ingr4 = "empty";
        */
        Log.d(LOG_TAG, "--- getMyIngr COMPLETE---");
    }

    // get Dishes with current ingredient
    public Cursor getDishWithMyIng1() {
        Log.d(LOG_TAG, "--- INNER JOIN with rawQuery---");
        //String sqlQuery = "SELECT dishtab.title "
        String sqlQuery;
        switch (quantity) {
            case 1: sqlQuery = "SELECT title, dishtab._id, ingrtab._id "
                    + "from dishtab "
                    + "inner join ingrtab "
                    + "on dishtab._id = ingrtab._id "
                    + "where (ingrtab.ingr_1 = @ingr1 OR ingrtab.ingr_2 = @ingr1 OR ingrtab.ingr_3 = @ingr1 OR ingrtab.ingr_4 = @ingr1)";
                    c = mDB.rawQuery(sqlQuery, new String[] {ingr1} );
                    break;

            case 2: sqlQuery = "SELECT title, dishtab._id, ingrtab._id "
                    + "from dishtab "
                    + "inner join ingrtab "
                    + "on dishtab._id = ingrtab._id "
                    + "where (ingrtab.ingr_1 = @ingr1 OR ingrtab.ingr_2 = @ingr1 OR ingrtab.ingr_3 = @ingr1 OR ingrtab.ingr_4 = @ingr1) AND " +
                    " (ingrtab.ingr_1 = @ingr2 OR ingrtab.ingr_2 = @ingr2 OR ingrtab.ingr_3 = @ingr2 OR ingrtab.ingr_4 = @ingr2)";
                    c = mDB.rawQuery(sqlQuery, new String[] {ingr1, ingr2} );
                    break;

            case 3: sqlQuery = "SELECT title, dishtab._id, ingrtab._id "
                    + "from dishtab "
                    + "inner join ingrtab "
                    + "on dishtab._id = ingrtab._id "
                    + "where (ingrtab.ingr_1 = @ingr1 OR ingrtab.ingr_2 = @ingr1 OR ingrtab.ingr_3 = @ingr1 OR ingrtab.ingr_4 = @ingr1) AND " +
                    " (ingrtab.ingr_1 = @ingr2 OR ingrtab.ingr_2 = @ingr2 OR ingrtab.ingr_3 = @ingr2 OR ingrtab.ingr_4 = @ingr2) AND " +
                    " (ingrtab.ingr_1 = @ingr3 OR ingrtab.ingr_2 = @ingr3 OR ingrtab.ingr_3 = @ingr3 OR ingrtab.ingr_4 = @ingr3)";
                    c = mDB.rawQuery(sqlQuery, new String[] {ingr1, ingr2, ingr3} );
                    break;

            case 4: sqlQuery = "SELECT title, dishtab._id, ingrtab._id "
                    + "from dishtab "
                    + "inner join ingrtab "
                    + "on dishtab._id = ingrtab._id "
                    + "where (ingrtab.ingr_1 = @ingr1 OR ingrtab.ingr_2 = @ingr1 OR ingrtab.ingr_3 = @ingr1 OR ingrtab.ingr_4 = @ingr1) AND " +
                    " (ingrtab.ingr_1 = @ingr2 OR ingrtab.ingr_2 = @ingr2 OR ingrtab.ingr_3 = @ingr2 OR ingrtab.ingr_4 = @ingr2) AND" +
                    " (ingrtab.ingr_1 = @ingr3 OR ingrtab.ingr_2 = @ingr3 OR ingrtab.ingr_3 = @ingr3 OR ingrtab.ingr_4 = @ingr3) AND" +
                    " (ingrtab.ingr_1 = @ingr4 OR ingrtab.ingr_2 = @ingr4 OR ingrtab.ingr_3 = @ingr4 OR ingrtab.ingr_4 = @ingr4)";
                    c = mDB.rawQuery(sqlQuery, new String[] {ingr1, ingr2, ingr3, ingr4} );
                    break;
        }
        Log.d(LOG_TAG, "--- getDishWithMyIng1 RETURNED ---");
        return c;

    }

    public Dish getFavDish(String favTitle) {
        String sqlQuery = "SELECT * FROM dishtab "
                + "WHERE title = ?";
        c = mDB.rawQuery(sqlQuery, new String[] {favTitle});
        c.moveToFirst();
        Log.d(LOG_TAG, "favTitle: " + favTitle);
        int titleInt = c.getColumnIndex(COLUMN_TITLE);
        int soundInt = c.getColumnIndex(COLUMN_SOUND);
        int imgInt = c.getColumnIndex(COLUMN_IMAGE);
        int timeInt = c.getColumnIndex(COLUMN_TIME);
        int ingrListInt = c.getColumnIndex(COLUMN_INGR_LIST);
        int recipeInt = c.getColumnIndex(COLUMN_RECIPE);

        String title = c.getString(titleInt);
        String sound = c.getString(soundInt);
        int img = c.getInt(imgInt);
        int time = c.getInt(timeInt);
        String ingrList = c.getString(ingrListInt);
        String recipe = c.getString(recipeInt);

        Dish dish = new Dish();
        dish.title = title;
        dish.sound = sound;
        dish.img = img;
        Log.d(LOG_TAG, "----- dish.img = " + img);
        dish.cookedTime = time;
        dish.ingrList = ingrList;
        dish.recipe = recipe;

        return dish;
    }


    public Dish getRandomDish() {
        //generate random number of our Dishes
        int random = 1 + (int)(Math.random() * ((47 - 1) + 1));
        Log.d(LOG_TAG, "--- getThisData ---");
        Log.d(LOG_TAG, "Id is: ");
        String selection = "_id = ?";
        //String xid = String.valueOf(id);
        Log.d(LOG_TAG, " random number:" + random);
        String randomS = Integer.toString(random);
        String [] selectionArgs = new String[]{randomS};
        c = mDB.query(DB_TABLE_DISH, null, selection, selectionArgs, null, null, null );
        c.moveToFirst();
        Log.d(LOG_TAG, " -----> move to first ----" + random);
        int titleInt = c.getColumnIndex(COLUMN_TITLE);
        int soundInt = c.getColumnIndex(COLUMN_SOUND);
        int imgInt = c.getColumnIndex(COLUMN_IMAGE);
        int timeInt = c.getColumnIndex(COLUMN_TIME);
        int ingrListInt = c.getColumnIndex(COLUMN_INGR_LIST);
        int recipeInt = c.getColumnIndex(COLUMN_RECIPE);

        String title = c.getString(titleInt);
        Log.d(LOG_TAG, " String title:" + title);
        String sound = c.getString(soundInt);
        int img = c.getInt(imgInt);
        int time = c.getInt(timeInt);
        String ingrList = c.getString(ingrListInt);
        String recipe = c.getString(recipeInt);

        Dish dish = new Dish();
        dish.title = title;
        dish.sound = sound;
        dish.img = img;
        Log.d(LOG_TAG, "----- dish.img = " + img);
        dish.cookedTime = time;
        dish.ingrList = ingrList;
        dish.recipe = recipe;

        return dish;
    }




// метод добавления одного блюда - при старте приложения выполняется столько раз сколько блюд
//в файле JSON
    public void addDish(String title, String sound, int img, int time, String ingr, String recipe) {
        ContentValues cv1 = new ContentValues();
        cv1.put(COLUMN_TITLE, title);
        Log.d(LOG_TAG, "Added title to DB: " + title);
        cv1.put(COLUMN_SOUND, sound);
        Log.d(LOG_TAG, "Added title to DB: " + sound);
        cv1.put(COLUMN_IMAGE, img);
        Log.d(LOG_TAG, "Added img to DB: " + img);
        cv1.put(COLUMN_TIME, time);
        Log.d(LOG_TAG, "Added time to DB: " + time);
        cv1.put(COLUMN_INGR_LIST, ingr);
        Log.d(LOG_TAG, "Added ingr to DB: " + ingr);
        cv1.put(COLUMN_RECIPE, recipe);
        Long rowID = mDB.insert(DB_TABLE_DISH, null, cv1);
        Log.d(LOG_TAG, "Added record with id: " + rowID);
    }

    //add ingredients for curremt dish to DB table ingrtab
    public void addIngr(String ingr1, String ingr2, String ingr3, String ingr4){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_INGR_1, ingr1);
        cv.put(COLUMN_INGR_2, ingr2);
        cv.put(COLUMN_INGR_3, ingr3);
        cv.put(COLUMN_INGR_4, ingr4);
        Long rowID = mDB.insert(DB_TABLE_INGR, null, cv);
        Log.d(LOG_TAG, "Added record ingredients_id: " + rowID);

    }

    // ----- Class for create and handle DB -----

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_DISH);
            Log.d(LOG_TAG, "--- TABLE DISH CREATED---");
/*
            ContentValues cv = new ContentValues();
            for (int i = 1; i < 2; i++) {
                cv.put(COLUMN_TITLE, "title dish " + i);
                cv.put(COLUMN_IMAGE, i);
                cv.put(COLUMN_TIME, 20);
                db.insert(DB_TABLE_DISH, null, cv);
                Log.d(LOG_TAG, "--- TABLE DISH CREATED---");
            }
*/
            db.execSQL(DB_CREATE_INGR);
            Log.d(LOG_TAG, "--- TABLE INGREDIENTS CREATED---");

/*
            for (int i = 1; i < 2; i++) {
                cv.clear();
                cv.put(COLUMN_INGR_1, "томат");
                cv.put(COLUMN_INGR_2, "картофель");
                cv.put(COLUMN_INGR_3, "мясо");
                db.insert(DB_TABLE_INGR, null, cv);
                Log.d(LOG_TAG, "--- TABLE INGREDIENTS CREATED---");
            }
            */
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }



    }
}
