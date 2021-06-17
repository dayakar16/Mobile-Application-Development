package com.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/*
Assignement # InClass 03
FileName: ProfileActivity.java
FullName of Students: Anoosh Hari and Dayakar Ravuri
*/

public class ProfileActivity extends AppCompatActivity {

    TextView name;
    TextView email;
    TextView id;
    TextView dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle(getString(R.string.title_profile));

        name=findViewById(R.id.nameProfileValue);
        email=findViewById(R.id.emailProfileValue);
        id=findViewById(R.id.idProfileValue);
        dept=findViewById(R.id.deptProfileValue);

        Profile profile = (Profile) getIntent().getParcelableExtra(MainActivity.PROFILE_DATA);
        name.setText(profile.name);
        email.setText(profile.email);
        id.setText(profile.studentId+"");
        dept.setText(profile.department);
    }
}