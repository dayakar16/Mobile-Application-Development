package com.example.midterm;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginFragment extends Fragment {




    public LoginFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText email,password;
    Button login;
    TextView createnewaccount;
    Iloginlistener mlistener;
    ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Login");
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        email = view.findViewById(R.id.login_email_id);
        password = view.findViewById(R.id.login_pass_id);
        login = view.findViewById(R.id.login_login_id);
        createnewaccount = view.findViewById(R.id.login_create_id);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading");


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                try {
                    if(mail == null || mail.isEmpty())
                          throw new Exception("Please enter mail id");
                    if(pass == null || pass.isEmpty())
                           throw new Exception("Please enter password");
                    new BackGroundTaskByAsyncTask().execute(mail,pass);
                } catch (Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        });



        createnewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.logintoCreateNewAccountFragment();
            }
        });

        return view;
    }


    class BackGroundTaskByAsyncTask
            extends AsyncTask<String, String, DataServices.AuthResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            login.setEnabled(false);
            createnewaccount.setVisibility(View.INVISIBLE);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(DataServices.AuthResponse authResponse) {
            super.onPostExecute(authResponse);
            if(authResponse != null)
                mlistener.logintoForumsFragment(authResponse);
            login.setEnabled(true);
            createnewaccount.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getContext(),values[0],Toast.LENGTH_LONG).show();
        }

        @Override
        protected DataServices.AuthResponse doInBackground(String... strings) {
             Log.d("daya"," In doInBackground");
             DataServices.AuthResponse response= null;
            try {
                response = DataServices.login(strings[0], strings[1]);
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return response;
        }


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Iloginlistener)
        {
            mlistener = (Iloginlistener)context;
        }
        else
            throw new RuntimeException("Must implement Iloginlistener");

    }



    public interface Iloginlistener {
        void logintoForumsFragment(DataServices.AuthResponse authResponse);
        void logintoCreateNewAccountFragment();
    }
}