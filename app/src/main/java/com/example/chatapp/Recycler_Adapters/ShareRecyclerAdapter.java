package com.example.chatapp.Recycler_Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.Class_Structures.SendMessageUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static com.example.chatapp.Ui_Fragments.Share.bitmap;
import static com.example.chatapp.Ui_Fragments.Share.shareCaption;
import static com.example.chatapp.Ui_Fragments.Share.shareImage;

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
    public void onBindViewHolder(@NonNull final ShareUserHolder holder, final int position) {
        count = 0;
        holder.shareRecylerName.setText(userList.get(position).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(count < 1) {
                    count++;
                    v.setVisibility(View.VISIBLE);
                    holder.selectedCheck.setVisibility(View.VISIBLE);

                    shareAndUploadImage(userList.get(position).getUuid());

                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void shareAndUploadImage(final String uuid){

        if(shareImage == null){
            Toast.makeText(context, "Please select an Image :(", Toast.LENGTH_SHORT).show();
        }else{


            //TODO
            shareImage.setDrawingCacheEnabled(true);
            shareImage.buildDrawingCache();
            bitmap = ((BitmapDrawable) shareImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("my_images").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(uuid).child(UUID.randomUUID().toString()).putBytes(data);
            uploadTask
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "Failed uploading", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String url = task.getResult().toString();
                            String sendOrReceived = "sent";
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("url",url);
                            hm.put("sendOrReceived","sent");
                            hm.put("message",shareCaption.getText().toString());

                            FirebaseDatabase.getInstance().getReference().child("my_users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("photos").child(uuid).push().setValue(hm)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) ;
                                    else
                                        Toast.makeText(context, "sharing failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                            HashMap<String, String> h = new HashMap<>();
                            h.put("url",url);
                            h.put("sendOrReceived","recieved");
                            h.put("message",shareCaption.getText().toString());

                            FirebaseDatabase.getInstance().getReference().child("my_users").child(uuid)
                                    .child("photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(h)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) ;
                                    else
                                        Toast.makeText(context, "sharing failed", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });

                }
            });


        }

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
