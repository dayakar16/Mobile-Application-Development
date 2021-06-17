package com.todo.itunesapps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
    Assignment #: HW04
    FileName: AppDetailsRecyclerViewAdapter
    Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
*/

public class AppDetailsRecyclerViewAdapter
        extends RecyclerView.Adapter<AppDetailsRecyclerViewAdapter.AppDetailsViewHolder> {

    private String[] genres;

    public AppDetailsRecyclerViewAdapter(String[] genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public AppDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
        view.setBackground(parent.getContext().getResources().getDrawable(R.drawable.border));
        AppDetailsViewHolder appViewHolder = new AppDetailsViewHolder(view);
        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppDetailsViewHolder holder, int position) {
        holder.textView.setText(genres[position]);
    }

    @Override
    public int getItemCount() {
        return this.genres != null ? this.genres.length : 0;
    }

    public static class AppDetailsViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public AppDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
