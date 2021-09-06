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
import java.util.ArrayList;

import static com.example.lulu.utils.FirebaseHelper.mSingersImagesRef;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singer_item,parent,false);
        return new SingerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingerViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        StorageReference ref = mSingersImagesRef.child(list.get(position).getUuid());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.singerImage.getContext())
                        .load(uri)
                        .into(holder.singerImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
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
