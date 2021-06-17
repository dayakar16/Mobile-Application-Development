package com.inclass05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
Assignment #: InClass05
FileName: AppCategoriesFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class AppCategoriesFragment extends Fragment {

    private static final String LOGIN_TOKEN = "LOGIN_TOKEN";

    private String loginToken;
    private String[] categories;

    TextView welcomeText;

    Button logout;

    ListView listView;
    ArrayAdapter<String> adapter;

    IAppCatogeriesHandler listener;

    public AppCategoriesFragment() {

    }

    public static AppCategoriesFragment newInstance(String loginToken) {
        AppCategoriesFragment fragment = new AppCategoriesFragment();
        Bundle args = new Bundle();
        args.putString(LOGIN_TOKEN, loginToken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loginToken = getArguments().getString(LOGIN_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_categories, container, false);

        getActivity().setTitle(getString(R.string.app_categories_title));

        welcomeText = view.findViewById(R.id.appCategoriesGreetings);
        logout = view.findViewById(R.id.appCategoriesLogout);

        listView = view.findViewById(R.id.appCategoriesListview);

        DataServices.getAccount(loginToken, new DataServices.AccountResponse() {
            @Override
            public void onSuccess(DataServices.Account account) {
                welcomeText.setText(getString(R.string.welcome_label)+" "+account.getName());
            }

            @Override
            public void onFailure(DataServices.RequestException exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DataServices.getAppCategories(loginToken, new DataServices.DataResponse<String>() {
            @Override
            public void onSuccess(ArrayList<String> data) {
                categories = data.toArray(new String[data.size()]);
                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, categories);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(DataServices.RequestException exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onClickHandler(position, categories);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(R.string.success_logout), Toast.LENGTH_SHORT).show();
                listener.onLogoutHandler();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if(context instanceof IAppCatogeriesHandler){
            listener = (IAppCatogeriesHandler) context;
        }
        super.onAttach(context);
    }

    public interface IAppCatogeriesHandler{
        void onLogoutHandler();
        void onClickHandler(int position, String[] categories);
    }
}