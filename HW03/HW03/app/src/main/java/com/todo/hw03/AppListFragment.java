package com.todo.hw03;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
Assignment #: HW03
FileName: AppListFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class AppListFragment extends Fragment {

    private static final String LOGIN_TOKEN = "LOGIN_TOKEN";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";

    private String loginToken;
    private String categoryName;

    TextView appName;
    TextView artistName;
    TextView releaseDate;

    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    AppListRecyclerViewAdapter adapter;

    ArrayList<DataServices.App> apps;

    IAppListFragmentHandler listener;

    public AppListFragment() {

    }

    public static AppListFragment newInstance(String loginToken, String categoryName) {
        AppListFragment fragment = new AppListFragment();
        Bundle args = new Bundle();
        args.putString(LOGIN_TOKEN, loginToken);
        args.putString(CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loginToken = getArguments().getString(LOGIN_TOKEN);
            categoryName = getArguments().getString(CATEGORY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);

        getActivity().setTitle(categoryName);

        appName = view.findViewById(R.id.appDetailsAppName);
        artistName = view.findViewById(R.id.appDetailsArtistName);
        releaseDate = view.findViewById(R.id.appDetailsReleaseDate);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.appListView);
        recyclerView.setLayoutManager(linearLayoutManager);

        DataServices.getAppsByCategory(loginToken, categoryName, new DataServices.DataResponse<DataServices.App>() {
            @Override
            public void onSuccess(ArrayList<DataServices.App> data) {
                apps = data;
            }

            @Override
            public void onFailure(DataServices.RequestException exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new AppListRecyclerViewAdapter(apps, listener);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof IAppListFragmentHandler){
            listener = (IAppListFragmentHandler) context;
        }
        super.onAttach(context);
    }

    public interface IAppListFragmentHandler{
        void onAppListClickHandler(DataServices.App app);
    }
}