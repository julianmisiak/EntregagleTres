package com.example.jmisiak.entregabletres.dao.persistent.artist;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.jmisiak.entregabletres.model.Artist;
import com.example.jmisiak.entregabletres.service.persistent.ArtistPersistent;

import java.util.List;

public class ArtistRepository {
    private ArtistPersistent artistPersistent;
    private LiveData<List<Artist>> artistList;

    ArtistRepository(Application application) {
        ArtistRoomDatabase db = ArtistRoomDatabase.getDatabase(application);
        artistPersistent = db.artist();
        artistList = artistPersistent.getAllArtist();
    }

    LiveData<List<Artist>> getAllArtist() {
        return artistList;
    }

    public void insert(Artist artist) {
        new insertAsyncTask(artistPersistent).execute(artist);
    }

    public Artist getArtistById(String artistId) {
        return artistPersistent.getArtistById(artistId);
    }

    public Integer countRows() {
        return artistPersistent.countRows();
    }

    private static class insertAsyncTask extends AsyncTask<Artist, Void, Void> {
        private ArtistPersistent mAsyncTaskDao;

        insertAsyncTask(ArtistPersistent dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Artist... params) {
            try {
                mAsyncTaskDao.insert(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}