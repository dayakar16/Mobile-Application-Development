package com.ahari.forumapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/*
    HW06
    ForumFragment
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class ForumFragment extends Fragment {

    private static final String FORUM = "FORUM";

    TextView title;
    TextView description;
    TextView author;
    TextView commentCounts;
    EditText comment;
    Button post;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    Forum forum;
    ForumFragmentAdapter adapter;
    LinearLayoutManager layoutManager;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

    ArrayList<Comment> comments = new ArrayList<>();

    String commentsText;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private AlertDialog.Builder builder;
    private Account account;

    public ForumFragment(Forum forum) {
        this.forum = forum;
    }

    public static ForumFragment newInstance(Forum forum) {
        ForumFragment fragment = new ForumFragment(forum);
        Bundle args = new Bundle();
        args.putSerializable(FORUM, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.forum = (Forum) getArguments().getSerializable(FORUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        recyclerView = view.findViewById(R.id.forumRecyclerView);
        title = view.findViewById(R.id.forumTitle);
        description = view.findViewById(R.id.forumDescription);
        author = view.findViewById(R.id.forumAuthor);
        commentCounts = view.findViewById(R.id.forumCommentSize);
        comment = view.findViewById(R.id.forumCommentBox);
        post = view.findViewById(R.id.forumPost);

        getActivity().setTitle(getString(R.string.forum_title));

        adapter = new ForumFragmentAdapter();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(getString(R.string.loading));

        commentsText = getString(R.string.comments);

        builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.error_dialogue_title));
        builder.setPositiveButton(getString(R.string.error_dialogue_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        getAccount();

        title.setText(forum.getTitle());
        description.setText(forum.getDescription());
        author.setText(forum.getCreatedBy().getName());

        fireStore.collection(getString(R.string.comments_document)).document(forum.forumId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        UserComments receivedComments = value.toObject(UserComments.class);
                        if (receivedComments != null) {
                            comments.clear();
                            comments.addAll(receivedComments.getComments());
                            commentCounts.setText(comments.size() + " "+commentsText);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = comment.getText().toString();
                if (commentText.isEmpty()){
                    builder.setMessage(getString(R.string.comment_description_error));
                    builder.show();
                } else if (comments.size() > 0) {
                    comments.add(new Comment(commentText, account, new Date()));
                    HashMap<String, Object> commentsUpdate = new HashMap<>();
                    commentsUpdate.put(getString(R.string.comments_document_key), comments);
                    fireStore.collection(getString(R.string.comments_document))
                            .document(forum.forumId)
                            .update(commentsUpdate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()) {
                                                               Toast.makeText(getActivity(), getString(R.string.create_comment_successful), Toast.LENGTH_SHORT).show();
                                                               comment.setText("");
                                                               adapter.notifyDataSetChanged();
                                                           } else {
                                                               builder.setMessage(task.getException() != null ? task.getException().getMessage() : getString(R.string.error_unknown));
                                                               builder.show();
                                                           }
                                                       }
                                                   }
                            );
                } else {
                    UserComments newCommentObject = new UserComments();
                    List<Comment> newCommentList = new ArrayList<>();
                    Comment newComment = new Comment();
                    newComment.setCreatedAt(new Date());
                    newComment.setCreatedBy(account);
                    newComment.setText(commentText);
                    newCommentList.add(newComment);
                    newCommentObject.getComments().clear();
                    newCommentObject.getComments().addAll(newCommentList);
                    fireStore.collection(getString(R.string.comments_document))
                            .document(forum.forumId)
                            .set(newCommentObject)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()) {
                                                               Toast.makeText(getActivity(), getString(R.string.create_comment_successful), Toast.LENGTH_SHORT).show();
                                                               comment.setText("");
                                                               adapter.notifyDataSetChanged();
                                                           } else {
                                                               builder.setMessage(task.getException() != null ? task.getException().getMessage() : getString(R.string.error_unknown));
                                                               builder.show();
                                                           }
                                                       }
                                                   }
                            );
                }
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

    class ForumFragmentAdapter extends RecyclerView.Adapter<ForumFragmentHolder> {

        @NonNull
        @Override
        public ForumFragmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.comment_list_item, parent, false);
            ForumFragmentHolder holder = new ForumFragmentHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ForumFragmentHolder holder, int position) {
            Comment comment = comments.get(position);
            holder.index = position;
            try {
                holder.box.setBackground(getResources().getDrawable(R.drawable.border));
                holder.comment.setText(comment.getText());
                holder.date.setText(dateFormat.format(comment.getCreatedAt()));
                holder.author.setText(comment.getCreatedBy().getName());
                holder.commentId = comment.getCommentId();
                if (comment.getCreatedBy().getName().equalsIgnoreCase(mAuth.getCurrentUser().getDisplayName())) {
                    holder.delete.setVisibility(View.VISIBLE);
                } else {
                    holder.delete.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e){
                comments.remove(comment);
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }

    class ForumFragmentHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView comment;
        TextView date;
        ImageView delete;
        ConstraintLayout box;
        String commentId;
        int index;

        public ForumFragmentHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.commentAuther);
            comment = itemView.findViewById(R.id.commentDescription);
            date = itemView.findViewById(R.id.commentDate);
            delete = itemView.findViewById(R.id.deleteCommentIcon);
            box = itemView.findViewById(R.id.commentBox);

            delete.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> updateForumLikedBy = new HashMap<>();
                    comments.remove(index);
                    updateForumLikedBy.put(getString(R.string.comments_document_key), comments);
                    fireStore.collection(getString(R.string.comments_document))
                            .document(forum.getForumId())
                            .update(updateForumLikedBy)
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), getString(R.string.delete_comment_successful), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(getActivity(), new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    builder.setMessage(e.getMessage());
                                    builder.show();
                                }
                            });
                }
            });
        }
    }
}