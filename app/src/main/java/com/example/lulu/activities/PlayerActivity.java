package com.example.lulu.activities;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lulu.R;
import com.example.lulu.classes.ArtistSong;
import com.example.lulu.utils.FirebaseHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {
    private SeekBar mMusicSb;
    private ImageView mPlayIv;
    private ImageView mPauseIv;
    private ImageView mLikeIv;
    private Handler mHandler = new Handler();

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
        Log.d("create", "onCreate: " + songQueue + "            " + currentSong);
        ProgressDialog progressdialog = new ProgressDialog(PlayerActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        FirebaseHelper.mSingersSongsRef
                .child(currentSong.getAdderUuid())
                .child(currentSong.getUuid() + "/" + currentSong.getName() + currentSong.getFileExtension())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("song", "play: " + uri);
                        currentSongUri = uri;
                        play();
                        progressdialog.dismiss();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("create", "onCreate: " + e.getLocalizedMessage(), e);
                });
        initializeViews();
        mSongNameTv.setText(currentSong.getName());
        DatabaseReference favoriteGetter = FirebaseHelper.likedSongsDatabase.child(FirebaseHelper.mAuth.getCurrentUser().getUid()).child(currentSong.getUuid());
        favoriteGetter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    mLikeIv.setImageResource(R.drawable.ic_heart);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("canceled", "onCancelled: " + error.getDetails());
            }
        });
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
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mMediaPlayer != null) {
                    int mCurrentPosition = mMediaPlayer.getCurrentPosition() / 1000;
                    mMusicSb.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mMediaPlayer.start();
        mMusicSb.setMax(mMediaPlayer.getDuration() / 1000);
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
        mLikeIv = findViewById(R.id.btn_like);

        mMusicSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mMediaPlayer != null && fromUser) {
                    mMediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void getMaxDuration() {
       int duration = mMediaPlayer.getDuration();
        Log.d("pls", "getMaxDuration: " + String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))));
    }

}
