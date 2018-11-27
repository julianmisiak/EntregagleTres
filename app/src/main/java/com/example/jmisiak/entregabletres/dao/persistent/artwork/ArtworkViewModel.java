package com.example.jmisiak.entregabletres.dao.persistent.artwork;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.jmisiak.entregabletres.dao.persistent.artwork.ArtworkRepository;
import com.example.jmisiak.entregabletres.model.Artwork;

import java.util.List;

public class ArtworkViewModel  extends AndroidViewModel {

    private ArtworkRepository mRepository;
    private LiveData<List<Artwork>> mAllArtwork;

    public ArtworkViewModel (Application application) {
        super(application);
        mRepository = new ArtworkRepository(application);
        mAllArtwork = mRepository.getAllArtwork();
    }

    public  LiveData<List<Artwork>> getAllArtwork() { return mAllArtwork; }
    public void insert(Artwork artwork) { mRepository.insert(artwork); }
    public void delete() { mRepository.delete(); }
}