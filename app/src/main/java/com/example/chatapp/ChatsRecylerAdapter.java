package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatsRecylerAdapter extends RecyclerView.Adapter<ChatsRecylerAdapter.MyChatsHolder> {

    Context context;
    ArrayList<SendMessageUser> userList;

    public ChatsRecylerAdapter(Context context, ArrayList<SendMessageUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyChatsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_recycler,parent,false);
        MyChatsHolder myChatsHolder = new MyChatsHolder(view);
        return  myChatsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyChatsHolder holder, int position) {
        holder.chatsName.setText(userList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class MyChatsHolder extends RecyclerView.ViewHolder{
        TextView chatsName;
        ImageView chatsImage;

        public MyChatsHolder(@NonNull View itemView) {
            super(itemView);

            chatsImage = itemView.findViewById(R.id.chatsImage);
            chatsName = itemView.findViewById(R.id.chatsName);

        }
    }

}
