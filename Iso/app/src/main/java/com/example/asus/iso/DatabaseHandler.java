package com.example.asus.iso;

/**
 * Created by Asus on 19/12/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {
    // Versi database
    private static final int DATABASE_VERSION = 1;

    // Nama Database
    private static final String DATABASE_NAME = "db_user";

    // Nama Tabel
    private static final String TABLE_USER = "tb_user";

    // Colom pada tabel
    public static final String KEY_ID_USER = "id";
    public static final String KEY_NAMA = "nama";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TIPE_USER = "tipe_user";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Membuat Tabel
    @Override
    public void onCreate(SQLiteDatabase db) {
        dataAwal(db);
    }


    private void dataAwal(SQLiteDatabase db){

        String CREATE_modelUserS_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID_USER + " INTEGER PRIMARY KEY," + KEY_NAMA + " TEXT," + KEY_USERNAME + " TEXT," +
                KEY_PASSWORD + " TEXT," + KEY_EMAIL + " TEXT," + KEY_TIPE_USER + " TEXT)";
        db.execSQL(CREATE_modelUserS_TABLE);

        String masterAdmin = "INSERT INTO " + TABLE_USER +"("+KEY_NAMA+","+KEY_USERNAME+","+KEY_PASSWORD+","+KEY_EMAIL+","+KEY_TIPE_USER+")" +
                " VALUES ('Master Admin','admin','admin','admin@theheran.com','SUPER ADMIN')";
        db.execSQL(masterAdmin);
    }

    // Proses Reset data
    public void prosesResetData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String hapusTabel= "DROP TABLE " + TABLE_USER;
        db.execSQL(hapusTabel);
        dataAwal(db);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }


    // Tambah Data user Baru
    public void prosesTambahUser(ModelUser modelUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAMA, modelUser.getNama());
        values.put(KEY_USERNAME, modelUser.getUsername());
        values.put(KEY_PASSWORD, modelUser.getPassword());
        values.put(KEY_EMAIL, modelUser.getEmail());
        values.put(KEY_TIPE_USER, modelUser.getTipeUser());
        // Input Data
        db.insert(TABLE_USER, null, values);
        db.close();
        // Tutup koneksi database
    }

    // Update Data
    public int prosesUpdate(ModelUser modelUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAMA, modelUser.getNama());
        values.put(KEY_USERNAME, modelUser.getUsername());
        values.put(KEY_PASSWORD, modelUser.getPassword());
        values.put(KEY_EMAIL, modelUser.getEmail());
        values.put(KEY_TIPE_USER, modelUser.getTipeUser());
        //proses update tabel
        return db.update(TABLE_USER, values, KEY_ID_USER + " = ?",new String[] { String.valueOf(modelUser.getIdUser()) });
    }

    // Hapus Data
    public int prosesHapus(String idUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USER, KEY_ID_USER + " = ?",new String[] {idUser });
    }


    public ModelUser getUserById(String id){
        ModelUser mdUser=null;

        String selectQuery = "SELECT  * FROM " + TABLE_USER+" where "+KEY_ID_USER+"="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                mdUser = new ModelUser();

                mdUser.setIdUser(cursor.getString(cursor.getColumnIndex(KEY_ID_USER)));
                mdUser.setNama(cursor.getString(cursor.getColumnIndex(KEY_NAMA)));
                mdUser.setUsername(cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
                mdUser.setPassword(cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
                mdUser.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                mdUser.setTipeUser(cursor.getString(cursor.getColumnIndex(KEY_TIPE_USER)));
            } while (cursor.moveToNext());
        }

        // Tutup Koneksi
        cursor.close();
        db.close();

        return mdUser;
    }

    public ModelUser getLogin(String username,String Password){
        ModelUser mdUser=null;

        String selectQuery = "SELECT  * FROM " + TABLE_USER+" where "+KEY_USERNAME+"='"+username+"' and "+KEY_PASSWORD+"='"+Password+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                mdUser = new ModelUser();

                mdUser.setIdUser(cursor.getString(cursor.getColumnIndex(KEY_ID_USER)));
                mdUser.setNama(cursor.getString(cursor.getColumnIndex(KEY_NAMA)));
                mdUser.setUsername(cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
                mdUser.setPassword(cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
                mdUser.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                mdUser.setTipeUser(cursor.getString(cursor.getColumnIndex(KEY_TIPE_USER)));
                mdUser.setIsLogin(true);
            } while (cursor.moveToNext());
        }else{
            mdUser = new ModelUser();
            mdUser.setIsLogin(false);
        }

        // Tutup Koneksi
        cursor.close();
        db.close();

        return mdUser;
    }


    // Method ambil semua data user
    public   Cursor prosesAmbilSemuaDatauser(){

        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // returning cursor
        return cursor;
    }


}
