package com.example.mymusicapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymusicapp.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class SongActivity extends AppCompatActivity {
    ImageView thumbnail_song;
    TextView song_title;
    Button play_button, pause_button, next_button, prev_button;
    TextView pass, due;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;

    Handler handler;

    ArrayList<String> urlList;
    ArrayList<String> songList;
    ArrayList<String> thumbList;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

//        Animation uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
//        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);

        thumbnail_song = findViewById(R.id.thumbnail_music);
        song_title = findViewById(R.id.song_title);

        play_button = findViewById(R.id.play_button);
        pause_button = findViewById(R.id.pause_button);
        seekBar = findViewById(R.id.seek_bar);
        pass = findViewById(R.id.tv_pass);
        due = findViewById(R.id.tv_due);

        next_button = findViewById(R.id.next_button);
        prev_button = findViewById(R.id.prev_button);

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (urlList.size() == position + 1) {
                    position = 0;
                    initList(songList.get(position), urlList.get(position), thumbList.get(position));
                } else {
                    position = position + 1;
                    initList(songList.get(position), urlList.get(position), thumbList.get(position));
                }
            }
        });

        prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {
                    position = urlList.size() - 1;
                    initList(songList.get(position), urlList.get(position), thumbList.get(position));
                } else {
                    position = position - 1;
                    initList(songList.get(position), urlList.get(position), thumbList.get(position));

                }
            }
        });

        mediaPlayer = new MediaPlayer();
        handler = new Handler();

        Intent intent = getIntent();
        String name = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");
        String thumbnail_img = intent.getStringExtra("artworkUrl");

        urlList = intent.getStringArrayListExtra("urlList");
        songList = intent.getStringArrayListExtra("songList");
        thumbList = intent.getStringArrayListExtra("thumbList");
        position = Integer.parseInt(intent.getStringExtra("position"));

        initList(name, url, thumbnail_img);


        pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        initializeSeekbar();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
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

    private void initList(String name, String url, String thumb_url) {
        song_title.setText(name);
//        song_title.setAnimation(fade);

        Picasso.get().load(thumb_url).resize(300, 200).centerCrop().into(thumbnail_song);
//        thumbnail_song.setAnimation(uptodown);

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mediaPlayer.start();
        initializeSeekbar();
    }

    @SuppressLint("DefaultLocale")
    private void initializeSeekbar() {
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        due.setText(String.format("%02d:%02d", (mediaPlayer.getDuration() / 1000) / 60, (mediaPlayer.getDuration() / 1000) % 60));
        SongActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);

                    String out = String.format("%02d:%02d", seekBar.getProgress() / 60, seekBar.getProgress() % 60);
                    pass.setText(out);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void play() {
        mediaPlayer.start();
        play_button.setVisibility(View.GONE);
        pause_button.setVisibility(View.VISIBLE);
    }

    private void pause() {
        mediaPlayer.pause();
        pause_button.setVisibility(View.GONE);
        play_button.setVisibility(View.VISIBLE);
    }
}