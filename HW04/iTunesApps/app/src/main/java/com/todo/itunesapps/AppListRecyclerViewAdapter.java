package com.todo.itunesapps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
    Assignment #: HW04
    FileName: AppListRecyclerViewAdapter
    Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
*/

public class AppListRecyclerViewAdapter
        extends RecyclerView.Adapter<AppListRecyclerViewAdapter.AppListViewHolder> {

    private ArrayList<com.todo.itunesapps.DataServices.App> apps;

    private AppListFragment.IAppListFragmentHandler appListListener;

    public AppListRecyclerViewAdapter(ArrayList<com.todo.itunesapps.DataServices.App> apps, AppListFragment.IAppListFragmentHandler appListListener) {
        this.apps = apps;
        this.appListListener = appListListener;
    }

    @NonNull
    @Override
    public AppListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_detail_list, parent, false);
        view.setBackground(parent.getContext().getResources().getDrawable(R.drawable.border));
        AppListViewHolder appViewHolder = new AppListViewHolder(view);
        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppListViewHolder holder, int position) {
        com.todo.itunesapps.DataServices.App app = apps.get(position);
        holder.appName.setText(app.name);
        holder.artistName.setText(app.artistName);
        holder.releaseDate.setText(app.releaseDate);
        holder.app = app;
        holder.appListListener = appListListener;
    }

    @Override
    public int getItemCount() {
        return this.apps != null ? this.apps.size() : 0;
    }

    public static class AppListViewHolder extends RecyclerView.ViewHolder {
        private TextView appName;
        private TextView artistName;
        private TextView releaseDate;
        private com.todo.itunesapps.DataServices.App app;
        private AppListFragment.IAppListFragmentHandler appListListener;


        public AppListViewHolder(@NonNull View itemView) {
            super(itemView);

            appName = itemView.findViewById(R.id.itemAppName);
            artistName = itemView.findViewById(R.id.itemArtistName);
            releaseDate = itemView.findViewById(R.id.itemReleaseDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appListListener.onAppListClickHandler(app);
                }
            });
        }
    }
}
