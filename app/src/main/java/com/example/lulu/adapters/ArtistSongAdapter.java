package com.example.lulu.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.activities.PlayerActivity;
import com.example.lulu.activities.SingerActivity;
import com.example.lulu.classes.ArtistSong;

import java.util.ArrayList;

public class ArtistSongAdapter extends RecyclerView.Adapter<ArtistSongAdapter.ArtistSongViewHolder>{

        ArrayList<ArtistSong> list;
        Context context;

    public ArtistSongAdapter(ArrayList<ArtistSong> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtistSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_song_item, parent,false);
        return new ArtistSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistSongViewHolder holder, int position) {
        ArtistSong currentSong = list.get(position);
        holder.name.setText(currentSong.getName());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("songs", list);
            intent.putExtra("currentSong", currentSong);
            context.startActivity(intent);
        });
        holder.likes.setText(String.valueOf(currentSong.getLikesCount()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ArtistSongViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView songImage;
        TextView likes;
        public ArtistSongViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.tv_artist_song_name);
            songImage = itemView.findViewById(R.id.iv_song_image);
            likes = itemView.findViewById(R.id.tv_artist_song_likes);
        }
    }
}