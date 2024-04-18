package com.mini.program.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "db_mini_program.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE gs_00 (nip TEXT, nama TEXT, " +
                "status TEXT, jml_anak TEXT, jabatan TEXT, gaji TEXT, " +
                "masa_kerja TEXT, bonus_wisata TEXT, budget TEXT, gaji_bersih TEXT);");

        db.execSQL("CREATE TABLE gs_01 (groupmessage TEXT, idmessage TEXT, description TEXT, " +
                "language1 TEXT, language2 TEXT, language3 TEXT, language4 TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS gs_00");
    }

    public void executeSQL(String queryData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryData);
    }

    public Cursor get_cursordata(String queryData) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(queryData, null);
    }

    public String get_string(String queryData, int i) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(queryData, null)) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                cursor.moveToPosition(0);
                return cursor.getString(i);
            } else return "";
        }
    }

    public void insert_data (String nip, String nama, String status, String jml_anak,
                             String jabatan, String masa_kerja, String bonus_wisata,
                             String budget, String gaji_bersih, String gaji) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM gs_00 WHERE nip = '" + nip + "'");

        ContentValues contentValues = new ContentValues();
        contentValues.put("nip", nip);
        contentValues.put("nama", nama);
        contentValues.put("status", status);
        contentValues.put("jml_anak", jml_anak);
        contentValues.put("jabatan", jabatan);
        contentValues.put("masa_kerja", masa_kerja);
        contentValues.put("bonus_wisata", bonus_wisata);
        contentValues.put("budget", budget);
        contentValues.put("gaji_bersih", gaji_bersih);
        contentValues.put("gaji", gaji);

        db.insert("gs_00", null, contentValues);
    }

    public void insert_GS01 (String groupmessage, String idmessage, String description,
                              String language1, String language2, String language3,
                              String language4) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM gs_01 " +
                "         WHERE idmessage = '" + idmessage + "';");
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupmessage", groupmessage);
        contentValues.put("idmessage", idmessage);
        contentValues.put("description", description);
        contentValues.put("language1", language1);
        contentValues.put("language2", language2);
        contentValues.put("language3", language3);
        contentValues.put("language4", language4);
        db.insert("gs_01", null, contentValues);
    }

}
