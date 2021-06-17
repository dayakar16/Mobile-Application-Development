package com.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

/*
Assignement # InClass 03
FileName: DepartmentActivity.java
FullName of Students: Anoosh Hari and Dayakar Ravuri
*/

public class DepartmentActivity extends AppCompatActivity {

    private static final String TAG = "InClass-DepartActivity";
    RadioGroup radioGroup;

    Button submit;
    Button cancel;

    Intent intent;

    public static String DEPARTMENT_VALUE = "department_value";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        setTitle(getString(R.string.title_department));

        submit = findViewById(R.id.departmentSubmit);
        cancel = findViewById(R.id.departmentCancel);

        radioGroup = findViewById(R.id.radioGroup);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                int checkedId = radioGroup.getCheckedRadioButtonId();
                if (checkedId != -1){
                    if(checkedId == R.id.radioCS){
                        Log.d(TAG, "onClick: "+getString(R.string.department_cs));
                        intent.putExtra(DEPARTMENT_VALUE, getString(R.string.department_cs));
                        setResult(RESULT_OK, intent);
                    } else if(checkedId == R.id.radioSIS){
                        Log.d(TAG, "onClick: "+getString(R.string.department_sis));
                        intent.putExtra(DEPARTMENT_VALUE, getString(R.string.department_sis));
                        setResult(RESULT_OK, intent);
                    } else if(checkedId == R.id.radioBI){
                        Log.d(TAG, "onClick: "+getString(R.string.department_bi));
                        intent.putExtra(DEPARTMENT_VALUE, getString(R.string.department_bi));
                        setResult(RESULT_OK, intent);
                    } else if(checkedId == R.id.radioDS){
                        Log.d(TAG, "onClick: "+getString(R.string.department_ds));
                        intent.putExtra(DEPARTMENT_VALUE, getString(R.string.department_ds));
                        setResult(RESULT_OK, intent);
                    }
                    finish();
                } else {
//                    Log.d(TAG, "onClick: no department selected");
                    Toast.makeText(DepartmentActivity.this, getString(R.string.toast_select_department), Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}