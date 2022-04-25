package com.example.clubolimpus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.clubolimpus.data.ClubOlimpusContract;

public class AddMemberActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDIT_MEMBER_LOAEDR = 111;
    Uri currentMemberUri;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText sportEditText;
    private Spinner genderSpiner;
    private int gender = 0;
    private ArrayAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = getIntent();

        currentMemberUri = intent.getData();

        if (currentMemberUri == null) {
            setTitle("Add a member");
            invalidateOptionsMenu();
        } else {
            setTitle("Add a member");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOAEDR, null, this);
        }

        firstNameEditText = findViewById(R.id.fisrtNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        sportEditText = findViewById(R.id.sportEditText);
        genderSpiner = findViewById(R.id.genderSpinner);

        spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        genderSpiner.setAdapter(spinnerAdapter);
        genderSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGender = (String) adapterView.getItemAtPosition(i);
                if (!TextUtils.isEmpty(selectedGender)) {
                    if (selectedGender.equals("Male")) {
                        gender = ClubOlimpusContract.MemberEntry.GENDER_MALE;
                    }else if (selectedGender.equals("Female")) {
                        gender = ClubOlimpusContract.MemberEntry.GENDER_FEMALE;
                    }else {
                        gender = ClubOlimpusContract.MemberEntry.GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender = 0;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_member_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveMember:
                saveMember();
                return true;
            case R.id.deleteMember:
                showDeleteMemberDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void saveMember() {

        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String sport = sportEditText.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Input the first name",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Input the last name",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(sport)){
            Toast.makeText(this, "Input the sport",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (gender == ClubOlimpusContract.MemberEntry.GENDER_UNKNOWN){
            Toast.makeText(this, "Choose the gender",
                    Toast.LENGTH_LONG).show();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(ClubOlimpusContract.MemberEntry.COLUMN_FIRST_NAME, firstName);
        contentValues.put(ClubOlimpusContract.MemberEntry.COLUMN_LAST_NAME, lastName);
        contentValues.put(ClubOlimpusContract.MemberEntry.COLUMN_SPORT, sport);
        contentValues.put(ClubOlimpusContract.MemberEntry.COLUMN_GENDER, gender);

        if (currentMemberUri == null){
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(ClubOlimpusContract.MemberEntry.CONTENT_URI, contentValues);

            if (uri == null) {
                Toast.makeText(this, "Insertion on data in the tablen falied",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show();
            }
        }else {
            int rowsChanged = getContentResolver().update(currentMemberUri, contentValues,
                    null, null);
            if (rowsChanged == 0){
                Toast.makeText(this, "Saving data in the tablen falied",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Member updated", Toast.LENGTH_LONG).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                ClubOlimpusContract.MemberEntry._ID,
                ClubOlimpusContract.MemberEntry.COLUMN_FIRST_NAME,
                ClubOlimpusContract.MemberEntry.COLUMN_LAST_NAME,
                ClubOlimpusContract.MemberEntry.COLUMN_GENDER,
                ClubOlimpusContract.MemberEntry.COLUMN_SPORT
        };

        return new CursorLoader(this,
                currentMemberUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            int firstNameColumnIndex = data.getColumnIndex(
                    ClubOlimpusContract.MemberEntry.COLUMN_FIRST_NAME);
            int lastNameColumnIndex = data.getColumnIndex(
                    ClubOlimpusContract.MemberEntry.COLUMN_LAST_NAME);
            int genderColumnIndex = data.getColumnIndex(
                    ClubOlimpusContract.MemberEntry.COLUMN_GENDER);
            int sportColumnIndex = data.getColumnIndex(
                    ClubOlimpusContract.MemberEntry.COLUMN_SPORT);

            String firstName = data.getString(firstNameColumnIndex);
            String lastName = data.getString(lastNameColumnIndex);
            int gender = data.getInt(firstNameColumnIndex);
            String sport = data.getString(sportColumnIndex);

            firstNameEditText.setText(firstName);
            lastNameEditText.setText(lastName);
            sportEditText.setText(sport);

            switch (gender){
                case ClubOlimpusContract.MemberEntry.GENDER_MALE:
                    genderSpiner.setSelection(1);
                    break;
                case ClubOlimpusContract.MemberEntry.GENDER_FEMALE:
                    genderSpiner.setSelection(2);
                    break;
                case ClubOlimpusContract.MemberEntry.GENDER_UNKNOWN:
                    genderSpiner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
    private void showDeleteMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the member?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteMember();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (dialogInterface != null){
                            dialogInterface.dismiss();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteMember() {
        if (currentMemberUri != null){
            int rowsDeleted = getContentResolver().delete(currentMemberUri,
                    null, null);

            if (rowsDeleted == 0){
                Toast.makeText(this, "Deleting data from the tablen falied",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Member is deleted",
                        Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }
}