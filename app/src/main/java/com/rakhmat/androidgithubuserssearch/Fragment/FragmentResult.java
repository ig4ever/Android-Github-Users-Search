package com.rakhmat.androidgithubuserssearch.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rakhmat.androidgithubuserssearch.Activity.MainActivity;
import com.rakhmat.androidgithubuserssearch.Adapter.UserAdapter;
import com.rakhmat.androidgithubuserssearch.Model.GetUser;
import com.rakhmat.androidgithubuserssearch.Model.User;
import com.rakhmat.androidgithubuserssearch.R;
import com.rakhmat.androidgithubuserssearch.Rest.ApiClient;
import com.rakhmat.androidgithubuserssearch.Rest.ApiInterface;
import com.rakhmat.androidgithubuserssearch.SimpleDividerItemDecoration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentResult extends Fragment {
    private ApiInterface mApiInterface;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private View view;
    private Context context;
    public static Activity activity;

    public FragmentResult() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_result, container, false);
        context = view.getContext();
        activity = getActivity();

        //Set RecyclerView
        recyclerView = view.findViewById(R.id.rv_result);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mApiInterface = ApiClient.createService(ApiInterface.class, "79fa396bdeabd826b4cb8e54ca7fb9f68e27ab96");
        refresh("");

        MainActivity.searchViewUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                refresh(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                refresh(s);
                return false;
            }
        });

        return view;
    }

    public void refresh(String username) {
        Call<GetUser> userCall = mApiInterface.getUsers(username);
        userCall.enqueue(new Callback<GetUser >() {
            @Override
            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body().getListDataUser();
                    mAdapter = new UserAdapter(userList);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    // error response
                }
            }

            @Override
            public void onFailure(Call<GetUser> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });
    }
}
