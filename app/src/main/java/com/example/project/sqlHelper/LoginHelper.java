package com.example.project.sqlHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.project.Model.User;

public class LoginHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "loginDB";
    public static final int DATABASE_VERSION =1;

    public static final String Tabel_user = "user";
    public static final String key_id = "id";
    public static final String key_username = "username";
    public static final String key_email = "email";
    public static final String key_password = "password";

    public static final String buat_Tabel =
            " CREATE TABLE " + Tabel_user + " ("
            + key_id + " INTEGER PRIMARY KEY, "
            + key_username + " TEXT, "
            + key_password + " TEXT, "
            + key_email + " TEXT" + " ) ";


    public LoginHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(buat_Tabel);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versiLama, int versiBaru) {
        Log.w(WorldListOpenHelper.class.getSimpleName(), "Update data dari"
                + versiLama + " ke " + versiBaru + "dimana akan menghapus semua data lama");
        db.execSQL(" DROP TABLE IF EXISTS " + Tabel_user);
        onCreate(db);
    }

    public void tambahUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(key_username, user.username);
        values.put(key_email, user.email);
        values.put(key_password, user.password);

        long todo_id = db.insert(Tabel_user, null, values);
    }

    public User Authenticate(User user){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Tabel_user,
                new String[]{key_id, key_username, key_email, key_password},
                key_email + "=?",
                new String[]{user.email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&&cursor.getCount()>0){
            User user1 = new User(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3));

            if (user.password.equalsIgnoreCase(user1.password)){
                return  user1;
            }
        }
        return null;
    }
    public boolean isEmailExist(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Tabel_user,
                new String[]{key_id, key_username, key_password, key_email},
                key_email + "=?",
                new String[]{email},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0){
            return true;
        }
        return false;
    }

}
