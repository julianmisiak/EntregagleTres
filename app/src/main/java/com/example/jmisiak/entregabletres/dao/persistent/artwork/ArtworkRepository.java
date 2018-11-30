package com.example.jmisiak.entregabletres.dao.persistent.artwork;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.jmisiak.entregabletres.model.Artwork;
import com.example.jmisiak.entregabletres.service.persistent.ArtworkPersistent;

import java.util.List;

public class ArtworkRepository {
    private ArtworkPersistent artworkPersistent;
    private LiveData<List<Artwork>> artworkList;

    ArtworkRepository(Application application) {
        ArtworkRoomDatabase db = ArtworkRoomDatabase.getDatabase(application);
        artworkPersistent = db.artwork();
        artworkList = artworkPersistent.getAllArtwork();
    }

    LiveData<List<Artwork>> getAllArtwork() {
        return artworkList;
    }

    public void insert(Artwork artwork) {
        new insertAsyncTask(artworkPersistent).execute(artwork);
    }

    private static class insertAsyncTask extends AsyncTask<Artwork, Void, Void> {
        private ArtworkPersistent mAsyncTaskDao;

        insertAsyncTask(ArtworkPersistent dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Artwork... params) {
            try {
                mAsyncTaskDao.insert(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}