package com.example.jmisiak.entregabletres.dao.persistent.artist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.jmisiak.entregabletres.dao.persistent.artwork.ArtworkRepository;
import com.example.jmisiak.entregabletres.model.Artist;
import com.example.jmisiak.entregabletres.model.Artwork;

import java.util.List;

public class ArtistViewModel extends AndroidViewModel {

    private ArtistRepository mRepository;
    private LiveData<List<Artist>> mAllArtist;

    public ArtistViewModel(Application application) {
        super(application);
        mRepository = new ArtistRepository(application);
        mAllArtist = mRepository.getAllArtist();
    }

    public  LiveData<List<Artist>> getAllArtist() { return mAllArtist; }
    public void insert(Artist artist) { mRepository.insert(artist); }
    public void delete() { mRepository.delete(); }
}