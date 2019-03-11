package com.rakhmat.androidgithubuserssearch.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.rakhmat.androidgithubuserssearch.Fragment.FragmentResult;
import com.rakhmat.androidgithubuserssearch.R;

public class MainActivity extends AppCompatActivity {
    public static SearchView searchViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new FragmentResult());

        searchViewUsers = findViewById(R.id.search_github_users);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return false;
    }
}
