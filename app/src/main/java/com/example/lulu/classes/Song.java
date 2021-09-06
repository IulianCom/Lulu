package com.example.lulu.classes;

import java.io.Serializable;
import java.util.Objects;

public class Song implements Serializable {
    private String uuid;
    private String name;
    private boolean isFavourite;
    private String adderUuid;

    public Song() {}

    public Song(String name, String uuid, String adderUuid) {
        this.uuid = uuid;
        this.name = name;
        this.isFavourite = false;
        this.adderUuid = adderUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getAdderUuid() {
        return adderUuid;
    }

    public void setAdderUuid(String adderUuid) {
        this.adderUuid = adderUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(name, song.name) &&
                Objects.equals(uuid, song.uuid);
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }
}
