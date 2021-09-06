package com.example.lulu.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.classes.Song;

import java.util.ArrayList;

import static com.example.lulu.utils.FirebaseHelper.likedSongsDatabase;
import static com.example.lulu.utils.FirebaseHelper.mAuth;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{

    ArrayList<Song> list;
    Context context;

    public SongAdapter(ArrayList<Song> list, Context context){
        this.list=list;
        this.context = context;
    }
    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song currentSong = list.get(position);
        holder.name.setText(currentSong.getName());

        holder.itemView.setOnClickListener(view -> {
            //goToWebPage(currentSong.getYoutubeLink());
        });

        holder.likedSong.setChecked(currentSong.isFavourite());


        holder.likedSong.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                currentSong.setFavourite(true);
                likedSongsDatabase.child(mAuth.getCurrentUser().getUid()).child(currentSong.getUuid()).setValue(currentSong);
            }
            else {
                currentSong.setFavourite(false);
                likedSongsDatabase.child(mAuth.getCurrentUser().getUid()).child(currentSong.getUuid()).removeValue();
            }
        });
    }

    private void goToWebPage(String yourUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(yourUrl));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView songImage;
        CheckBox likedSong;
        public SongViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.tv_song_name);
            songImage = itemView.findViewById(R.id.iv_song_image);
            likedSong = itemView.findViewById(R.id.checkBox);
        }
    }
}
