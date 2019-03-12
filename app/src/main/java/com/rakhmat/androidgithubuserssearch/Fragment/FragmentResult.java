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
import com.rakhmat.androidgithubuserssearch.Rest.AccessToken;
import com.rakhmat.androidgithubuserssearch.Rest.ApiClient;
import com.rakhmat.androidgithubuserssearch.Rest.ApiInterface;
import com.rakhmat.androidgithubuserssearch.SimpleDividerItemDecoration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentResult extends Fragment {
    private ApiInterface mApiInterface;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private View view;
    private Context context;
    private String activity;
    private SearchView searchViewUsers;
    private String tokenType;
    private String tokenAccess;

    public FragmentResult() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_result, container, false);
        context = view.getContext();
        final String token = getArguments().getString("code");
        Log.e("token : ",token);
        searchViewUsers = getActivity().findViewById(R.id.search_github_users);

        //Set RecyclerView
        recyclerView = view.findViewById(R.id.rv_result);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        if (token != null) {
            // get access token
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://github.com/").addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();

            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<AccessToken> accessToken = apiInterface.getAccessToken("37de96e20fe5c60832c8", "7b9c286fd096c8bfd347c758a08c679dcc771514", token);
            accessToken.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    Log.e("access token : ",response.body().getAccessToken());
                    Log.e("token type : ",response.body().getTokenType());
                    tokenAccess = response.body().getAccessToken();
                    tokenType = response.body().getTokenType();

                    mApiInterface = ApiClient.createService(ApiInterface.class, tokenType + " " + tokenAccess);
                    refresh("");

                    searchViewUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {

                }
            });
        }

        return view;
    }

    public void refresh(String username) {
        Call<GetUser> userCall = mApiInterface.getUsers(username);
        userCall.enqueue(new Callback<GetUser >() {
            @Override
            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body().getListDataUser();
                    Log.e("", "List Data Total :" + userList.size());
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
