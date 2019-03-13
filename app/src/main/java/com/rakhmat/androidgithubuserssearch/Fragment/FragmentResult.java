package com.rakhmat.androidgithubuserssearch.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.rakhmat.androidgithubuserssearch.Adapter.UserAdapter;
import com.rakhmat.androidgithubuserssearch.Model.GetUser;
import com.rakhmat.androidgithubuserssearch.Model.User;
import com.rakhmat.androidgithubuserssearch.R;
import com.rakhmat.androidgithubuserssearch.Model.AccessToken;
import com.rakhmat.androidgithubuserssearch.Rest.ApiClient;
import com.rakhmat.androidgithubuserssearch.Rest.ApiInterface;
import com.rakhmat.androidgithubuserssearch.SimpleDividerItemDecoration;

import java.util.ArrayList;
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
    private SearchView searchViewUsers;
    private String tokenType;
    private String tokenAccess;
    private String token;
    private String clientId;
    private String clientSecret;
    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling;
    private int currentItems;
    private int totalItems;
    private int scrollOutItems;
    private ProgressBar progressBar;
    private List<User> userList;
    private String keywords;
    private int indexPage;
    private int perPage;

    public FragmentResult() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_result, container, false);
        context = view.getContext();
        token = getArguments().getString("code");
        clientId = getArguments().getString("client_id");
        clientSecret = getArguments().getString("client_secret");
        indexPage = 2;
        perPage = 50; //show 50 result per page
        linearLayoutManager = new LinearLayoutManager(context);
        userList = new ArrayList<>();

        //Log.e("token : ",token);
        searchViewUsers = getActivity().findViewById(R.id.search_github_users);
        progressBar = view.findViewById(R.id.progress_bar);

        //Set RecyclerView
        recyclerView = view.findViewById(R.id.rv_result);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (token != null) {
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://github.com/").addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();

            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<AccessToken> accessToken = apiInterface.getAccessToken(clientId, clientSecret, token);
            accessToken.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    //Log.e("access token : ",response.body().getAccessToken());
                    //Log.e("token type : ",response.body().getTokenType());
                    tokenAccess = response.body().getAccessToken();
                    tokenType = response.body().getTokenType();

                    mApiInterface = ApiClient.createService(ApiInterface.class, tokenType + " " + tokenAccess);

                    searchViewUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            keywords = s;
                            refresh(keywords);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            keywords = s;
                            refresh(keywords);
                            return false;
                        }
                    });
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {

                }
            });
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling = false;
                    fetchData();
                }
            }
        });

        return view;
    }

    public void refresh(final String username) {
        Call<GetUser> userCall = mApiInterface.getUsers(username, perPage + "", "1");
        userCall.enqueue(new Callback<GetUser >() {
            @Override
            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                if (response.isSuccessful()) {
                    if(!response.body().getListDataUser().isEmpty()) {
                        userList = response.body().getListDataUser();
                    }
                    //Log.e("", "List Data Total :" + userList.size());
                    mAdapter = new UserAdapter(userList);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    userList.clear();
                }
            }

            @Override
            public void onFailure(Call<GetUser> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void refresh(final String username, int page) {
        final Call<GetUser> userCall = mApiInterface.getUsers(username, perPage + "", page + "");
        userCall.enqueue(new Callback<GetUser >() {
            @Override
            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                if (response.isSuccessful()) {
                    if(!response.body().getListDataUser().isEmpty()){
                        userList.addAll(response.body().getListDataUser());
                    }
                    mAdapter.notifyDataSetChanged();
                    //Log.e("", "List Data Total :" + userList.size());
                } else {
                    userList.clear();
                }
            }

            @Override
            public void onFailure(Call<GetUser> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh(keywords, indexPage++);
                progressBar.setVisibility(View.GONE);
            }
        }, 5000);
    }
}
