package com.example.mymusicapp.Class;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusicapp.Activities.SongActivity;
import com.example.mymusicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Song> songs;
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> songList = new ArrayList<>();
    ArrayList<String> thumbList = new ArrayList<>();

    public SongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row_item, parent, false);

        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Song song = songs.get(position);
        SongViewHolder viewHolder = (SongViewHolder) holder;

        viewHolder.titleHolder.setText(song.getTitle());
        viewHolder.durationHolder.setText(song.getDuration());

        Picasso.get().load(!song.getArtworkUrl().isEmpty() ? song.getArtworkUrl() : "https://images.unsplash.com/photo-1619983081563-430f63602796?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1887&q=80")
                .resize(90, 60).centerCrop().into(viewHolder.artworkHolder);


        if (!urlList.contains(songs.get(position).getUrl()))
            urlList.add(songs.get(position).getUrl());
        if (!songList.contains(songs.get(position).getTitle()))
            songList.add(songs.get(position).getTitle());
        if (!thumbList.contains(songs.get(position).getArtworkUrl()))
            thumbList.add(songs.get(position).getArtworkUrl());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SongActivity.class);
                intent.putExtra("title", song.getTitle());
                intent.putExtra("url", song.getUrl());
                intent.putExtra("artworkUrl", song.getArtworkUrl());

                intent.putExtra("urlList", urlList);
                intent.putExtra("songList", songList);
                intent.putExtra("thumbList", thumbList);
                intent.putExtra("position", String.valueOf(position));

                context.startActivity(intent);
            }
        });
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView artworkHolder;
        TextView titleHolder, durationHolder, sizeHolder;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            artworkHolder = itemView.findViewById(R.id.artworkView);
            titleHolder = itemView.findViewById(R.id.titleView);
            durationHolder = itemView.findViewById(R.id.durationView);
            sizeHolder = itemView.findViewById(R.id.sizeView);
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterSongs(List<Song> filteredList) {
        songs = filteredList;
        notifyDataSetChanged();
    }
}
