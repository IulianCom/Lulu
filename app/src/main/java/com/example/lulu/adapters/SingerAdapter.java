package com.example.lulu.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lulu.R;
import com.example.lulu.activities.SingerActivity;
import com.example.lulu.classes.Singer;
import com.example.lulu.classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.lulu.utils.FirebaseHelper.mSingersImagesRef;
import static com.example.lulu.utils.FirebaseHelper.mStorageRef;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.SingerViewHolder>{

    ArrayList<User> list;
    Context context;

    public SingerAdapter(ArrayList<User> list, Context context){
        this.list=list;
        this.context = context;
    }
    @NonNull
    @Override
    public SingerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singer_item, parent,false);
        return new SingerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingerViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        mStorageRef.child("users/" + list.get(position)
                .getUuid() + "/profile.jpg")
                .getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get()
                            .load(uri)
                            .into(holder.singerImage);
                });
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SingerActivity.class);
            intent.putExtra("uuid", list.get(position).getUuid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SingerViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView singerImage;
        public SingerViewHolder(@NonNull View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.singers_name);
            singerImage=itemView.findViewById(R.id.singer_image);
        }
    }
}
