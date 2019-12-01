package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
    public void onBindViewHolder(@NonNull MyChatsHolder holder, final int position) {
        holder.chatsName.setText(userList.get(position).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatRoom.class);
                intent.putExtra("chatUserUuid",userList.get(position).getUuid());
                intent.putExtra("chatUserName",userList.get(position).getUsername());
                context.startActivity(intent);
            }
        });
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
