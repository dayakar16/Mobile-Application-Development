package com.todo.hw03;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/*
    Assignment #: HW03
    FileName: LoginFragment
    Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
*/

public class LoginFragment extends Fragment {

    TextView createNewAccount;

    EditText email;
    EditText password;

    Button login;

    String loginToken;
    private ILoginHandler listener;

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

        createNewAccount = view.findViewById(R.id.loginCreateNewAccount);
        email = view.findViewById(R.id.loginEmail);
        password = view.findViewById(R.id.loginPassword);
        login = view.findViewById(R.id.loginButtonLogin);

        login.setOnClickListener(new View.OnClickListener() {
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

                    DataServices.login(loginEmail, loginPassword, new DataServices.AuthResponse() {
                        @Override
                        public void onSuccess(String token) {
                            Toast.makeText(getActivity(), getString(R.string.success_login), Toast.LENGTH_SHORT).show();
                            loginToken = token;
                            DataServices.getAccount(token, new DataServices.AccountResponse() {
                                @Override
                                public void onSuccess(DataServices.Account account) {
                                    listener.onLoginHandler(token, account);
                                }

                                @Override
                                public void onFailure(DataServices.RequestException exception) {
                                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(DataServices.RequestException exception) {
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCreateNewAccountHandler();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof ILoginHandler) {
            listener = (ILoginHandler) context;
        }
        super.onAttach(context);
    }

    public interface ILoginHandler {
        void onLoginHandler(String loginToken, DataServices.Account account);

        void onCreateNewAccountHandler();
    }
}