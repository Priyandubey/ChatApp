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
    int count ;

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
    public void onBindViewHolder(@NonNull final ShareUserHolder holder, int position) {
        count = 0;
        holder.shareRecylerName.setText(userList.get(position).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(count < 1) {
                    count++;
                    v.setVisibility(View.VISIBLE);
                    holder.selectedCheck.setVisibility(View.VISIBLE);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class ShareUserHolder extends RecyclerView.ViewHolder{
        ImageView shareRecylerImage;
        TextView shareRecylerName;
        ImageView selectedCheck;

        public ShareUserHolder(@NonNull View itemView) {
            super(itemView);

            selectedCheck = itemView.findViewById(R.id.selectedCheck);
            shareRecylerImage = itemView.findViewById(R.id.shareRecylerImage);
            shareRecylerName = itemView.findViewById(R.id.shareRecylerName);

        }
    }

}
