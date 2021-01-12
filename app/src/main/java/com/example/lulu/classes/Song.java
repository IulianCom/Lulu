package com.example.lulu;

public class Song {
    private String name;
    private String youtubeLink;
    private String uuid;

    public Song() {}

    public Song(String name, String youtubeLink, String uuid) {
        this.name = name;
        this.youtubeLink = youtubeLink;
        this.uuid = uuid;
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
}
