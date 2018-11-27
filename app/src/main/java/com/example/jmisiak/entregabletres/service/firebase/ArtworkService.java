package com.example.jmisiak.entregabletres.service.firebase;

import com.example.jmisiak.entregabletres.model.ArtworkContainer;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArtworkService {

    @GET("bins/x858r")
    Call<ArtworkContainer> getArtworkList();
}
