package com.example.jmisiak.entregabletres.dao.persistent;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jmisiak.entregabletres.dao.DaoHelper;
import com.example.jmisiak.entregabletres.dao.persistent.artwork.ArtworkViewModel;
import com.example.jmisiak.entregabletres.model.Artwork;
import com.example.jmisiak.entregabletres.model.ArtworkContainer;
import com.example.jmisiak.entregabletres.service.firebase.ArtworkService;
import com.example.jmisiak.entregabletres.util.ResultListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtworkDAO extends DaoHelper {
    private ArtworkService service;
    private ArtworkViewModel mArtworkViewModel;
    private AppCompatActivity activity;
    private FirebaseStorage mStorage;

    public ArtworkDAO(AppCompatActivity activity) {
        super();
        service = retrofit.create(ArtworkService.class);
        mArtworkViewModel = ViewModelProviders.of(activity).get(ArtworkViewModel.class);
        mStorage = FirebaseStorage.getInstance();
        this.activity = activity;
    }

    public void getArtworkList(final ResultListener<List<Artwork>> resultListener) {
        Call<ArtworkContainer> call = service.getArtworkList();

        call.enqueue(new Callback<ArtworkContainer>() {
            @Override
            public void onResponse(Call<ArtworkContainer> call, Response<ArtworkContainer> response) {
                ArtworkContainer container = response.body();
                List<Artwork> resultList = container.getArtworkList();
                resultListener.finish(resultList);
            }

            @Override
            public void onFailure(Call<ArtworkContainer> call, Throwable t) {
                Log.e("ERROR", t.toString());
            }
        });
    }


    public void getArtworkListDatabase(final ResultListener<List<Artwork>> resultListener) {
        mArtworkViewModel = ViewModelProviders.of(activity).get(ArtworkViewModel.class);
        mArtworkViewModel.getAllArtwork().observe(activity, new Observer<List<Artwork>>() {
            @Override
            public void onChanged(@Nullable List<Artwork> artworks) {
                resultListener.finish(artworks);
                mArtworkViewModel.getAllArtwork().removeObserver(this);
            }

        });
    }

    public void insertArtwork(List<Artwork> artworkList) {
        StorageReference raiz = mStorage.getReference();
        for (final Artwork artwork : artworkList) {
            StorageReference imagenReference = raiz.child(artwork.getImage());
            imagenReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(activity.getApplicationContext())
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