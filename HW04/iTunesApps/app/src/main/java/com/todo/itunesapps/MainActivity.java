package com.todo.itunesapps;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/*
Assignment #: HW04
FileName: MainActivity
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class MainActivity extends AppCompatActivity
        implements com.todo.itunesapps.AppListFragment.IAppListFragmentHandler, com.todo.itunesapps.LoginFragment.ILoginHandler,
                   com.todo.itunesapps.AppCategoriesFragment.IAppCategoriesHandler,
                   com.todo.itunesapps.RegisterFragment.IRegisterFragmentHandler {

    private String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
    private String REGISTER_FRAGMENT = "REGISTER_FRAGMENT";
    private String APP_DETAILS_FRAGMENT = "APP_DETAILS_FRAGMENT";
    private String APP_LIST_FRAGMENT = "APP_LIST_FRAGMENT";
    private String APP_CATEGORIES_FRAGMENT = "APP_CATEGORIES_FRAGMENT";

    com.todo.itunesapps.DataServices.Account account;
    String loginToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainContainer, com.todo.itunesapps.LoginFragment.newInstance(), LOGIN_FRAGMENT)
                .commit();

    }

    @Override
    public void onLoginHandler(String loginToken, com.todo.itunesapps.DataServices.Account account) {
        this.loginToken = loginToken;
        this.account = account;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, com.todo.itunesapps.AppCategoriesFragment.newInstance(loginToken, account), APP_CATEGORIES_FRAGMENT)
                .commit();
    }

    @Override
    public void onCreateNewAccountHandler() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, com.todo.itunesapps.RegisterFragment.newInstance(), REGISTER_FRAGMENT)
                .commit();
    }

    @Override
    public void onLogoutHandler() {
        account = null;
        loginToken = null;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, com.todo.itunesapps.LoginFragment.newInstance(), LOGIN_FRAGMENT)
                .commit();
    }

    @Override
    public void onClickHandler(int position, String[] categories) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, com.todo.itunesapps.AppListFragment.newInstance(loginToken, categories[position]), APP_LIST_FRAGMENT)
                .addToBackStack(APP_CATEGORIES_FRAGMENT)
                .commit();
    }

    @Override
    public void onRegistrationSubmit(String token, com.todo.itunesapps.DataServices.Account account) {
        this.loginToken = token;
        this.account = account;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, com.todo.itunesapps.AppCategoriesFragment.newInstance(token, account), APP_CATEGORIES_FRAGMENT)
                .commit();
    }

    @Override
    public void onRegistrationCancelHandler() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, com.todo.itunesapps.LoginFragment.newInstance(), LOGIN_FRAGMENT)
                .commit();
    }

    @Override
    public void onAppListClickHandler(com.todo.itunesapps.DataServices.App app) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, com.todo.itunesapps.AppDetailsFragment.newInstance(app), APP_DETAILS_FRAGMENT)
                .addToBackStack(APP_LIST_FRAGMENT)
                .commit();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}