package com.inclass04;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
Assignment #: InClass04
FileName: LoginFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class LoginFragment extends Fragment {

    Button loginButton;
    Button createNewAccount;

    EditText email;
    EditText password;

    DataServices.Account accountDetails;

    ILoginListener listener;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        getActivity().setTitle(getString(R.string.login_title));

        loginButton = view.findViewById(R.id.loginButton);
        createNewAccount = view.findViewById(R.id.createNewAccountButton);
        email = view.findViewById(R.id.loginEmailText);
        password = view.findViewById(R.id.loginPasswordInput);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = email.getText().toString();
                String loginPassword = password.getText().toString();
                try {
                    if (loginEmail.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_email));
                    }

                    if (loginPassword.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_password));
                    }

                    accountDetails = DataServices.login(loginEmail, loginPassword);
                    if (accountDetails != null) {
                        Toast.makeText(getActivity(), getString(R.string.success_login), Toast.LENGTH_SHORT).show();
                        listener.launchAccountDetails(accountDetails);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.failed_login), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchRegister();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ILoginListener){
            listener = (ILoginListener) context;
        }
    }

    public interface ILoginListener {
        public void launchAccountDetails(DataServices.Account account);
        public void launchRegister();
    }
}