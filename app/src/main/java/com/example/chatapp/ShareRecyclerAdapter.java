package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShareRecyclerAdapter extends RecyclerView.Adapter<ShareRecyclerAdapter.ShareUserHolder> {

    Context context;
    ArrayList<SendMessageUser> userList;

    public ShareRecyclerAdapter(Context context, ArrayList<SendMessageUser> userList) {
        this.context = context;
        this.userList = userList;

    }

    @NonNull
    @Override
    public ShareUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_recycler,parent,false);
        ShareUserHolder shareUserHolder = new ShareUserHolder(view);
        return  shareUserHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShareUserHolder holder, int position) {
        holder.shareRecylerName.setText(userList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class ShareUserHolder extends RecyclerView.ViewHolder{
        ImageView shareRecylerImage;
        TextView shareRecylerName;

        public ShareUserHolder(@NonNull View itemView) {
            super(itemView);

            shareRecylerImage = itemView.findViewById(R.id.shareRecylerImage);
            shareRecylerName = itemView.findViewById(R.id.shareRecylerName);

        }
    }

}
