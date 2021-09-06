package com.example.lulu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lulu.R;
import com.example.lulu.classes.ArtistSong;
import com.example.lulu.utils.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {
    private SeekBar mMusicSb;
    private ImageView mPlayIv;
    private ImageView mPauseIv;

    private TextView mSongNameTv;

    private MediaPlayer mMediaPlayer;
    private ArrayList<ArtistSong> songQueue;
    private ArtistSong currentSong;

    private Uri currentSongUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        currentSong = (ArtistSong) getIntent().getSerializableExtra("currentSong");
        songQueue = (ArrayList<ArtistSong>) getIntent().getSerializableExtra("songs");
        Log.d("pula", "onCreate: " + songQueue + "            " + currentSong);
        FirebaseHelper.mSingersSongsRef
                .child(currentSong.getAdderUuid())
                .child(currentSong.getUuid() + "/" + currentSong.getName() + currentSong.getFileExtension())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("plm", "play: " + uri);
                        currentSongUri = uri;
                        play();
                        mMusicSb.setMax(mMediaPlayer.getDuration());
                        getMaxDuration();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("asda", "onCreate: " + e.getLocalizedMessage(), e);
                });
        initializeViews();
        mSongNameTv.setText(currentSong.getName());
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    private void setListeners() {
        mPlayIv.setOnClickListener(l -> {
            if(!mMediaPlayer.isPlaying()) {
                play();
                mPauseIv.setEnabled(true);
                mPlayIv.setEnabled(false);
            }
        });

        mPauseIv.setOnClickListener(l -> {
            if(mMediaPlayer.isPlaying()) {
                pause();
                mPlayIv.setEnabled(true);
                mPauseIv.setEnabled(false);
            }
        });
    }

    public void play() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, currentSongUri);
        }
        mMediaPlayer.start();
    }

    public void pause() {
        if(mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stopPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void initializeViews() {
        mMusicSb = findViewById(R.id.sb_activity_player);
        mPlayIv = findViewById(R.id.iv_activity_player_play);
        mPauseIv = findViewById(R.id.iv_activity_player_pause);
        mSongNameTv = findViewById(R.id.activity_player_tv_song_name);
    }

    public void getMaxDuration() {
       int duration = mMediaPlayer.getDuration();
        Log.d("pls", "getMaxDuration: " + String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))));
    }
}
