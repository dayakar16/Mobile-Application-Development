//Assignment InClass07
//File name ContactDetailsFragment
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
import android.widget.TextView;
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

public class ContactDetailsFragment extends Fragment {

    private static final String POSITION = "POSITION";

    String user;
    IContactDetailsListener listener;
    private final OkHttpClient client = new OkHttpClient();
    Activity activity;

    public ContactDetailsFragment(String user) {
        this.user = user;
    }

    public static ContactDetailsFragment newInstance(String user) {
        ContactDetailsFragment fragment = new ContactDetailsFragment(user);
        Bundle args = new Bundle();
        args.putString(POSITION, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = getArguments().getString(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        TextView name = view.findViewById(R.id.cDetailsNameValue);
        TextView email = view.findViewById(R.id.cDetailsEmailValue);
        TextView phone = view.findViewById(R.id.cDetailsNumberValue);
        TextView type = view.findViewById(R.id.cDetailsTypeValue);
        TextView id = view.findViewById(R.id.cDetailsIdValue);

        Button editButton = view.findViewById(R.id.cDetailsEdit);
        Button deleteButton = view.findViewById(R.id.cDetailsDelete);

        getActivity().setTitle(getString(R.string.contact_details));

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        String idForCall = user.split(",")[0];

        activity = getActivity();

        client.newCall(new Request.Builder()
                .url(getString(R.string.CONTACTS_BY_ID) + "/" + idForCall).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.popFromBackStack();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resp = response.body().string();
                if (response.isSuccessful() && !(resp.contains(idForCall) && resp.contains("not found"))) {
                    Contact contact = new Contact(resp);
                    user = contact.toString();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            name.setText(contact.getName());
                            email.setText(contact.getEmail());
                            phone.setText(contact.getPhone());
                            type.setText(contact.getType());
                            id.setText(contact.getId());
                            editButton.setEnabled(true);
                            deleteButton.setEnabled(true);
                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), resp, Toast.LENGTH_SHORT).show();
                            listener.popFromBackStack();
                        }
                    });
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.launchEditContactDetails(new Contact(user));
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.newCall(new Request.Builder()
                        .url(getString(R.string.CONTACTS_DELETE))
                        .post(new FormBody.Builder().add("id", idForCall).build())
                        .build())
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        alertMessage(e.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String responseBody = response.body().string();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("demo", "onResponse: " + responseBody);
                                        if (responseBody.contains("successful")) {
                                            Toast.makeText(activity, responseBody, Toast.LENGTH_SHORT).show();
                                            listener.popFromBackStack();
                                        } else {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                                            alert.setMessage(responseBody);
                                            alert.setTitle(activity.getResources().getString(R.string.message_title));
                                            alert.setPositiveButton(activity.getResources().getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    listener.popFromBackStack();
                                                }
                                            });
                                            alert.setNegativeButton(activity.getResources().getString(R.string.cancel_label), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    listener.popFromBackStack();
                                                }
                                            });
                                            alert.show();
                                        }
                                    }
                                });
                            }
                        });
            }
        });

        return view;
    }

    private void alertMessage(String e) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage(e);
        alert.setTitle(activity.getResources().getString(R.string.message_title));
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IContactDetailsListener) {
            this.listener = (IContactDetailsListener) context;
        }
    }

    interface IContactDetailsListener {
        void launchEditContactDetails(Contact user);

        void popFromBackStack();
    }
}