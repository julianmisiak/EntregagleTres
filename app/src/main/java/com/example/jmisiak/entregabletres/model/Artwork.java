package com.example.jmisiak.entregabletres.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.Objects;

@Entity(tableName = "ARTWORK")
public class Artwork implements Comparable{
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @NonNull
    @ColumnInfo(name = "ARTIST_ID")
    private Integer artistId;

    private String image;

    @ColumnInfo(name = "NAME")
    private String name;
    @Ignore
    private Uri imageUri;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] imageByte;

    public Artwork(){}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getArtistId() {
        return artistId;
    }

    public void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artwork artwork = (Artwork) o;
        return artistId.equals(artwork.artistId);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return artistId.hashCode();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Artwork artwork = (Artwork) o;
        if(this.equals(artwork)){
            return  0;
        }else if(this.getArtistId() < artwork.getArtistId()){
            return -1;
        }else{
            return 1;
        }
    }
}
