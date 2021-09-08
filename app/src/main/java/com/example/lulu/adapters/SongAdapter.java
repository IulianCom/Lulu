package com.example.lulu.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.activities.PlayerActivity;
import com.example.lulu.classes.ArtistSong;
import com.example.lulu.classes.Song;
import com.example.lulu.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.lulu.utils.FirebaseHelper.artistSongsDatabase;
import static com.example.lulu.utils.FirebaseHelper.likedSongsDatabase;
import static com.example.lulu.utils.FirebaseHelper.mAuth;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{

    ArrayList<Song> list;
    ArrayList<ArtistSong> artistSongs;
    Context context;

    public SongAdapter(ArrayList<Song> list, Context context){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        DatabaseReference ref = FirebaseHelper.artistSongsDatabase;
        artistSongs = new ArrayList<>();
        for (Song song : list) {
            ref.child(song.getAdderUuid())
                    .child(song.getUuid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            artistSongs.add(snapshot.getValue(ArtistSong.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song currentSong = list.get(position);
        holder.name.setText(currentSong.getName());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("songs", artistSongs);
            intent.putExtra("currentSong", artistSongs.get(position));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        holder.likedSong.setChecked(currentSong.isFavourite());
        holder.likedSong.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentSong.setFavourite(isChecked);
            likedSongsDatabase.child(mAuth.getCurrentUser().getUid()).child(currentSong.getUuid()).setValue(currentSong);
            if(isChecked) {
                artistSongsDatabase.child(currentSong.getAdderUuid()).child(currentSong.getUuid()).child("likesCount").setValue(artistSongs.get(position).getLikesCount() + 1);
                currentSong.incrementAppreciations();
            }
            else {
                artistSongsDatabase.child(currentSong.getAdderUuid()).child(currentSong.getUuid()).child("likesCount").setValue(artistSongs.get(position).getLikesCount() - 1);
                likedSongsDatabase.child(mAuth.getUid()).child(currentSong.getUuid()).removeValue();
                list.remove(currentSong);
                currentSong.decrementAppreciations();
            }
        });
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
