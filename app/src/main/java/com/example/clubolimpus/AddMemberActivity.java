package com.example.clubolimpus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.clubolimpus.data.ClubOlimpusContract;

public class AddMemberActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText groupNameEditText;
    private Spinner genderSpiner;
    private int gender = 0;
    private ArrayAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        firstNameEditText = findViewById(R.id.fisrtNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        groupNameEditText = findViewById(R.id.groupEditText);
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
                return true;
            case R.id.deleteMember:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}