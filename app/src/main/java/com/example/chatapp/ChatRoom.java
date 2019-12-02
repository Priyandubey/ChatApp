package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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


        recyclerView = findViewById(R.id.messagesRecycler);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        userRoomRecylerAdapter = new UserRoomRecylerAdapter(arrayList,this);
        recyclerView.setAdapter(userRoomRecylerAdapter);

        FirebaseDatabase.getInstance().getReference().child("my_users").child(FirebaseAuth.getInstance().getUid()).child(uuid)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        String m = dataSnapshot.child("message").getValue().toString();
                        String ros = dataSnapshot.child("sendOrReceived").getValue().toString();

                        arrayList.add(new ChatInfo(m,ros));
                        userRoomRecylerAdapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(arrayList.size() - 1);

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sendEditText.getText().toString().equals("")){
                    Toast.makeText(ChatRoom.this, "Please type some message", Toast.LENGTH_SHORT).show();
                }else {
                    String text = sendEditText.getText().toString();
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("message",text);
                    hm.put("sendOrReceived","sent");

                    FirebaseDatabase.getInstance().getReference().child("my_users").child(FirebaseAuth.getInstance().getUid()).child(uuid).push()
                            .setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            Toast.makeText(ChatRoom.this, "message sent", Toast.LENGTH_SHORT).show();
                            else Toast.makeText(ChatRoom.this, "message fail", Toast.LENGTH_SHORT).show();
                        }
                    });

                    HashMap<String, String> h = new HashMap<>();
                    h.put("message",text);
                    h.put("sendOrReceived","received");

                    FirebaseDatabase.getInstance().getReference().child("my_users").child(uuid).child(FirebaseAuth.getInstance().getUid()).push()
                            .setValue(h).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            Toast.makeText(ChatRoom.this, "message received by human", Toast.LENGTH_SHORT).show();
                            else Toast.makeText(ChatRoom.this, "message receive fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


    }
    ///////////////// stack overflow input closer ///////////////
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    /////////////////-----------------------------////////////////
}
