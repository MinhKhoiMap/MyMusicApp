package com.example.mymusicapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import com.example.mymusicapp.Class.Song;
import com.example.mymusicapp.Class.SongAdapter;
import com.example.mymusicapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SongAdapter songAdapter;
    List<Song> allSongs = new ArrayList<>();

    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Music App");

        recyclerView = findViewById(R.id.recyclerview);

        fetchSongs();
    }

    private void fetchSongs() {
        List<Song> songs = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("Songs");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Song song = snap.getValue(Song.class);
                    Log.d("songs", song.getArtworkUrl());
                    songs.add(song);
                }
                showSongs(songs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSongs(List<Song> songs) {
        if (songs.size() == 0) {
            Toast.makeText(this, "No Songs", Toast.LENGTH_SHORT).show();
            return;
        }

        allSongs.clear();
        allSongs.addAll(songs);

        String title = "My Music App" + " - " + songs.size();
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        songAdapter = new SongAdapter(this, songs);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(songAdapter);
        scaleInAnimationAdapter.setDuration(1000);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        scaleInAnimationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(scaleInAnimationAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_btn, menu);

        MenuItem menuItem = menu.findItem(R.id.search_btn);

        SearchView searchView = (SearchView) menuItem.getActionView();

        SearchSong(searchView);

        return super.onCreateOptionsMenu(menu);
    }

    private void SearchSong(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSongs(newText.toLowerCase());
                return true;
            }
        });
    }

    private void filterSongs(String query) {
        List<Song> filteredList = new ArrayList<>();

        if (allSongs.size() > 0) {
            for (Song song : allSongs) {
                if (song.getTitle().toLowerCase().contains(query)) {
                    filteredList.add(song);
                }
            }

            if (songAdapter != null) {
                songAdapter.filterSongs(filteredList);
            }
        }
    }
}