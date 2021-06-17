package com.inclass04;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/*
Assignment #: InClass04
FileName: AccountDetailsFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class AccountDetailsFragment extends Fragment {

    public static String ACCOUNT_DETAILS_KEY = "ACCOUNT_DETAILS_KEY";

    Button editProfile;
    Button logout;

    TextView name;

    IAccountListener listener;

    DataServices.Account accountDetails;

    public AccountDetailsFragment() {

    }

    public void updateDetails(DataServices.Account account){
        this.accountDetails = account;
    }

    public static AccountDetailsFragment newInstance(DataServices.Account account) {
        AccountDetailsFragment fragment = new AccountDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ACCOUNT_DETAILS_KEY, account);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accountDetails = (DataServices.Account) getArguments().getSerializable(ACCOUNT_DETAILS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        getActivity().setTitle(getString(R.string.account_title));

        editProfile = view.findViewById(R.id.editProfileButton);
        logout = view.findViewById(R.id.logoutButton);

        name = view.findViewById(R.id.accountNameTextView);

        if (accountDetails != null){
            name.setText(accountDetails.getName());
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchUpdate(accountDetails);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchLogin();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IAccountListener){
            listener = (IAccountListener) context;
        }
    }

    public interface IAccountListener {
        public void launchUpdate(DataServices.Account account);
        public void launchLogin();
    }
}