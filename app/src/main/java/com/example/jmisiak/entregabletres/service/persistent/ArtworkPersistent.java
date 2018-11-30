package com.example.jmisiak.entregabletres.service.persistent;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.jmisiak.entregabletres.model.Artwork;

import java.util.List;

@Dao
public interface ArtworkPersistent {
    @Insert
    void insert(Artwork artwork);

    @Update
    void update(Artwork artwork);

    @Query("DELETE FROM ARTWORK")
    void deleteAll();

    @Query("SELECT * from ARTWORK")
    LiveData<List<Artwork>> getAllArtwork();

}
