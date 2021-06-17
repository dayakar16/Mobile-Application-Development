package com.ahari.forumapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

/*
    HW06
    NewForumFragment
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class NewForumFragment extends Fragment {

    Button submit;
    EditText forumTitle;
    EditText forumDescription;
    TextView cancel;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Account createdBy;

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    public NewForumFragment() {

    }

    public static NewForumFragment newInstance() {
        NewForumFragment fragment = new NewForumFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_forum, container, false);
        forumTitle = view.findViewById(R.id.newForumTitle);
        forumDescription = view.findViewById(R.id.newForumDescription);
        submit = view.findViewById(R.id.newForumSubmit);
        cancel = view.findViewById(R.id.newForumCancel);

        getActivity().setTitle(getString(R.string.new_forum_title));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(getString(R.string.loading));

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
                fireStore.collection(getString(R.string.accounts_document))
                        .get().addOnSuccessListener(getActivity(), new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot value: queryDocumentSnapshots){
                            createdBy = value.toObject(Account.class);
                            if (createdBy.getEmail().equalsIgnoreCase(mAuth.getCurrentUser().getEmail())){
                                createdBy.setAccountId(value.getId());
                                break;
                            }
                        }

                        try {
                            String forumTitleString = forumTitle.getText().toString();
                            String forumDescriptionString = forumDescription.getText().toString();

                            if (forumTitleString.isEmpty()) {
                                throw new Exception(getString(R.string.forum_title_error));
                            }

                            if (forumDescriptionString.isEmpty()) {
                                throw new Exception(getString(R.string.forum_description_error));
                            }

                            Forum forum = new Forum();
                            forum.setTitle(forumTitleString);
                            forum.setDescription(forumDescriptionString);
                            forum.setCreatedAt(new Date());
                            forum.setCreatedBy(createdBy);
                            forum.setLikedBy(new ArrayList<>());

                            fireStore.collection(getString(R.string.forums_document))
                                    .add(forum)
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getActivity(), getString(R.string.create_forum_successful), Toast.LENGTH_SHORT).show();
                                                listener.launchForumFragmentBack();
                                            } else {
                                                builder.setMessage(task.getException() != null ? task.getException().getMessage() : getString(R.string.error_unknown));
                                                builder.show();
                                            }
                                        }
                                    });

                        } catch (Exception e){
                            builder.setMessage(e.getMessage());
                            builder.show();
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchForumFragmentBack();
            }
        });

        return view;
    }

    INewForumFragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof INewForumFragmentListener){
            listener = (INewForumFragmentListener) context;
        }
    }

    interface INewForumFragmentListener{
        void launchForumFragmentBack();
    }
}