package com.inclass05;

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
Assignment #: InClass05
FileName: RegisterFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class RegisterFragment extends Fragment {

    TextView cancel;
    Button submitButton;

    TextView name;
    TextView email;
    TextView password;

    private IRegisterFragmentHandler listener;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

        submitButton = view.findViewById(R.id.registerSubmit);
        cancel = view.findViewById(R.id.registerCancel);

        name = view.findViewById(R.id.registerName);
        email = view.findViewById(R.id.registerEmail);
        password = view.findViewById(R.id.registerPassword);

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

                    DataServices.register(registerName, registerEmail, registerPassword, new DataServices.AuthResponse() {
                        @Override
                        public void onSuccess(String token) {
                            Toast.makeText(getActivity(), getString(R.string.success_register), Toast.LENGTH_SHORT).show();
                            listener.onRegistrationSubmit(token);
                        }

                        @Override
                        public void onFailure(DataServices.RequestException exception) {
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.cancelled_register), Toast.LENGTH_SHORT).show();
                listener.onRegistrationCancelHandler();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof IRegisterFragmentHandler) {
            listener = (IRegisterFragmentHandler) context;
        }
        super.onAttach(context);
    }

    public interface IRegisterFragmentHandler {
        void onRegistrationSubmit(String token);

        void onRegistrationCancelHandler();
    }
}