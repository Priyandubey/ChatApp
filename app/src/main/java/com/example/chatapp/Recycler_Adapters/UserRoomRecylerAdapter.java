package com.example.chatapp.Recycler_Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Class_Structures.ChatInfo;
import com.example.chatapp.R;

import java.util.ArrayList;

public class UserRoomRecylerAdapter extends RecyclerView.Adapter<UserRoomRecylerAdapter.ChatRoomViewHolder> {

    ArrayList<ChatInfo> arrayList;
    Context context;

    public UserRoomRecylerAdapter(ArrayList<ChatInfo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_room_recycler,parent,false);
        ChatRoomViewHolder chatRoomViewHolder = new ChatRoomViewHolder(view);
        return chatRoomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
            holder.chatsRoomChats.setText(arrayList.get(position).getMessage());
            if(arrayList.get(position).getSendOrReceived().equals("sent")){
                holder.chatsRoomChats.setBackgroundColor(Color.parseColor("#e5fff2"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.chatsRoomChats.setGravity(Gravity.END);
                }
            }else{
                holder.chatsRoomChats.setBackgroundColor(Color.WHITE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.chatsRoomChats.setGravity(Gravity.START);
                }
            }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder{

        TextView chatsRoomChats;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            chatsRoomChats = itemView.findViewById(R.id.chatsRoomChats);
        }
    }

}
