package com.example.john.munchies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "DB_Context";

    private static final String TABLE_USERACCOUNT = "UserAccount";
    private static final String USER_ID = "UserID";
    private static final String USER_DNAME = "DisplayName";
    private static final String USER_PASSWORD = "Password";

    private static final String TABLE_RESTAURANT = "Restaurant";
    private static final String RESTAURANT_ID = "RestaurantID";
    private static final String RESTAURANT_NAME = "RestaurantName";

    private static final String TABLE_REGISTERED = "Registered";
    private static final String REGISTERED_ID = "RegisteredID";
    private static final String PASSWORD = "RegisteredPass";



    String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + TABLE_USERACCOUNT + "("
            + USER_ID + " INTEGER PRIMARY KEY, "
            + USER_DNAME + " TEXT, "
            + USER_PASSWORD + " TEXT " + ")";

    String CREATE_RESTAURANT_TABLE = "CREATE TABLE " + TABLE_RESTAURANT + "("
            + RESTAURANT_ID + " INTEGER PRIMARY KEY, "
            + RESTAURANT_NAME + " TEXT" + ")";

    String CREATE_REGISTERED_TABLE = "CREATE TABLE " + TABLE_REGISTERED + "("
            + REGISTERED_ID + " INTEGER, "
            + PASSWORD + " TEXT " + ")";

    String InsertIntoRegistered =
            "INSERT INTO " + TABLE_REGISTERED + "(" + REGISTERED_ID + ", " + PASSWORD + ") " +
                    "VALUES(300898431, confirmPassword), " +
                    "      (300451287, 'asdf987'), " +
                    "      (302487958, '987pass');";

    String InsertAdmin = "INSERT INTO " + TABLE_USERACCOUNT+ "(" + USER_ID + ", " + USER_DNAME+ ", " + USER_PASSWORD +") " +
                                 "VALUES (000999, 'Admin', 'password');";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_REGISTERED_TABLE);
        db.execSQL(InsertIntoRegistered);
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(InsertAdmin);
        db.execSQL(CREATE_RESTAURANT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTERED);
        onCreate(db);
    }

    //Admin AddRestaurant
    public boolean AddRestaurant(String restaurantName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RESTAURANT_NAME, restaurantName);
        long result = db.insert(TABLE_RESTAURANT, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    //Get Restaurants
    public Cursor getRestaurant(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_RESTAURANT;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public Cursor getRegisteredSchoolUser(String user, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_REGISTERED + " WHERE "
                + REGISTERED_ID + " = " + user + " AND "
                + PASSWORD + " = '" + pass + "'";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

//    public Cursor getRegisteredSchoolUser(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String sql = "SELECT * FROM " + TABLE_REGISTERED;
//        Cursor cursor = db.rawQuery(sql, null);
//        return cursor;
//    }

    public boolean AddUser(String id, String displayname, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, id);
        contentValues.put(USER_DNAME, displayname);
        contentValues.put(USER_PASSWORD, password);
        long result = db.insert(TABLE_USERACCOUNT, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Boolean UpdatePassword(String user, String oldPass, String newPass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_PASSWORD, newPass);
        String where = USER_DNAME + "=? AND " + USER_PASSWORD + "=? ";
        String[] whereArgs = {user, oldPass};
        long result = db.update(TABLE_USERACCOUNT,contentValues, where,  whereArgs);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor GetUser(String user, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_USERACCOUNT + " WHERE "
                + USER_DNAME + " = '" + user  + "' AND "
                + USER_PASSWORD + " = '"+ pass + "'";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public Cursor checkUser(String user){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_USERACCOUNT + " WHERE "
                + USER_ID + " = " + user ;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }


}
