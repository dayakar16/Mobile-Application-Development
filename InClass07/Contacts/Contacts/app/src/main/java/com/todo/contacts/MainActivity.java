//Assignment InClass07
//File name MainActivity
// Dayakar Ravuri and Anoosh Hari Group 29 A

package com.todo.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
                                                    ContactsFragment.IContactsFragmentListener,
                                                    ContactDetailsFragment.IContactDetailsListener,
                                                    CreateContactFragment.ICreateContactListener,
                                                    EditContactFragment.IEditContactListener {

    public static final String ALL_CONTACTS = "ALL_CONTACTS";
    public static final String CONTACT_DETAILS = "CONTACT_DETAILS";
    public static final String EDIT_CONTACT_DETAILS = "EDIT_CONTACT_DETAILS";
    public static final String CREATE_CONTACT = "CREATE_CONTACT";

    List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainContainer, new ContactsFragment(), ALL_CONTACTS)
                .commit();
    }

    @Override
    public void launchContactDetails(List<Contact> contacts, int position) {
        this.contacts = contacts;
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(ALL_CONTACTS)
                .replace(R.id.mainContainer, new ContactDetailsFragment(contacts.get(position).toString()), CONTACT_DETAILS)
                .commit();
    }

    @Override
    public void launchCreateContact() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(ALL_CONTACTS)
                .replace(R.id.mainContainer, new CreateContactFragment(), CREATE_CONTACT)
                .commit();
    }

    @Override
    public void launchEditContactDetails(Contact user) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(CONTACT_DETAILS)
                .replace(R.id.mainContainer, new EditContactFragment(user), EDIT_CONTACT_DETAILS)
                .commit();
    }

    @Override
    public void popFromBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void popBackStackListener() {
        getSupportFragmentManager().popBackStack();
    }
}