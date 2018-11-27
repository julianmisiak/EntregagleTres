package com.example.jmisiak.entregabletres.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
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
    @SerializedName("Influenced_by")
    private String influencedBy;

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


    public String getInfluencedBy() {
        return influencedBy;
    }


    @Override
    public String toString() {
        return "Artist{" +
                "artistId=" + artistId +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", influencedBy='" + influencedBy + '\'' +
                '}';
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setInfluencedBy(String influencedBy) {
        this.influencedBy = influencedBy;
    }
}
