package com.example.jmisiak.entregabletres.controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jmisiak.entregabletres.dao.ArtworkDAO;
import com.example.jmisiak.entregabletres.dao.persistent.artwork.ArtworkViewModel;
import com.example.jmisiak.entregabletres.model.Artwork;
import com.example.jmisiak.entregabletres.util.ResultListener;
import com.example.jmisiak.entregabletres.view.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ArtworkController {
    ArtworkDAO dao;
    private ArtworkViewModel mArtworkViewModel;
    private FirebaseStorage mStorage;
    private StorageReference raiz;

    public ArtworkController() {
        dao = new ArtworkDAO();
        mStorage = FirebaseStorage.getInstance();
        raiz = mStorage.getReference();
    }

    public void getArtworkList(final ResultListener<List<Artwork>> listenerView) {
        dao.getArtworkList(new ResultListener<List<Artwork>>() {
            @Override
            public void finish(List<Artwork> result) {
                listenerView.finish(result);
            }

        });
    }

    public void getArtworkListDatabase(final ResultListener<List<Artwork>> resultListener, MainActivity activity) {
        mArtworkViewModel = ViewModelProviders.of(activity).get(ArtworkViewModel.class);
        mArtworkViewModel.getAllArtwork().observe(activity, new Observer<List<Artwork>>() {
            @Override
            public void onChanged(@Nullable List<Artwork> artworks) {
                resultListener.finish(artworks);
                mArtworkViewModel.getAllArtwork().removeObserver(this);
            }

        });

    }


    public void insertArtwork(List<Artwork> artworkList, final Context context) {
        for (final Artwork artwork : artworkList) {
            StorageReference imagenReference = raiz.child(artwork.getImage());
            imagenReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .asBitmap().load(uri)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    artwork.setImageByte(stream.toByteArray());
                                    mArtworkViewModel.insert(artwork);

                                }
                            });
                }
            });

        }


    }

}
