//Assignment InClass07
//File name ContactsFragment
// Dayakar Ravuri and Anoosh Hari Group 29 A



package com.todo.contacts;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ContactsFragment extends Fragment implements CustomRecyclerView.IUpdateListListener {

    private final OkHttpClient client = new OkHttpClient();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CustomRecyclerView adapter;
    IContactsFragmentListener listener;
    List<Contact> contacts = new ArrayList<>();
    Button create;

    public ContactsFragment() {

    }

    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        create = view.findViewById(R.id.createContact);

        recyclerView = view.findViewById(R.id.contactsRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        getActivity().setTitle(getString(R.string.contacts));

        contacts.clear();
        adapter = new CustomRecyclerView(contacts, listener, ContactsFragment.this);
        recyclerView.setAdapter(adapter);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchCreateContact();
            }
        });

        updateRecyclerView();

        return view;
    }

    private void updateRecyclerView() {
        Request request = new Request.Builder()
                .url(getActivity().getResources().getString(R.string.CONTACTS_ALL))
                .build();

        Log.d("demo", "onCreateView: " + request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("demo", "onFailure: " + e.getMessage());
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String apiResponse = response.body().string();
                    String[] conts = apiResponse.split("\n");
                    contacts.clear();
                    if (conts.length >= 1 && conts[0].split(",").length > 1) {
                        contacts.addAll(Arrays.asList(conts).stream().map(e -> new Contact(e)).collect(Collectors.toList()));
                        Log.d("demo", "onResponse: " + contacts);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_contacts), Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.api_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IContactsFragmentListener) {
            this.listener = (IContactsFragmentListener) context;
        }
    }

    @Override
    public void updateListViewListener() {
        adapter.notifyDataSetChanged();
        updateRecyclerView();
    }

    public interface IContactsFragmentListener {
        void launchContactDetails(List<Contact> contact, int position);

        void launchCreateContact();
    }
}