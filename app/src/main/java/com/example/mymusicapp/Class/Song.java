package com.example.mymusicapp.Class;

public class Song {
    String title;
    String url;
    String artworkUrl;
    String duration;

    public Song() {
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public String getDuration() {
        return duration;
    }

    public int getTotalDuration() {
        int totalDuration = 0;
        String[] nums = duration.split(":");
        for (String num : nums) {
            totalDuration += Integer.getInteger(num);
        }

        return totalDuration;
    }
}
