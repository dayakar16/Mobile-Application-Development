package com.example.midterm;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private DataServices.AuthResponse authResponse;
    private DataServices.Forum forum;

    public ForumFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ForumFragment newInstance(DataServices.AuthResponse authResponse, DataServices.Forum forum) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, authResponse);
        args.putSerializable(ARG_PARAM2, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.authResponse = (DataServices.AuthResponse) getArguments().getSerializable(ARG_PARAM1);
            this.forum = (DataServices.Forum) getArguments().getSerializable(ARG_PARAM2);
        }
    }


    TextView title,name,description,numberofComment;
    EditText writecomment;
    Button post;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<DataServices.Comment> comments = new ArrayList<>();
    Recycleviewadapter adapter;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Forum");
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        title = view.findViewById(R.id.final_title);
        name = view.findViewById(R.id.final_name);
        description = view.findViewById(R.id.final_desc);
        numberofComment = view.findViewById(R.id.numberofcomments);
        writecomment = view.findViewById(R.id.final_writecomment);
        post = view.findViewById(R.id.final_post);
        title.setText(forum.getTitle());
        name.setText(forum.getCreatedBy().getName());
        description.setText(forum.getDescription());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading");


        recyclerView = view.findViewById(R.id.final_recycleview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        new BackGroundTaskByAsyncTask().execute();
        adapter = new Recycleviewadapter(comments);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);




        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = writecomment.getText().toString();
                try {
                    if (c == null || c.isEmpty())
                        throw new Exception("Please enter comment");
                    new CreateCommentTaskByAsyncTask().execute(c);
                } catch (Exception e)
                {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    class Recycleviewadapter extends  RecyclerView.Adapter<ViewHolder>{

        ArrayList<DataServices.Comment> list;

        public Recycleviewadapter(ArrayList<DataServices.Comment> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.reference2,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            DataServices.Comment comment = list.get(position);
            holder.commentname.setText(comment.createdBy.getName());
            holder.commentdescription.setText(comment.text);
            holder.commentdate.setText(comment.createdAt.toString());
            holder.comment = comment;
            if(comment.createdBy.getName().equals(authResponse.getAccount().getName()))
            {
                holder.delete.setVisibility(View.VISIBLE);
            }
            else
                holder.delete.setVisibility(View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

         TextView commentname,commentdescription,commentdate;
         ImageView delete;
          DataServices.Comment comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            commentname = itemView.findViewById(R.id.comment_name);
            commentdescription = itemView.findViewById(R.id.comment_description);
            commentdate = itemView.findViewById(R.id.comment_date);
            delete = itemView.findViewById(R.id.comment_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DeleteCommentTaskByAsyncTask().execute(comment);
                }
            });

        }
    }

    class BackGroundTaskByAsyncTask
            extends AsyncTask<String, String, ArrayList<DataServices.Comment>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<DataServices.Comment> list) {
            super.onPostExecute(list);
            comments.clear();
            comments .addAll(list);
            numberofComment.setText(comments.size() + " Comments");
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getContext(),values[0],Toast.LENGTH_LONG).show();
        }

        @Override
        protected ArrayList<DataServices.Comment> doInBackground(String... strings) {
            Log.d("daya"," In doInBackground");
            ArrayList<DataServices.Comment> list=null;
            try {
                list = DataServices.getForumComments(authResponse.getToken(),forum.getForumId());
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return list;
        }
    }

    class CreateCommentTaskByAsyncTask
            extends AsyncTask<String, String, ArrayList<DataServices.Comment>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<DataServices.Comment> list) {
            super.onPostExecute(list);
            comments.clear();
            comments .addAll(list);
            numberofComment.setText(comments.size() + " Comments");
            adapter.notifyDataSetChanged();
            writecomment.setText("");
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getContext(),values[0],Toast.LENGTH_LONG).show();
        }

        @Override
        protected ArrayList<DataServices.Comment> doInBackground(String... strings) {
            Log.d("daya"," In doInBackground");
            ArrayList<DataServices.Comment> list=null;
            try {
                DataServices.createComment(authResponse.getToken(),forum.getForumId(),strings[0]);
                list = DataServices.getForumComments(authResponse.getToken(),forum.getForumId());
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return list;
        }
    }

    class DeleteCommentTaskByAsyncTask
            extends AsyncTask<DataServices.Comment, String, ArrayList<DataServices.Comment>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<DataServices.Comment> list) {
            super.onPostExecute(list);
            comments.clear();
            comments .addAll(list);
            numberofComment.setText(comments.size() + " Comments");
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getContext(),values[0],Toast.LENGTH_LONG).show();
        }

        @Override
        protected ArrayList<DataServices.Comment> doInBackground(DataServices.Comment... comments) {
            Log.d("daya"," In doInBackground");
            ArrayList<DataServices.Comment> list=null;
            try {
                     DataServices.deleteComment(authResponse.getToken(),forum.getForumId(),comments[0].commentId);
                list = DataServices.getForumComments(authResponse.getToken(),forum.getForumId());
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return list;
        }
    }


}