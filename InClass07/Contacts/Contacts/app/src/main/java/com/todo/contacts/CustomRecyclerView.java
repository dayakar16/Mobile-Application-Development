//Assignment InClass07
//File name CustomRecyclerView
// Dayakar Ravuri and Anoosh Hari Group 29 A


package com.todo.contacts;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.Buffer;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomRecyclerView
        extends RecyclerView.Adapter<CustomRecyclerView.CustomRecyclerViewHolder> {

    ContactsFragment.IContactsFragmentListener listener;
    List<Contact> contacts;
    Activity activity;
    IUpdateListListener updateListListener;

    CustomRecyclerView(List<Contact> contacts, ContactsFragment.IContactsFragmentListener listener, ContactsFragment contactsFragment) {
        this.contacts = contacts;
        this.listener = listener;
        this.updateListListener = (IUpdateListListener) contactsFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_layout, parent, false);
        CustomRecyclerViewHolder viewHolder = new CustomRecyclerViewHolder(view);
        activity = (Activity) parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerViewHolder holder, int position) {
        if (contacts != null && contacts.size() != 0) {
            String[] contact = contacts.get(position).toString().split(",");
            if (contact.length >= 5) {
                holder.phoneValue.setText(contact[3]);
                holder.emailValue.setText(contact[2]);
                holder.nameValue.setText(contact[1]);
                holder.idValue.setText(contact[0]);
                holder.position = position;
                holder.id = contact[0];
                holder.box.setBackground(activity.getResources().getDrawable(R.drawable.border));
                holder.phoneType.setText(contact[4]);
            }
        }
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    interface IUpdateListListener {
        void updateListViewListener();
    }

    public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameValue;
        TextView emailValue;
        TextView phoneValue;
        TextView idValue;
        TextView phoneType;
        Button delete;
        String id;
        ConstraintLayout box;
        int position;

        public CustomRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameValue = itemView.findViewById(R.id.contactNameValue);
            emailValue = itemView.findViewById(R.id.contactEmailValue);
            phoneValue = itemView.findViewById(R.id.contactMobileValue);
            delete = itemView.findViewById(R.id.contactButtonDelete);
            idValue = itemView.findViewById(R.id.contactIdValue);
            phoneType = itemView.findViewById(R.id.contactPhoneTypeValue);
            box = itemView.findViewById(R.id.boxContainer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.launchContactDetails(contacts, position);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FormBody post= new FormBody.Builder().add("id",id).build();
                    Request request = new Request.Builder()
                            .url(itemView.getResources().getString(R.string.CONTACTS_DELETE))
                            .post(post).build();
                    new OkHttpClient()
                            .newCall(request)
                            .enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        String responseData = response.body().string();
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(activity, responseData, Toast.LENGTH_SHORT).show();
                                                updateListListener.updateListViewListener();
                                            }
                                        });
                                    }
                                }
                            });
                }
            });
        }
    }
}
