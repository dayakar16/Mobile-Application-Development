package com.inclass05;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/*
Assignment #: InClass05
FileName: AppDetailsFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class AppDetailsFragment extends Fragment {

    private static final String APP_DETAILS = "APP_DETAILS";

    private DataServices.App app;

    TextView appName;
    TextView artistName;
    TextView releaseDate;

    ListView listView;
    ArrayAdapter<String> adapter;

    String[] genres;

    public AppDetailsFragment() {

    }

    public static AppDetailsFragment newInstance(DataServices.App app) {
        AppDetailsFragment fragment = new AppDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(APP_DETAILS, app);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            app = (DataServices.App) getArguments().getSerializable(APP_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_details, container, false);

        getActivity().setTitle(getString(R.string.app_details_title));

        appName = view.findViewById(R.id.appDetailsAppName);
        artistName = view.findViewById(R.id.appDetailsArtistName);
        releaseDate = view.findViewById(R.id.appDetailsReleaseDate);

        listView = view.findViewById(R.id.appDetailsListView);

        genres = app.genres.toArray(new String[app.genres.size()]);

        appName.setText(app.name);
        artistName.setText(app.artistName);
        releaseDate.setText(app.releaseDate);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,android.R.id.text1, genres);
        listView.setAdapter(adapter);

        return view;
    }
}