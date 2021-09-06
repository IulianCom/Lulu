package com.example.lulu.classes;

import com.example.lulu.utils.FirebaseHelper;

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

    public void incrementAppreciations() {
        FirebaseHelper.userDatabase.child(adderUuid).get().addOnSuccessListener(dataSnapshot -> {
            User adder = dataSnapshot.getValue(User.class);
            adder.setAppreciations(adder.getAppreciations() + 1);
            FirebaseHelper.userDatabase.child(adderUuid).setValue(adder);
        });
    }

    public void decrementAppreciations() {
        FirebaseHelper.userDatabase.child(adderUuid).get().addOnSuccessListener(dataSnapshot -> {
            User adder = dataSnapshot.getValue(User.class);
            adder.setAppreciations(adder.getAppreciations() - 1);
            FirebaseHelper.userDatabase.child(adderUuid).setValue(adder);
        });
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
