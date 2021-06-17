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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewForumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private DataServices.AuthResponse authResponse;


    public NewForumFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewForumFragment newInstance(DataServices.AuthResponse authResponse) {
        NewForumFragment fragment = new NewForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, authResponse);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.authResponse = (DataServices.AuthResponse)getArguments().getSerializable(ARG_PARAM1);

        }
    }

    EditText title,description;
    Button submit;
    TextView cancel;
    InewForumlistener mlistener;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("New Forum");
        View view = inflater.inflate(R.layout.fragment_new_forum, container, false);
        title = view.findViewById(R.id.new_title_id);
        description = view.findViewById(R.id.new_desc_id);
        submit = view.findViewById(R.id.new_submit_id);
        cancel = view.findViewById(R.id.new_cancel_id);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tl = title.getText().toString();
                String des = description.getText().toString();
                try {
                    if(tl == null || tl.isEmpty())
                        throw new Exception("Please enter title");
                    if(des == null || des.isEmpty())
                        throw new Exception("Please enter description");

                    new CreateAsyncTask().execute(tl,des);
                } catch (Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  mlistener.popnewtoformus();
            }
        });

        return view;
    }


    class CreateAsyncTask extends AsyncTask<String,String, ArrayList<DataServices.Forum>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<DataServices.Forum> list) {
            super.onPostExecute(list);
            mlistener.popnewtoformuswithnew(list);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getContext(),values[0],Toast.LENGTH_LONG).show();
        }

        @Override
        protected ArrayList<DataServices.Forum> doInBackground(String... strings) {
            Log.d("daya"," In doInBackground");
            ArrayList<DataServices.Forum> list=null;
            try {
                DataServices.createForum(authResponse.getToken(),strings[0],strings[1]);
                list = DataServices.getAllForums(authResponse.getToken());
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return list;

        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof InewForumlistener)
        {
            mlistener = (InewForumlistener)context;
        }
        else
            throw new RuntimeException("Must implement InewForumlistener");

    }



    public interface InewForumlistener {
        void popnewtoformus();
        void popnewtoformuswithnew(ArrayList<DataServices.Forum> forums);
    }

}