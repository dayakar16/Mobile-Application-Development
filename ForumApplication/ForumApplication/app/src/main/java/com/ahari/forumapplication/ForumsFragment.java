package com.ahari.forumapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

/*
    HW06
    ForumsFragment
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class ForumsFragment extends Fragment {

    private static final String TAG = "demo";

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button logout;
    Button newForum;
    IForumsFragmentListener listener;
    ArrayList<Forum> forums = new ArrayList<>();
    ForumsAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    Account account;


    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

    public ForumsFragment() {

    }

    public static ForumsFragment newInstance() {
        ForumsFragment fragment = new ForumsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forums, container, false);
        logout = view.findViewById(R.id.forumsFragmentLogout);
        newForum = view.findViewById(R.id.forumsFragmentNewForum);
        recyclerView = view.findViewById(R.id.forumsFragmentRecyclerView);

        getActivity().setTitle(getString(R.string.forums_title));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(getString(R.string.loading));

        builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.error_dialogue_title));
        builder.setPositiveButton(getString(R.string.error_dialogue_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ForumsAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getAccount();

        fireStore.collection(getString(R.string.forums_document)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                forums.clear();
                for (QueryDocumentSnapshot val : value) {
                    Forum forum = val.toObject(Forum.class);
                    forum.setForumId(val.getId());
                    forums.add(forum);
                }
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onEvent: " + forums);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                listener.launchLoginFragment();
            }
        });

        newForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchNewForumFragment();
            }
        });
        return view;
    }

    private void getAccount() {
        progressDialog.show();
        fireStore.collection(getString(R.string.accounts_document))
                .get().addOnSuccessListener(getActivity(), new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                    Account account1 = value.toObject(Account.class);
                    if (account1.getEmail().equalsIgnoreCase(mAuth.getCurrentUser().getEmail())) {
                        account = account1;
                        account.setAccountId(value.getId());
                        break;
                    }
                }
                if (account != null) {
                    progressDialog.dismiss();
                } else {
                    Account addAccount = new Account();
                    addAccount.setEmail(mAuth.getCurrentUser().getEmail());
                    addAccount.setName(mAuth.getCurrentUser().getDisplayName());
                    fireStore.collection(getString(R.string.accounts_document)).add(addAccount)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    task.getResult().getId();
                                    progressDialog.dismiss();
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IForumsFragmentListener) {
            listener = (IForumsFragmentListener) context;
        }
    }

    interface IForumsFragmentListener {
        void launchLoginFragment();

        void launchNewForumFragment();

        void launchForumFragment(Forum forum);
    }

    class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapterHolder> {

        @NonNull
        @Override
        public ForumsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forums_list_item, parent, false);
            ForumsAdapterHolder holder = new ForumsAdapterHolder(view);
            return holder;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(@NonNull ForumsAdapterHolder holder, int position) {
            Forum forum = forums.get(position);
            holder.createdDate.setText(dateFormat.format(forum.getCreatedAt()));
            holder.forumDescription.setText(forum.getDescription());
            Log.d(TAG, "onBindViewHolder: " + forum.getCreatedBy());
            holder.createrName.setText(forum.getCreatedBy().getName());
            holder.title.setText(forum.getTitle());
            holder.outline.setBackground(getResources().getDrawable(R.drawable.border));
            holder.likes.setText(forum.getLikedBy().size() + " " + getResources().getString(R.string.likes));
            holder.forumId = forum.forumId;
            holder.forum = forum;
            HashSet<String> likedUsers = new HashSet<>();
            forum.getLikedBy().stream().forEach(e -> likedUsers.add(e.getEmail()));
            if (likedUsers.contains(mAuth.getCurrentUser().getEmail())) {
                holder.likeIcon.setImageDrawable(getResources().getDrawable(R.drawable.like_favorite));
                holder.isLiked = true;
            } else {
                holder.likeIcon.setImageDrawable(getResources().getDrawable(R.drawable.like_not_favorite));
                holder.isLiked = false;
            }
            if (forum.getCreatedBy().getName().equalsIgnoreCase(mAuth.getCurrentUser().getDisplayName())) {
                holder.deleteIcon.setVisibility(View.VISIBLE);
            } else {
                holder.deleteIcon.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return forums.size();
        }
    }

    class ForumsAdapterHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView createrName;
        TextView forumDescription;
        TextView likes;
        TextView createdDate;
        ImageView deleteIcon;
        ImageView likeIcon;
        ConstraintLayout outline;
        String forumId;
        boolean isLiked;
        Forum forum;

        public ForumsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            createrName = itemView.findViewById(R.id.itemForumCreaterName);
            forumDescription = itemView.findViewById(R.id.itemForumDescription);
            likes = itemView.findViewById(R.id.itemForumLikes);
            createdDate = itemView.findViewById(R.id.itemForumCreated);
            deleteIcon = itemView.findViewById(R.id.imageViewDelete);
            likeIcon = itemView.findViewById(R.id.imageViewLike);
            outline = itemView.findViewById(R.id.outlineBox);

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fireStore.collection(getString(R.string.accounts_document))
                            .document(forumId).delete()
                            .addOnFailureListener(getActivity(), new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    builder.setMessage(e.getMessage());
                                    builder.show();
                                }
                            });
                }
            });

            likeIcon.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> updateForumLikedBy = new HashMap<>();
                    if (isLiked) {
                        updateForumLikedBy.put(getString(R.string.likedBy_document_key), forum.getLikedBy().stream()
                                .filter(e -> !e.getEmail().equalsIgnoreCase(account.getEmail())).collect(Collectors.toList()));
                    } else {
                        forum.getLikedBy().add(account);
                        updateForumLikedBy.put(getString(R.string.likedBy_document_key), forum.getLikedBy());
                    }
                    fireStore.collection(getString(R.string.forums_document))
                            .document(forumId)
                            .update(updateForumLikedBy);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.launchForumFragment(forum);
                }
            });
        }
    }
}