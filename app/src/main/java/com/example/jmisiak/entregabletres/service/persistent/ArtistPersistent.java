package com.example.jmisiak.entregabletres.service.persistent;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jmisiak.entregabletres.model.Artist;
import com.example.jmisiak.entregabletres.model.Artwork;

import java.util.List;

@Dao
public interface ArtistPersistent {
    @Insert
    void insert(Artist artist);

    @Update
    void update(Artist artist);

    @Query("DELETE FROM ARTIST")
    void deleteAll();

    @Query("SELECT * from ARTIST")
    LiveData<List<Artist>> getAllArtist();
}
