package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public void onBindViewHolder(@NonNull final MyChatsHolder holder, final int position) {
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
        FirebaseDatabase.getInstance().getReference().child("my_users").child(userList.get(position).getUuid()).child("profile_pic_url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    String uri = dataSnapshot.getValue().toString();
                    Glide.with(context)
                            .load(uri)
                            .into(holder.chatsImage);
                }catch(Exception e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "unsuccessful profile loading", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent = new Intent(context,PhotoChat.class);
                intent.putExtra("chatUserUuid",userList.get(position).getUuid());
                intent.putExtra("chatUserName",userList.get(position).getUsername());
                context.startActivity(intent);

                return true;
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
