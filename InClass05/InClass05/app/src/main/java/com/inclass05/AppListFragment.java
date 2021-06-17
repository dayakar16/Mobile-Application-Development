package com.inclass05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
Assignment #: InClass05
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

    ListView listView;
    AppItemAdapter adapter;

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
        listView = view.findViewById(R.id.appListView);

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

        adapter = new AppItemAdapter(getContext(), R.layout.app_detail_list, apps);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onAppListClickHandler(apps.get(position));
            }
        });
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
        void onAppListClickHandler(DataServices.App loginToken);
    }
}