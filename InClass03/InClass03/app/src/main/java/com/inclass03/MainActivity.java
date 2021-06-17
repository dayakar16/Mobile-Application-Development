package com.inclass03;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
Assignement # InClass 03
FileName: MainActivity.java
FullName of Students: Anoosh Hari and Dayakar Ravuri
*/

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "InClass-MainActivity";
//    public static final String NAME = "NAME";
//    public static final String EMAIL = "EMAIL";
//    public static final String STUDENT_ID = "ID";
//    public static final String DEPARTMENT = "DEPARTMENT";
    public static final String PROFILE_DATA = "PROFILE_DATA";

    EditText name;
    EditText email;
    EditText id;

    Button deptButton;
    Button submit;

    TextView deptValue;
    private static int REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.title_registration));

        name = findViewById(R.id.nameEntry);
        email = findViewById(R.id.emailEntry);
        id = findViewById(R.id.idEntry);

        deptButton = findViewById(R.id.deptSelect);
        submit = findViewById(R.id.submitButton);

        deptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent departmentIntent = new Intent(getString(R.string.intent_implicit));
                startActivityForResult(departmentIntent, REQ_CODE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submitIntent = new Intent(MainActivity.this, ProfileActivity.class);
                Profile profile;
                String name;
                String email;
                String studentId;
                String dept;
                try {
                    try {
                        name = MainActivity.this.name.getText().toString();
                        if (name.isEmpty()) throw new Exception();
                    } catch (Exception e) {
                        throw new Exception(getString(R.string.toast_name));
                    }

                    try {
                        email = MainActivity.this.email.getText().toString();
                        if (email.isEmpty() || !email.contains("@") || !email.contains(".")) throw new Exception();
                    } catch (Exception e) {
                        throw new Exception(getString(R.string.toast_email));
                    }

                    try {
                        studentId = MainActivity.this.id.getText().toString();
                        if (studentId.isEmpty()) throw new Exception();
                    } catch (Exception e) {
                        throw new Exception(getString(R.string.toast_id));
                    }

                    try {
                        dept = MainActivity.this.deptValue.getText().toString();
                        if (dept.isEmpty()) throw new Exception();
                    } catch (Exception e) {
                        throw new Exception(getString(R.string.toast_dept));
                    }

                    profile = new Profile(name, email, studentId, dept);
                    submitIntent.putExtra(PROFILE_DATA, profile);
                    startActivity(submitIntent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

//                    submitIntent.putExtra(NAME, name.getText());
//                    submitIntent.putExtra(EMAIL, email.getText());
//                    submitIntent.putExtra(STUDENT_ID, id.getText());
//                    submitIntent.putExtra(DEPARTMENT, deptValue.getText());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        deptValue = findViewById(R.id.deptLabel);
        if (resultCode != RESULT_CANCELED && data != null && data.getStringExtra(DepartmentActivity.DEPARTMENT_VALUE) != null) {
            deptValue.setText(data.getStringExtra(DepartmentActivity.DEPARTMENT_VALUE));
        }
        Log.d(TAG, "onActivityResult: ");
    }
}