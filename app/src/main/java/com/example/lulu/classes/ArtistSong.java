package com.example.lulu.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ArtistSong extends Song implements Serializable {
    private int likesCount;
    private String fileExtension;

    public ArtistSong() {
        super();
    }

    public ArtistSong(String name, String uuid, String fileExtension, String adderUuid) {
        super(name, uuid, adderUuid);
        this.likesCount = 0;
        this.fileExtension = fileExtension;
    }

    public ArtistSong(String name, String uuid, int likesCount, String fileExtension, String adderUuid) {
        super(name, uuid, adderUuid);
        this.likesCount = likesCount;
        this.fileExtension = fileExtension;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileName() {
        if(fileExtension == null)
            return super.getName();
        return super.getName() + fileExtension;
    }

    @Override
    public String toString() {
        return super.toString() +
                "ArtistSong{" +
                "likesCount=" + likesCount +
                ", fileExtension='" + fileExtension + '\'' +
                '}';
    }
}
