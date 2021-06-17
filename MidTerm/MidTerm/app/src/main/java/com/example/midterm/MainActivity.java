package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoginFragment.Iloginlistener, CreateNewAccountFragment.Iregisterlistener, ForumsFragment.Iforumlistener , NewForumFragment.InewForumlistener {

    DataServices.AuthResponse authResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainActivity,new LoginFragment(),"LoginFragment")
                     .commit();
    }

    @Override
    public void logintoForumsFragment(DataServices.AuthResponse authResponse) {
        this.authResponse = authResponse;
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.mainActivity,ForumsFragment.newInstance(authResponse),"ForumsFragment")
                                   .commit();
    }

    @Override
    public void logintoCreateNewAccountFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity,new CreateNewAccountFragment(),"CreateNewAccountFragment")
                .commit();
    }


    @Override
    public void registertologin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity,new LoginFragment(),"LoginFragment")
                .commit();
    }

    @Override
    public void registertoforums(DataServices.AuthResponse authResponse) {
        this.authResponse = authResponse;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity,new ForumsFragment(),"ForumsFragment")
                .commit();

    }

    @Override
    public void forumtonewforumlistener() {
        getSupportFragmentManager().beginTransaction()
                 .addToBackStack(null)
                .replace(R.id.mainActivity,NewForumFragment.newInstance(authResponse),"NewForumFragment")
                .commit();
    }

    @Override
    public void forumtologinlistener() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity,new LoginFragment(),"LoginFragment")
                .commit();
    }

    @Override
    public void forumstoforumlistener(DataServices.Forum forum) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.mainActivity,ForumFragment.newInstance(authResponse,forum),"NewForumFragment")
                .commit();
    }


    @Override
    public void popnewtoformus() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void popnewtoformuswithnew(ArrayList<DataServices.Forum> forums) {
            getSupportFragmentManager().popBackStack();
            ForumsFragment fragment = (ForumsFragment) getSupportFragmentManager().findFragmentByTag("ForumsFragment");
            fragment.getList(forums);
    }


}