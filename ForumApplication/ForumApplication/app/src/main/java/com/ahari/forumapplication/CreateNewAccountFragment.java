package com.ahari.forumapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/*
    HW06
    CreateNewAccountFragment
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class CreateNewAccountFragment extends Fragment {
    private static final String TAG = "demo";

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    public CreateNewAccountFragment() {

    }

    public static CreateNewAccountFragment newInstance() {
        CreateNewAccountFragment fragment = new CreateNewAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText name;
    EditText email;
    EditText password;
    Button submit;
    TextView cancel;
    ICreateNewAccountListener listener;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_account, container, false);
        name = view.findViewById(R.id.createNewFragementName);
        email = view.findViewById(R.id.createNewFragementEmail);
        password = view.findViewById(R.id.createNewFragementPassword);
        submit = view.findViewById(R.id.createNewFragementSubmit);
        cancel = view.findViewById(R.id.createNewFragementCancel);

        getActivity().setTitle(getString(R.string.create_new_account_title));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.error_dialogue_title));
        builder.setPositiveButton(getString(R.string.error_dialogue_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nameNew = name.getText().toString();
                    String emailNew = email.getText().toString();
                    String passwordNew = password.getText().toString();

                    if (nameNew.isEmpty()) {
                        throw new Exception(getString(R.string.name_error));
                    }

                    if (emailNew.isEmpty()) {
                        throw new Exception(getString(R.string.email_error));
                    }

                    if (passwordNew.isEmpty()) {
                        throw new Exception(getString(R.string.password_error));
                    }

                    mAuth.createUserWithEmailAndPassword(emailNew, passwordNew)
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(getActivity(), getString(R.string.registration_successful), Toast.LENGTH_SHORT).show();
                                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nameNew).build();
                                    authResult.getUser().updateProfile(request);
                                    Account account = new Account();
                                    account.setName(nameNew);
                                    account.setEmail(emailNew);
                                    fireStore.collection(getString(R.string.accounts_document))
                                            .add(account)
                                            .addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                }
                                            });
                                    listener.launchForumsFragment();
                                }
                            }).addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            builder.setMessage(e.getMessage());
                            builder.show();
                        }
                    });
                } catch (Exception e) {
                    builder.setMessage(e.getMessage());
                    builder.show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchLoginFragment();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ICreateNewAccountListener) {
            listener = (ICreateNewAccountListener) context;
        }
    }

    interface ICreateNewAccountListener {
        void launchForumsFragment();

        void launchLoginFragment();
    }
}