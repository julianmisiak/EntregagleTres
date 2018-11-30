package com.example.jmisiak.entregabletres.controller;

import android.support.v7.app.AppCompatActivity;

import com.example.jmisiak.entregabletres.dao.persistent.ArtistDAO;
import com.example.jmisiak.entregabletres.model.Artist;
import com.example.jmisiak.entregabletres.util.ResultListener;

import java.util.List;

public class ArtistController {
    private ArtistDAO dao;

    private AppCompatActivity activity;

    public ArtistController(AppCompatActivity activity) {
        this.activity = activity;
        dao = new ArtistDAO(activity);
    }

    public void getArtistFirebase(final ResultListener<List<Artist>> listenerView) {
        dao.getArtistListFirebase(new ResultListener<List<Artist>>() {
            @Override
            public void finish(List<Artist> result) {
                listenerView.finish(result);
            }
        });

    }

    public Artist getArtistById(String artistId) {
        return dao.getArtistById(artistId);
    }

    public void checkAndInsertArtistIntoDatabase() {
        if (dao.countRows() == 0) {
            getArtistFirebase(new ResultListener<List<Artist>>() {
                @Override
                public void finish(List<Artist> result) {
                    dao.insertArtistList(result);
                }
            });
        }
    }
}
