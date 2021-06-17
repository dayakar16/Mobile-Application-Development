package com.inclass04;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

/*
Assignment #: InClass04
FileName: MainActivity
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class MainActivity extends AppCompatActivity implements RegisterFragment.IRegisterListener, UpdateAccountFragment.IUpdateAccountDetailsListener, AccountDetailsFragment.IAccountListener, LoginFragment.ILoginListener {

    public static String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
    public static String ACCOUNT_DETAILS_FRAGMENT = "ACCOUNT_DETAILS_FRAGMENT";
    public static String UPDATE_ACCOUNT_DETAILS = "UPDATE_ACCOUNT_DETAILS";
    public static String REGISTER_FRAGMENT = "REGISTER_FRAGMENT";

    DataServices.Account accountDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainContainer, new LoginFragment(), LOGIN_FRAGMENT)
                .commit();
    }

    @Override
    public void launchUpdate(DataServices.Account account) {
        this.accountDetails = account;
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(ACCOUNT_DETAILS_FRAGMENT)
                .replace(R.id.mainContainer, UpdateAccountFragment.newInstance(account), UPDATE_ACCOUNT_DETAILS)
                .commit();
    }

    @Override
    public void launchRegister() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, new RegisterFragment(), REGISTER_FRAGMENT)
                .commit();
    }

    @Override
    public void launchAccountDetails(DataServices.Account account) {
        if (account != null) {
            this.accountDetails = account;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainContainer, AccountDetailsFragment.newInstance(account), ACCOUNT_DETAILS_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void launchLogin() {
        this.accountDetails = null;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, new LoginFragment(), LOGIN_FRAGMENT)
                .commit();
    }

    @Override
    public void launchAccountDetailsFromUpdateAccountDetails(DataServices.Account account) {
        if (account != null) {
            this.accountDetails = account;
            AccountDetailsFragment accountDetailsFragment = (AccountDetailsFragment) getSupportFragmentManager().findFragmentByTag(ACCOUNT_DETAILS_FRAGMENT);
            accountDetailsFragment.updateDetails(account);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void launchAccountDetailsFromRegister(DataServices.Account account) {
        launchAccountDetails(account);
    }

    @Override
    public void launchLoginListnerFromRegister() {
        launchLogin();
    }
}