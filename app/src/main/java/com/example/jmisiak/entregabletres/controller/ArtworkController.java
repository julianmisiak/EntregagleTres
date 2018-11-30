package com.example.jmisiak.entregabletres.controller;

import com.example.jmisiak.entregabletres.dao.persistent.ArtworkDAO;
import com.example.jmisiak.entregabletres.model.Artwork;
import com.example.jmisiak.entregabletres.util.ResultListener;
import com.example.jmisiak.entregabletres.view.MainActivity;

import java.util.List;

public class ArtworkController {
    private ArtworkDAO dao;

    public ArtworkController(MainActivity activity) {
        dao = new ArtworkDAO(activity);
    }

    public void getArtworkList(final ResultListener<List<Artwork>> listenerView) {
        dao.getArtworkList(new ResultListener<List<Artwork>>() {
            @Override
            public void finish(List<Artwork> result) {
                listenerView.finish(result);
            }

        });
    }

    public void getArtworkListDatabase(final ResultListener<List<Artwork>> resultListener) {
        dao.getArtworkListDatabase(new ResultListener<List<Artwork>>() {
            @Override
            public void finish(List<Artwork> result) {
                resultListener.finish(result);
            }
        });
    }

    public void insertArtwork(List<Artwork> artworkList) {
        dao.insertArtwork(artworkList);
    }
}
