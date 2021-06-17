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
import android.widget.Toast;

/*
Assignment #: InClass04
FileName: RegisterFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class RegisterFragment extends Fragment {

    Button cancelButton;
    Button submitButton;

    TextView name;
    TextView email;
    TextView password;

    DataServices.Account accountDetails;

    IRegisterListener listener;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        getActivity().setTitle(getString(R.string.register_title));

        submitButton = view.findViewById(R.id.registerSubmitButton);
        cancelButton = view.findViewById(R.id.registerCancelButton);

        name = view.findViewById(R.id.registerNameLabel);
        email = view.findViewById(R.id.registerEmailLabel);
        password = view.findViewById(R.id.registerPasswordLabel);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerName = name.getText().toString();
                String registerEmail = email.getText().toString();
                String registerPassword = password.getText().toString();
                try {
                    if (registerName.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_name));
                    }
                    if (registerEmail.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_email));
                    }

                    if (registerPassword.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_password));
                    }

                    accountDetails = DataServices.register(registerName, registerEmail, registerPassword);
                    if (accountDetails != null) {
                        Toast.makeText(getActivity(), getString(R.string.success_register), Toast.LENGTH_SHORT).show();
                        listener.launchAccountDetailsFromRegister(accountDetails);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.failed_register), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchLoginListnerFromRegister();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IRegisterListener){
            listener = (IRegisterListener) context;
        }
    }

    public interface IRegisterListener {
        public void launchAccountDetailsFromRegister(DataServices.Account account);
        public void launchLoginListnerFromRegister();
    }
}