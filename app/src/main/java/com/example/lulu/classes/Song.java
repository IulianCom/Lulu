package com.example.lulu.classes;

import java.util.Objects;

public class Song {
    private String name;
    private String youtubeLink;
    private String uuid;
    private boolean isFavourite;

    public Song() {}

    public Song(String name, String youtubeLink, String uuid) {
        this.name = name;
        this.youtubeLink = youtubeLink;
        this.uuid = uuid;
        this.isFavourite = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(name, song.name) &&
                Objects.equals(youtubeLink, song.youtubeLink) &&
                Objects.equals(uuid, song.uuid);
    }
}
