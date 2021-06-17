//Assignment InClass07
//File name CreateContactFragment
// Dayakar Ravuri and Anoosh Hari Group 29 A



package com.todo.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class CreateContactFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Activity activity;

    String name_param = "name";
    String email_param = "email";
    String phone_param = "phone";
    String type_param = "type";

    ICreateContactListener listener;

    public CreateContactFragment() {

    }

    public static CreateContactFragment newInstance() {
        CreateContactFragment fragment = new CreateContactFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_contact, container, false);

        EditText name = view.findViewById(R.id.createContactNameValue);
        EditText email = view.findViewById(R.id.createContactEmailValue);
        EditText phone = view.findViewById(R.id.createContactNumberValue);
        EditText type = view.findViewById(R.id.createContactTypeValue);

        Button create = view.findViewById(R.id.createContactSave);
        Button cancel = view.findViewById(R.id.createContactCancel);

        getActivity().setTitle(getString(R.string.create_contact));

        activity = getActivity();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailString = email.getText().toString();
                String nameString = name.getText().toString();
                String phoneString = phone.getText().toString();
                String phoneTypeString = type.getText().toString();

                try {
                    if (nameString.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_name));
                    }
                    if (emailString.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_email));
                    }
                    if (phoneString.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_phone));
                    }
                    if (phoneTypeString.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_type));
                    }
                    callCreateContactAPI(name, phone, email, type);
                } catch (Exception e) {
                    alertMessage(e.getMessage());
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.popBackStackListener();
            }
        });

        return view;
    }

    private void alertMessage(String e) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(e);
        alert.setTitle(getString(R.string.message_title));
        alert.setPositiveButton(activity.getResources().getString(R.string.ok_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setNegativeButton(activity.getResources().getString(R.string.cancel_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void callCreateContactAPI(EditText name, EditText phone, EditText email, EditText type) {
        RequestBody body = new FormBody.Builder()
                .add(name_param, name.getText().toString())
                .add(phone_param, phone.getText().toString())
                .add(email_param, email.getText().toString())
                .add(type_param, type.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url(activity.getResources().getString(R.string.CONTACTS_CREATE))
                .post(body)
                .build();
        final Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("demo", "onClick: " + buffer.readUtf8());
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("demo", "run: " + responseBody);
                        Toast.makeText(getActivity(), responseBody, Toast.LENGTH_SHORT).show();
                    }
                });
                if (responseBody.contains("successfully")){
                    listener.popBackStackListener();
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertMessage(responseBody);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ICreateContactListener){
            this.listener = (ICreateContactListener) context;
        }
    }

    interface ICreateContactListener{
        void popBackStackListener();
    }
}