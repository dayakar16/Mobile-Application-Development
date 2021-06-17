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
import android.widget.TextView;
import android.widget.Toast;

/*
Assignment #: InClass04
FileName: UpdateAccountFragment
Full Name of Students: Anoosh Hari, Dayakar Ravuri Group 29
 */

public class UpdateAccountFragment extends Fragment {

    public static String KEY = "KEY";
    DataServices.Account accountDetails;

    TextView email;

    EditText name;
    EditText password;

    Button submit;
    Button cancel;

    IUpdateAccountDetailsListener listener;

    public UpdateAccountFragment() {

    }

    public static UpdateAccountFragment newInstance(DataServices.Account account) {
        UpdateAccountFragment fragment = new UpdateAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, account);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            this.accountDetails = (DataServices.Account) getArguments().getSerializable(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_account, container, false);

        getActivity().setTitle(getString(R.string.update_title));

        email = view.findViewById(R.id.updateEmailView);
        name = view.findViewById(R.id.updateNameText);
        password = view.findViewById(R.id.updatePasswordText);

        submit = view.findViewById(R.id.updateSubmitButton);
        cancel = view.findViewById(R.id.updateCancelButton);

        if (accountDetails != null) {
            email.setText(accountDetails.getEmail());
            name.setText(accountDetails.getName());
            password.setText(accountDetails.getPassword());
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginName = name.getText().toString();
                String loginPassword = password.getText().toString();
                try {
                    if (loginName.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_name));
                    }

                    if (loginPassword.isEmpty()) {
                        throw new Exception(getString(R.string.invalid_password));
                    }

                    accountDetails = DataServices.update(accountDetails, loginName, loginPassword);
                    if (accountDetails != null) {
                        Toast.makeText(getActivity(), getString(R.string.success_update), Toast.LENGTH_SHORT).show();
                        listener.launchAccountDetailsFromUpdateAccountDetails(accountDetails);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.failure_update), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchAccountDetailsFromUpdateAccountDetails(null);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IUpdateAccountDetailsListener) {
            listener = (IUpdateAccountDetailsListener) context;
        }
    }

    public interface IUpdateAccountDetailsListener {
        public void launchAccountDetailsFromUpdateAccountDetails(DataServices.Account account);
    }
}