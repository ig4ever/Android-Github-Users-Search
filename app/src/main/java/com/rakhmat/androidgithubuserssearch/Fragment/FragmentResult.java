package com.rakhmat.androidgithubuserssearch.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private View view;
    private Context context;
    private Activity activity;

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

        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        refresh();

        return view;
    }

    public void refresh() {
        Call<GetUser> userCall = mApiInterface.getUser("pikachu");
        userCall.enqueue(new Callback<GetUser>() {
            @Override
            public void onResponse(Call<GetUser> call, Response<GetUser>
                    response) {
                List<User> KontakList = response.body().getListDataUser();
                Log.d("Retrofit Get", "Jumlah data Kontak: " +
                        String.valueOf(KontakList.size()));
                mAdapter = new UserAdapter(KontakList);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetUser> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }
}
