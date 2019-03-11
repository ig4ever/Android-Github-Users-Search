package com.rakhmat.androidgithubuserssearch.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rakhmat.androidgithubuserssearch.DownloadImageTask;
import com.rakhmat.androidgithubuserssearch.Model.User;
import com.rakhmat.androidgithubuserssearch.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    List<User> userList;

    public UserAdapter(List <User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder mViewHolder = new MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTextViewUsername.setText(userList.get(position).getUsername());
        new DownloadImageTask(holder.mImageViewAvatar).execute(userList.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewUsername;
        public ImageView mImageViewAvatar;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextViewUsername = (TextView) itemView.findViewById(R.id.username);
            mImageViewAvatar = (ImageView) itemView.findViewById(R.id.avatar);
        }
    }
}
