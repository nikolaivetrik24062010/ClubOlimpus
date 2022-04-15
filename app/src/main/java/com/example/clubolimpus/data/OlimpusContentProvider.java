package com.example.clubolimpus.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

public class OlimpusContentProvider extends ContentProvider {

    OlimpusDbOpenHelper dbOpenHelper;

    private static final int MEMBERS = 111;
    private static final int MEMBER_ID = 222;



    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(ClubOlimpusContract.AUTHORITY, ClubOlimpusContract.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(ClubOlimpusContract.AUTHORITY, ClubOlimpusContract.PATH_MEMBERS
                + "/#", MEMBER_ID);

    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new OlimpusDbOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                cursor = db.query(ClubOlimpusContract.MemberEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                break;

            case MEMBER_ID:
                String selection = ClubOlimpusContract.MemberEntry._ID + "=?";
                String[] selectionArds = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ClubOlimpusContract.MemberEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                break;

            default:
                Toast.makeText(getContext(), "Incorrect URI", Toast.LENGTH_LONG).show();
                throw new IllegalArgumentException("Can not query incorrect URI" + uri);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
