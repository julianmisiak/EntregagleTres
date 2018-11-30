package com.example.jmisiak.entregabletres.dao.persistent.artwork;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.jmisiak.entregabletres.model.Artwork;
import com.example.jmisiak.entregabletres.service.persistent.ArtworkPersistent;

@Database(entities = {Artwork.class}, version = 21)
public abstract class ArtworkRoomDatabase extends RoomDatabase {
    public abstract ArtworkPersistent artwork();

    private static volatile ArtworkRoomDatabase INSTANCE;

    public static ArtworkRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ArtworkRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ArtworkRoomDatabase.class, "artwork_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final ArtworkPersistent mDao;

        PopulateDbAsync(ArtworkRoomDatabase db) {
            mDao = db.artwork();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            return null;
        }
    }

}
