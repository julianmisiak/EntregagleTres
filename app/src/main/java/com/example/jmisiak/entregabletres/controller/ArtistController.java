package com.example.jmisiak.entregabletres.controller;

import com.example.jmisiak.entregabletres.dao.persistent.artist.ArtistDAO;
import com.example.jmisiak.entregabletres.model.Artist;
import com.example.jmisiak.entregabletres.util.ResultListener;
import com.example.jmisiak.entregabletres.view.MainActivity;

import java.util.List;

public class ArtistController {
    private ArtistDAO dao;

    private MainActivity activity;

    public ArtistController(MainActivity activity) {
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

    public void getArtistListDatabase(final ResultListener<List<Artist>> listenerView) {

        dao.getArtistListDatabase(new ResultListener<List<Artist>>() {
            @Override
            public void finish(List<Artist> result) {
                listenerView.finish(result);
            }
        }, activity);
    }

    public void insertArtistList(List<Artist> artistList) {
        dao.insertArtistList(artistList);
    }

    public void getArtistList(final ResultListener<List<Artist>> listenerView) {
        getArtistListDatabase(new ResultListener<List<Artist>>() {
            @Override
            public void finish(List<Artist> result) {
                if (result.isEmpty()) {
                    getArtistFirebase(new ResultListener<List<Artist>>() {
                        @Override
                        public void finish(List<Artist> result) {
                            dao.insertArtistList(result);
                            listenerView.finish(result);
                        }
                    });
                } else {
                    listenerView.finish(result);
                }
            }
        });
    }
}
