package com.example.jmisiak.entregabletres.dao.persistent.artist;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.jmisiak.entregabletres.model.Artist;
import com.example.jmisiak.entregabletres.model.Artwork;
import com.example.jmisiak.entregabletres.service.persistent.ArtistPersistent;
import com.example.jmisiak.entregabletres.service.persistent.ArtworkPersistent;

@Database(entities = {Artist.class}, version = 11)
public abstract class ArtistRoomDatabase extends RoomDatabase {
    public abstract ArtistPersistent artist();

    private static volatile ArtistRoomDatabase INSTANCE;

   public static ArtistRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ArtistRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ArtistRoomDatabase.class, "artist_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback = new Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ArtistPersistent mDao;

        PopulateDbAsync(ArtistRoomDatabase db) {
            mDao = db.artist();
        }

        @Override
        protected Void doInBackground(final Void... params) {
         //  mDao.deleteAll();
            return null;
        }
    }

}
