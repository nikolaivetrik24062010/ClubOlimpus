package com.example.clubolimpus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clubolimpus.data.ClubOlimpusContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MEMBER_LOAEDR = 123;
    MemberCursorAdapter memberCursorAdapter;

    ListView dataListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataListView = findViewById(R.id.dataListView);

        FloatingActionButton floatingActionButton =
                findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        AddMemberActivity.class);
                startActivity(intent);
            }
        });

        memberCursorAdapter = new MemberCursorAdapter(this, null, false);
        dataListView.setAdapter(memberCursorAdapter);

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
                Uri currentMemberUri = ContentUris.withAppendedId
                        (ClubOlimpusContract.MemberEntry.CONTENT_URI, l);
                intent.setData(currentMemberUri);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(MEMBER_LOAEDR, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                ClubOlimpusContract.MemberEntry._ID,
                ClubOlimpusContract.MemberEntry.COLUMN_FIRST_NAME,
                ClubOlimpusContract.MemberEntry.COLUMN_LAST_NAME,
                ClubOlimpusContract.MemberEntry.COLUMN_SPORT
        };

        CursorLoader cursorLoader = new CursorLoader(this,
                ClubOlimpusContract.MemberEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        memberCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        memberCursorAdapter.swapCursor(null);
    }
}