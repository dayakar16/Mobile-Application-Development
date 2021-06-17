package com.todo.hw03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
Assignment #: HW03
FileName: AppCategoriesRecyclerViewAdapter
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class AppCategoriesRecyclerViewAdapter
        extends RecyclerView.Adapter<AppCategoriesRecyclerViewAdapter.AppCategoriesViewHolder> {

    private AppCategoriesFragment.IAppCategoriesHandler appCategoriesListener;

    private String[] categories;

    public AppCategoriesRecyclerViewAdapter(AppCategoriesFragment.IAppCategoriesHandler appCategoriesListener, String[] categories) {
        this.categories = categories;
        this.appCategoriesListener = appCategoriesListener;
    }

    @NonNull
    @Override
    public AppCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        view.setBackground(parent.getContext().getResources().getDrawable(R.drawable.border));
        AppCategoriesViewHolder appViewHolder = new AppCategoriesViewHolder(view);
        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppCategoriesViewHolder holder, int position) {
        holder.position = position;
        holder.appCategoriesListener = appCategoriesListener;
        holder.categories = categories;
        holder.textView.setText(categories[position]);
    }

    @Override
    public int getItemCount() {
        return this.categories.length;
    }

    public static class AppCategoriesViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private AppCategoriesFragment.IAppCategoriesHandler appCategoriesListener;
        private String[] categories;
        private TextView textView;

        public AppCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appCategoriesListener.onClickHandler(position, categories);
                }
            });
        }
    }
}
