package com.ahari.forumapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/*
    HW06
    MainActivity
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class MainActivity extends AppCompatActivity implements LoginFragment.ILoginListener,
                                                               CreateNewAccountFragment.ICreateNewAccountListener,
                                                               ForumsFragment.IForumsFragmentListener,
                                                               NewForumFragment.INewForumFragmentListener {
    private String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
    private String FORUMS_FRAGMENT = "FORUMS_FRAGMENT";
    private String CREATE_NEW_ACCOUNT_FRAGMENT = "CREATE_NEW_ACCOUNT_FRAGMENT";
    private String NEW_FORUM_FRAGMENT = "NEW_FORUM_FRAGMENT";
    private String FORUM_FRAGMENT = "FORUM_FRAGMENT";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mAuth.getCurrentUser() != null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForumsFragment(), FORUMS_FRAGMENT)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment(), LOGIN_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void launchForumsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ForumsFragment.newInstance(), FORUMS_FRAGMENT)
                .commit();
    }

    @Override
    public void launchLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance(), LOGIN_FRAGMENT)
                .commit();
    }

    @Override
    public void launchNewForumFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, NewForumFragment.newInstance(), NEW_FORUM_FRAGMENT)
                .addToBackStack(FORUMS_FRAGMENT)
                .commit();
    }

    @Override
    public void launchForumFragment(Forum forum) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ForumFragment.newInstance(forum), FORUM_FRAGMENT)
                .addToBackStack(NEW_FORUM_FRAGMENT)
                .commit();
    }

    @Override
    public void launchCreateNewAccountFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, CreateNewAccountFragment.newInstance(), CREATE_NEW_ACCOUNT_FRAGMENT)
                .commit();
    }

    @Override
    public void launchForumFragmentBack() {
        getSupportFragmentManager().popBackStack();
    }
}