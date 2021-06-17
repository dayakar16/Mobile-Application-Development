package com.example.midterm;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    // TODO: Rename and change types of parameters
    private DataServices.AuthResponse authResponse;


    public ForumsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ForumsFragment newInstance(DataServices.AuthResponse authResponse) {
        ForumsFragment fragment = new ForumsFragment();
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

    Button logout,newforum;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Iforumlistener mlistener;
    RecycleviewAdapter adapter;
    ArrayList<DataServices.Forum> forums = new ArrayList<>();
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Forums");
        View view = inflater.inflate(R.layout.fragment_forums, container, false);
        logout = view.findViewById(R.id.forum_logout_id);
        newforum = view.findViewById(R.id.forum_newforum_id);
        recyclerView = view.findViewById(R.id.forum_recycleview);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new RecycleviewAdapter();


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading");


        new BackGroundTaskByAsyncTask().execute(authResponse.getToken());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);

        newforum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.forumtonewforumlistener();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.forumtologinlistener();
            }
        });

        return view;
    }

   class  RecycleviewAdapter extends  RecyclerView.Adapter<ViewHolder> {

       @NonNull
       @Override
       public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_reference_item,parent,false);
           ViewHolder viewHolder = new ViewHolder(view);
           return viewHolder;
       }

       @Override
       public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           DataServices.Forum forum = forums.get(position);
           holder.title.setText(forum.getTitle());
           holder.name.setText(forum.getCreatedBy().getName());
           holder.likes.setText(forum.getLikedBy().size() +" likes");
           holder.date.setText(forum.getCreatedAt().toString());
           holder.description.setText(forum.getDescription());
           holder.forum = forum;
           if(forum.getLikedBy().contains(authResponse.getAccount()))
           {
               holder.like.setImageDrawable(getResources().getDrawable(R.drawable.like_favorite));
               holder.isLike = true;
           } else {
               holder.like.setImageDrawable(getResources().getDrawable(R.drawable.like_not_favorite));
               holder.isLike = false;
           }
           if(forum.getCreatedBy().getName().equals(authResponse.getAccount().getName()))
           {
               holder.delete.setVisibility(View.VISIBLE);
           }
           else
               holder.delete.setVisibility(View.INVISIBLE);
       }


       @Override
       public int getItemCount() {
           return forums.size();
       }
   }

    public void getList(ArrayList<DataServices.Forum> forums) {
        forums.clear();
        forums.addAll(forums);
        adapter.notifyDataSetChanged();
    }

   public class ViewHolder extends  RecyclerView.ViewHolder {
       TextView title,name,description,likes,date;
       ImageView delete,like;
       DataServices.Forum forum;
       boolean isLike=false;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           title = itemView.findViewById(R.id.r_forum_title_id);
           name = itemView.findViewById(R.id.r_forum_name);
           description = itemView.findViewById(R.id.r_forum_descr_id);
           likes = itemView.findViewById(R.id.r_forum_likes);
           date = itemView.findViewById(R.id.r_forum_created_date);
           delete = itemView.findViewById(R.id.r_delete_id);
           like = itemView.findViewById(R.id.r_like_id);

           delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   new DeleteAsyncTask().execute(forum.getForumId());
               }
           });

           like.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                        if(!isLike)
                        {
                            new likeAsyncTask().execute(forum.getForumId());
                        }
                        else {
                            new UnlikeAsyncTask().execute(forum.getForumId());
                        }
                   }
           });

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   mlistener.forumstoforumlistener(forum);
               }
           });

       }
   }


       class likeAsyncTask extends AsyncTask<Long,String,ArrayList<DataServices.Forum>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<DataServices.Forum> list) {
            super.onPostExecute(list);
            forums.clear();
            forums.addAll(list);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getContext(),values[0],Toast.LENGTH_LONG).show();
        }

        @Override
        protected ArrayList<DataServices.Forum> doInBackground(Long... longs) {
            Log.d("daya"," In doInBackground");
            ArrayList<DataServices.Forum> list=null;
            try {
                DataServices.likeForum(authResponse.getToken(),longs[0]);
                list = DataServices.getAllForums(authResponse.getToken());
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return list;

        }
    }

    class UnlikeAsyncTask extends AsyncTask<Long,String,ArrayList<DataServices.Forum>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<DataServices.Forum> list) {
            super.onPostExecute(list);
            forums.clear();
            forums.addAll(list);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getContext(),values[0],Toast.LENGTH_LONG).show();
        }

        @Override
        protected ArrayList<DataServices.Forum> doInBackground(Long... longs) {
            Log.d("daya"," In doInBackground");
            ArrayList<DataServices.Forum> list=null;
            try {
                DataServices.unLikeForum(authResponse.getToken(),longs[0]);
                list = DataServices.getAllForums(authResponse.getToken());
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return list;

        }
    }


class DeleteAsyncTask extends AsyncTask<Long,String,ArrayList<DataServices.Forum>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<DataServices.Forum> list) {
        super.onPostExecute(list);
        forums.clear();
        forums.addAll(list);
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(getContext(),values[0],Toast.LENGTH_LONG).show();
    }

    @Override
    protected ArrayList<DataServices.Forum> doInBackground(Long... longs) {
        Log.d("daya"," In doInBackground");
        ArrayList<DataServices.Forum> list=null;
        try {
            DataServices.deleteForum(authResponse.getToken(),longs[0]);
            list = DataServices.getAllForums(authResponse.getToken());
        } catch (DataServices.RequestException e) {
            publishProgress(e.getMessage());
        }
        return list;

    }
}


class BackGroundTaskByAsyncTask
            extends AsyncTask<String, String, ArrayList<DataServices.Forum>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<DataServices.Forum> list) {
            super.onPostExecute(list);
              forums.clear();
             forums.addAll(list);
             adapter.notifyDataSetChanged();
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
                list = DataServices.getAllForums(strings[0]);
            } catch (DataServices.RequestException e) {
                publishProgress(e.getMessage());
            }
            return list;
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  Iforumlistener)
            mlistener = (Iforumlistener)context;
    }

    public interface Iforumlistener {
        void forumtonewforumlistener();
        void forumtologinlistener();
        void forumstoforumlistener(DataServices.Forum forum);
    }
}