package com.example.jmisiak.entregabletres.dao.persistent.artist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jmisiak.entregabletres.model.Artist;
import com.example.jmisiak.entregabletres.util.ResultListener;
import com.example.jmisiak.entregabletres.view.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistDAO {
    private FirebaseDatabase mDatabase;
    private DatabaseReference raizDatabase;
    private ArtistViewModel mArtistViewModel;

    public ArtistDAO(MainActivity activity) {
        mDatabase = FirebaseDatabase.getInstance();
        raizDatabase = mDatabase.getReference();
        mArtistViewModel = ViewModelProviders.of(activity).get(ArtistViewModel.class);
    }

    public void getArtistListFirebase(final ResultListener<List<Artist>> resultListener) {
        final List<Artist> artistList = new ArrayList<>();
        raizDatabase.child("artists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                    Artist artist = childSnapShot.getValue(Artist.class);
                    artistList.add(artist);
                }
                resultListener.finish(artistList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void getArtistListDatabase(final ResultListener<List<Artist>> resultListener, final MainActivity activity) {
        mArtistViewModel = ViewModelProviders.of(activity).get(ArtistViewModel.class);
        mArtistViewModel.getAllArtist().observe(activity, new Observer<List<Artist>>() {
            @Override
            public void onChanged(@Nullable List<Artist> artistsList) {
                resultListener.finish(artistsList);
                mArtistViewModel.getAllArtist().removeObserver(this);
            }

        });
    }


    public void insertArtistList(List<Artist> artistList){
        for(Artist artist : artistList){
            mArtistViewModel.insert(artist);
        }
    }
}
