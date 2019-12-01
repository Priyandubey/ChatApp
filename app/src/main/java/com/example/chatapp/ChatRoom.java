package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoom extends AppCompatActivity {

    RecyclerView recyclerView;
    UserRoomRecylerAdapter userRoomRecylerAdapter;
    RecyclerView.LayoutManager layoutManager;

    ImageView sendButton;
    EditText sendEditText;

    ArrayList<ChatInfo> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        sendButton = findViewById(R.id.sendButton);
        sendEditText = findViewById(R.id.sendEditText);

        arrayList = new ArrayList<>();
        Intent intent = this.getIntent();
        final String uuid = intent.getStringExtra("chatUserUuid");
        String username = intent.getStringExtra("chatUserName");

        setTitle(username);

        arrayList.add(new ChatInfo("Hi","sent"));
        arrayList.add(new ChatInfo("Hi","received"));
        arrayList.add(new ChatInfo("How r u doin ?","sent"));
        arrayList.add(new ChatInfo("I am doing good. wbu ?","received"));
        arrayList.add(new ChatInfo("I am doin good as well.","sent"));
        arrayList.add(new ChatInfo(":)","received"));
        arrayList.add(new ChatInfo("Hi","sent"));
        arrayList.add(new ChatInfo("Hi","received"));
        arrayList.add(new ChatInfo("How r u doin ?","sent"));
        arrayList.add(new ChatInfo("I am doing good. wbu ?","received"));
        arrayList.add(new ChatInfo("I am doin good as well.","sent"));
        arrayList.add(new ChatInfo("I am doing good. wbu ?","received"));
        arrayList.add(new ChatInfo("I am doin good as well.","sent"));
        arrayList.add(new ChatInfo("I am doing good. wbu ?","received"));
        arrayList.add(new ChatInfo("I am doin good as well.","sent"));
        arrayList.add(new ChatInfo("I am doing good. wbu ?","received"));
        arrayList.add(new ChatInfo("I am doin good as well.","sent"));
        arrayList.add(new ChatInfo("I am doin good as well.","sent"));
        arrayList.add(new ChatInfo("I am doin good as well.","sent"));
        arrayList.add(new ChatInfo("I am doin good as well.","sent"));
        arrayList.add(new ChatInfo("I am doin good as well.","sent"));


        recyclerView = findViewById(R.id.messagesRecycler);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        userRoomRecylerAdapter = new UserRoomRecylerAdapter(arrayList,this);
        recyclerView.setAdapter(userRoomRecylerAdapter);
        recyclerView.smoothScrollToPosition(arrayList.size() - 1);

        sendEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sendEditText.getText().toString().equals("")){
                    Toast.makeText(ChatRoom.this, "Please type some message", Toast.LENGTH_SHORT).show();
                }else {
                    String text = sendEditText.getText().toString();
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("message",text);
                    hm.put("sendOrReceived",uuid);

                    FirebaseDatabase.getInstance().getReference().child("my_users").child(FirebaseAuth.getInstance().getUid()).child(uuid).push().setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }

            }
        });


    }
}
