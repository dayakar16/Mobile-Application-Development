//Assignment InClass07
//File name EditContactFragment
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
import okio.Buffer;

public class EditContactFragment extends Fragment {

    private static final String USER = "USER";

    private Contact user;

    String id_param = "id";
    String name_param = "name";
    String email_param = "email";
    String phone_param = "phone";
    String type_param = "type";

    private OkHttpClient client = new OkHttpClient();

    Contact contact;

    IEditContactListener listener;

    public EditContactFragment(Contact user) {
        this.user = user;
    }

    public static EditContactFragment newInstance(Contact user) {
        EditContactFragment fragment = new EditContactFragment(user);
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = (Contact) getArguments().getSerializable(USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_contact, container, false);

        EditText name = view.findViewById(R.id.editContactNameValue);
        EditText email = view.findViewById(R.id.editContactEmailValue);
        EditText phone = view.findViewById(R.id.editContactNumberValue);
        EditText type = view.findViewById(R.id.editContactTypeValue);
        TextView id = view.findViewById(R.id.editContactIdValue);

        Button editContactSave = view.findViewById(R.id.editContactSave);
        Button editContactCancel = view.findViewById(R.id.editContactCancel);

        getActivity().setTitle(getString(R.string.edit_details));

        contact = user;
        name.setText(contact.getName());
        email.setText(contact.getEmail());
        phone.setText(contact.getPhone());
        type.setText(contact.getType());
        id.setText(contact.getId());

        Activity activity = getActivity() != null ? getActivity() : null;

        editContactSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(getActivity().getResources().getString(R.string.message_title));
                alert.setMessage(getString(R.string.edit_screen_popup));
                alert.setPositiveButton(getString(R.string.yes_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestBody body = new FormBody.Builder()
                                .add(id_param, user.getId())
                                .add(name_param, name.getText().toString())
                                .add(phone_param, phone.getText().toString())
                                .add(email_param, email.getText().toString())
                                .add(type_param, type.getText().toString())
                                .build();

                        Request request = new Request.Builder()
                                .url(activity.getResources().getString(R.string.CONTACTS_UPDATE))
                                .post(body)
                                .build();
                        final Buffer buffer = new Buffer();
                        try {
                            body.writeTo(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("demo", "onClick: " + buffer.readUtf8());
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                alertMessageNew(e.getMessage(), editContactCancel);
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String responseBody = response.body().string();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("demo", "run: " + responseBody);
                                        if (response.isSuccessful() || responseBody.toLowerCase().contains("unable".toLowerCase())) {
                                            alertMessageNew(responseBody, editContactCancel);
                                        } else {
                                            alertMessage(responseBody);
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                alert.setNegativeButton(getString(R.string.no_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        editContactCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.popFromBackStack();
            }
        });

        return view;
    }

    private void alertMessageNew(String resp, Button editContactCancel) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(resp);
        alert.setTitle(getActivity().getResources().getString(R.string.message_title));
        alert.setPositiveButton(getActivity().getResources().getString(R.string.ok_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.popFromBackStack();
            }
        });
        alert.setNegativeButton(getActivity().getResources().getString(R.string.cancel_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.popFromBackStack();
            }
        });
        alert.show();
    }

    private void alertMessage(String e) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(e);
        alert.setTitle(getString(R.string.message_title));
        alert.setPositiveButton(getActivity().getResources().getString(R.string.ok_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setNegativeButton(getActivity().getResources().getString(R.string.cancel_label), new DialogInterface.OnClickListener() {
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
        if (context instanceof IEditContactListener) {
            listener = (IEditContactListener) context;
        }
    }

    interface IEditContactListener{
        void popFromBackStack();
    }
}