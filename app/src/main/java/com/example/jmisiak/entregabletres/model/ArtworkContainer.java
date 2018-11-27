package com.example.jmisiak.entregabletres.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtworkContainer {
    @SerializedName("paints")
    List<Artwork> artworkList;

    public List<Artwork> getArtworkList() {
        return artworkList;
    }
}
