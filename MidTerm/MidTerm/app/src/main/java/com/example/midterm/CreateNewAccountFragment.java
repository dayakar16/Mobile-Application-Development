package com.example.midterm;

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


public class CreateNewAccountFragment extends Fragment {




    public CreateNewAccountFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CreateNewAccountFragment newInstance(String param1, String param2) {
        CreateNewAccountFragment fragment = new CreateNewAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

     EditText name,mail,password;
     Button submt;
     TextView cancel;
    Iregisterlistener mlistener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Create New Account");
        View view = inflater.inflate(R.layout.fragment_create_new_account, container, false);
        name = view.findViewById(R.id.cr_name_id);
        mail = view.findViewById(R.id.cr_email_id);
        password = view.findViewById(R.id.cr_pass_id);
        submt = view.findViewById(R.id.cr_submit_id);
        cancel = view.findViewById(R.id.cr_cancel_id);

        submt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ml = mail.getText().toString();
                String pass = password.getText().toString();
                String nm = name.getText().toString();
                try {
                    if(nm == null || nm.isEmpty())
                        throw new Exception("Please enter name");
                    if(ml == null || ml.isEmpty())
                        throw new Exception("Please enter mail id");
                    if(pass == null || pass.isEmpty())
                        throw new Exception("Please enter password");
                    new BackGroundTaskByAsyncTask().execute(nm,ml,pass);
                } catch (Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.registertologin();
            }
        });


        return view;
    }


    class BackGroundTaskByAsyncTask
            extends AsyncTask<String, String, DataServices.AuthResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(DataServices.AuthResponse authResponse) {
            super.onPostExecute(authResponse);
            if(authResponse != null)
                mlistener.registertoforums(authResponse);
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
                response = DataServices.register(strings[0], strings[1],strings[2]);
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return response;
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Iregisterlistener)
        {
            mlistener = (Iregisterlistener)context;
        }
        else
            throw  new RuntimeException("Must implement IRegisterlistener");
    }

    public interface Iregisterlistener {
        void registertologin();
        void registertoforums(DataServices.AuthResponse authResponse);
    }
}