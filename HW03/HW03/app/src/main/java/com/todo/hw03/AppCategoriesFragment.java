package com.todo.hw03;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
Assignment #: HW03
FileName: AppCategoriesFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class AppCategoriesFragment extends Fragment {

    private static final String LOGIN_TOKEN = "LOGIN_TOKEN";
    private static final String ACCOUNT_TOKEN = "ACCOUNT_TOKEN";

    private String loginToken;
    private DataServices.Account account;
    private String[] categories;

    TextView welcomeText;

    Button logout;

    RecyclerView recyclerView;
    AppCategoriesRecyclerViewAdapter adapter;
    LinearLayoutManager layoutManager;

    IAppCategoriesHandler listener;

    public AppCategoriesFragment() {

    }

    public static AppCategoriesFragment newInstance(String loginToken, DataServices.Account account) {
        AppCategoriesFragment fragment = new AppCategoriesFragment();
        Bundle args = new Bundle();
        args.putString(LOGIN_TOKEN, loginToken);
        args.putSerializable(ACCOUNT_TOKEN, account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loginToken = getArguments().getString(LOGIN_TOKEN);
            account = (DataServices.Account) getArguments().getSerializable(ACCOUNT_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_categories, container, false);

        getActivity().setTitle(getString(R.string.app_categories_title));

        welcomeText = view.findViewById(R.id.appCategoriesGreetings);
        logout = view.findViewById(R.id.appCategoriesLogout);

        recyclerView = view.findViewById(R.id.appCategoriesRecyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        welcomeText.setText(getString(R.string.welcome_label) + " " + account.getName());

        DataServices.getAppCategories(loginToken, new DataServices.DataResponse<String>() {
            @Override
            public void onSuccess(ArrayList<String> data) {
                categories = data.toArray(new String[data.size()]);
                adapter = new AppCategoriesRecyclerViewAdapter(listener, categories);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(DataServices.RequestException exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (context instanceof IAppCategoriesHandler) {
            listener = (IAppCategoriesHandler) context;
        }
        super.onAttach(context);
    }

    public interface IAppCategoriesHandler {
        void onLogoutHandler();

        void onClickHandler(int position, String[] categories);
    }
}