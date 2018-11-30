package com.example.jmisiak.entregabletres.dao.persistent;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.example.jmisiak.entregabletres.dao.persistent.artist.ArtistViewModel;
import com.example.jmisiak.entregabletres.model.Artist;
import com.example.jmisiak.entregabletres.util.ResultListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistDAO {
    public static final String PATH_ARTIST_DATABASE = "artists";
    private FirebaseDatabase mDatabase;
    private ArtistViewModel mArtistViewModel;

    public ArtistDAO(AppCompatActivity activity) {
        mDatabase = FirebaseDatabase.getInstance();
        mArtistViewModel = ViewModelProviders.of(activity).get(ArtistViewModel.class);
    }

    public void getArtistListFirebase(final ResultListener<List<Artist>> resultListener) {
        final List<Artist> artistList = new ArrayList<>();
        DatabaseReference raizDatabase = mDatabase.getReference();
        raizDatabase.child(PATH_ARTIST_DATABASE).addValueEventListener(new ValueEventListener() {
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

    public void insertArtistList(List<Artist> artistList) {
        for (Artist artist : artistList) {
            mArtistViewModel.insert(artist);
        }
    }

    public Artist getArtistById(String artistId) {
        return mArtistViewModel.getArtistById(artistId);
    }

    public Integer countRows() {
        return mArtistViewModel.countRows();
    }
}
