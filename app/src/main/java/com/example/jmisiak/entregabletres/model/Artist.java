package com.example.jmisiak.entregabletres.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.database.PropertyName;

@Entity(tableName = "ARTIST")
public class Artist {
    @PrimaryKey
    @NonNull
    private String artistId;

    @ColumnInfo(name = "NAME")
    private String name;

    @ColumnInfo(name = "NACIONALITY")
    private String nationality;

    @ColumnInfo(name = "INFLUENCED_BY")
    @PropertyName("Influenced_by")
    private String Influenced_by;

    public Artist() {
    }

    public String getArtistId() {
        return artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public String getInfluenced_by() {
        return Influenced_by;
    }

    public void setInfluenced_by(String influenced_by) {
        Influenced_by = influenced_by;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

}
