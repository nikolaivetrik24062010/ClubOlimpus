package com.example.clubolimpus.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
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
                throw new IllegalArgumentException("Can not query incorrect URI" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        String firstName = contentValues.getAsString(ClubOlimpusContract.MemberEntry.COLUMN_FIRST_NAME);
        if (firstName == null) {
            throw new IllegalArgumentException("You have to input first name");
        }

        String lastName = contentValues.getAsString(ClubOlimpusContract.MemberEntry.COLUMN_LAST_NAME);
        if (lastName == null) {
            throw new IllegalArgumentException("You have to input last name");
        }

        Integer gender = contentValues.getAsInteger(ClubOlimpusContract.MemberEntry.COLUMN_GENDER);
        if (gender == null || !(gender == ClubOlimpusContract.MemberEntry.GENDER_UNKNOWN || gender
                == ClubOlimpusContract.MemberEntry.GENDER_MALE || gender
                == ClubOlimpusContract.MemberEntry.GENDER_FEMALE)) {
            throw new IllegalArgumentException("You have to input correct gender");
        }

        String sport = contentValues.getAsString(ClubOlimpusContract.MemberEntry.COLUMN_SPORT);
        if (sport == null) {
            throw new IllegalArgumentException("You have to input sport");
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                Long id = db.insert(ClubOlimpusContract.MemberEntry.TABLE_NAME,
                        null, contentValues);
                if (id == -1){
                    Log.e("insertMethod", "Insertion on data in the tablen falied for " + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);

            default:
                throw new IllegalArgumentException("Insertion on data in the tablen falied for " + uri);
        }

    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsDeleted;

        switch (match) {
            case MEMBERS:

                rowsDeleted = db.delete(ClubOlimpusContract.MemberEntry.TABLE_NAME, s, strings);
                break;

            case MEMBER_ID:
                String selection = ClubOlimpusContract.MemberEntry._ID + "=?";
                String[] selectionArds = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ClubOlimpusContract.MemberEntry.TABLE_NAME, s, strings);
                break;

            default:
                throw new IllegalArgumentException("Can not delete this URI " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        if (contentValues.containsKey(ClubOlimpusContract.MemberEntry.COLUMN_FIRST_NAME)) {
            String firstName = contentValues.getAsString(ClubOlimpusContract.MemberEntry.COLUMN_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("You have to input first name");
            }
        }
        if (contentValues.containsKey(ClubOlimpusContract.MemberEntry.COLUMN_LAST_NAME)) {
            String lastName = contentValues.getAsString(ClubOlimpusContract.MemberEntry.COLUMN_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("You have to input last name");
            }
        }
        if (contentValues.containsKey(ClubOlimpusContract.MemberEntry.COLUMN_GENDER)) {

            Integer gender = contentValues.getAsInteger(ClubOlimpusContract.MemberEntry.COLUMN_GENDER);
            if (gender == null || !(gender == ClubOlimpusContract.MemberEntry.GENDER_UNKNOWN || gender
                    == ClubOlimpusContract.MemberEntry.GENDER_MALE || gender
                    == ClubOlimpusContract.MemberEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException("You have to input correct gender");
            }
        }
        if (contentValues.containsKey(ClubOlimpusContract.MemberEntry.COLUMN_SPORT)) {
            String sport = contentValues.getAsString(ClubOlimpusContract.MemberEntry.COLUMN_SPORT);
            if (sport == null) {
                throw new IllegalArgumentException("You have to input sport");
            }
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsUpdated;

        switch (match) {
            case MEMBERS:

                rowsUpdated = db.update(ClubOlimpusContract.MemberEntry.TABLE_NAME,
                        contentValues, s, strings);

                break;

            case MEMBER_ID:
                String selection = ClubOlimpusContract.MemberEntry._ID + "=?";
                String[] selectionArds = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(ClubOlimpusContract.MemberEntry.TABLE_NAME,
                        contentValues, s, strings);

                break;

            default:
                throw new IllegalArgumentException("Can not update this URI " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return  rowsUpdated;
    }
    @Override
    public String getType(Uri uri) {

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:

                return ClubOlimpusContract.MemberEntry.CONTENT_MULTIPLE_ITEMS;

            case MEMBER_ID:
                return ClubOlimpusContract.MemberEntry.CONTENT_SINGLE_ITEM;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
