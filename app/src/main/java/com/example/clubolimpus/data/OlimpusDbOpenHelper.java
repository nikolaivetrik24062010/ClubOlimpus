package com.example.clubolimpus.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OlimpusDbOpenHelper extends SQLiteOpenHelper {
    public OlimpusDbOpenHelper(Context context) {
        super(context, ClubOlimpusContract.DATABASE_NAME,
                null, ClubOlimpusContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MEMBERS_TABLE = "CREATE TABLE " + ClubOlimpusContract.MemberEntry.TABLE_NAME + "("
                + ClubOlimpusContract.MemberEntry._ID + " INTEGER PRIMARY KEY,"
                + ClubOlimpusContract.MemberEntry.COLUMN_FIRST_NAME + " TEXT,"
                + ClubOlimpusContract.MemberEntry.COLUMN_LAST_NAME + " TEXT,"
                + ClubOlimpusContract.MemberEntry.COLUMN_GENDER + " INTEGER NOT NULL,"
                + ClubOlimpusContract.MemberEntry.COLUMN_SPORT + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_MEMBERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClubOlimpusContract.DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }
}
