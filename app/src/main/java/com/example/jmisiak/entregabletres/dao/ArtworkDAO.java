package com.example.jmisiak.entregabletres.dao;

import android.util.Log;

import com.example.jmisiak.entregabletres.model.Artwork;
import com.example.jmisiak.entregabletres.model.ArtworkContainer;
import com.example.jmisiak.entregabletres.service.firebase.ArtworkService;
import com.example.jmisiak.entregabletres.util.ResultListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtworkDAO extends DaoHelper {
    private ArtworkService service;

    public ArtworkDAO() {
        super();
        service = retrofit.create(ArtworkService.class);
    }

    public void getArtworkList(final ResultListener<List<Artwork>> resultListener) {
        Call<ArtworkContainer> call = service.getArtworkList();

        call.enqueue(new Callback<ArtworkContainer>() {
            @Override
            public void onResponse(Call<ArtworkContainer> call, Response<ArtworkContainer> response) {
                ArtworkContainer container = response.body();
                List<Artwork> resultList = container.getArtworkList();
                resultListener.finish(resultList);
            }

            @Override
            public void onFailure(Call<ArtworkContainer> call, Throwable t) {
                Log.e("ERROR", t.toString());
            }
        });
    }
}
