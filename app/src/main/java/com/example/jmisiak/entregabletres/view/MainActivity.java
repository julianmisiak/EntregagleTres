package com.example.jmisiak.entregabletres.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jmisiak.entregabletres.R;
import com.example.jmisiak.entregabletres.controller.ArtistController;
import com.example.jmisiak.entregabletres.controller.ArtworkController;
import com.example.jmisiak.entregabletres.model.Artist;
import com.example.jmisiak.entregabletres.model.Artwork;
import com.example.jmisiak.entregabletres.util.ResultListener;
import com.example.jmisiak.entregabletres.view.adapter.ArtworkAdapter;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity implements ArtworkAdapter.ArtistDetailListener {
    private ArtworkController controllerArtwork;
    private ArtistController controllerArtist;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressBar pbArtwork;
    private LinearLayout artworkLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarMainActivity);
        appBarLayout = findViewById(R.id.appbarMainAcivity);
        controllerArtwork = new ArtworkController(this);
        controllerArtist = new ArtistController(this);
        pbArtwork = findViewById(R.id.pbArtwork);
        artworkLinearLayout = findViewById(R.id.artworkLinearLayout);

        loadArtworkRecyclerView();

        ImageButton btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookSdk.sdkInitialize(getApplicationContext());
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final FloatingActionButton btnUploadImage = findViewById(R.id.btnUploadImage);
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chooserTitle = getResources().getString(R.string.chooserTitle);
                EasyImage.openChooserWithGallery(MainActivity.this, chooserTitle, 1);
            }
        });


        appBarLayout.addOnOffsetChangedListener(appBarlistener);
        checkAndInsertArtistIntoDatabase();

        FloatingActionButton btnChat = findViewById(R.id.btnChat);
        btnChat.setOnClickListener(chatListener);
    }

    private void checkAndInsertArtistIntoDatabase() {
        controllerArtist.checkAndInsertArtistIntoDatabase();
    }


    private void loadArtworkRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvArtwork);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        ArtworkAdapter adapter = new ArtworkAdapter(new ArrayList<Artwork>(), R.layout.artwork_cardview, this);
        recyclerView.setAdapter(adapter);

        int resId = R.anim.grid_layout_animation_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(MainActivity.this, resId);
        recyclerView.setLayoutAnimation(animation);
        loadAdapterData(adapter);
    }

    private void loadAdapterData(final ArtworkAdapter adapter) {
        controllerArtwork.getArtworkListDatabase(new ResultListener<List<Artwork>>() {
            @Override
            public void finish(List<Artwork> result) {
                if (!result.isEmpty()) {
                    pbArtwork.setVisibility(View.GONE);
                    artworkLinearLayout.setVisibility(View.VISIBLE);
                    adapter.setArtworkList(result);

                } else {
                    controllerArtwork.getArtworkList(new ResultListener<List<Artwork>>() {
                        @Override
                        public void finish(List<Artwork> result) {
                            controllerArtwork.insertArtwork(result);
                            pbArtwork.setVisibility(View.GONE);
                            artworkLinearLayout.setVisibility(View.VISIBLE);
                            adapter.setArtworkList(result);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

    AppBarLayout.OnOffsetChangedListener appBarlistener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0) {
                String title = getResources().getString(R.string.title_collapsing);
                collapsingToolbarLayout.setTitle(title);
            } else {
                String title = getResources().getString(R.string.title_not_collapsing);
                collapsingToolbarLayout.setTitle(title);
            }
        }
    };

    @Override
    public void onClickArtwork(final Integer artistId, final byte[] imageArtworByte) {
        artistDetailDialog(artistId, imageArtworByte);
    }

    private void artistDetailDialog(final Integer artistId, final byte[] imageArtworByte) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String buttomClose = getResources().getString(R.string.buttom_close);
        builder.setNegativeButton(buttomClose, null);
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.artist_detail_layout, null);
        dialog.setView(dialogView);
        dialog.show();

        final ImageView imgArtistDetailArtwork = dialogView.findViewById(R.id.imgArtistDetailArtwork);
        final TextView tvNameArtistDetail = dialogView.findViewById(R.id.tvNameArtistDetail);
        final TextView tvNationalityArtistDetail = dialogView.findViewById(R.id.tvNationalityArtistDetail);
        final TextView tvInfluencedByArtistDetail = dialogView.findViewById(R.id.tvInfluencedByArtistDetail);

        loadDataArtistDetail(artistId, imageArtworByte, imgArtistDetailArtwork, tvNameArtistDetail, tvNationalityArtistDetail, tvInfluencedByArtistDetail);
    }

    private void loadDataArtistDetail(final Integer artistId, final byte[] imageArtworByte, final ImageView imgArtistDetailArtwork, final TextView tvNameArtistDetail, final TextView tvNationalityArtistDetail, final TextView tvInfluencedByArtistDetail) {
        final Artist artist = controllerArtist.getArtistById(artistId.toString());
        Glide.with(getApplicationContext()).load(imageArtworByte).into(imgArtistDetailArtwork);
        tvNameArtistDetail.setText(artist.getName());
        tvNationalityArtistDetail.setText(artist.getNationality());
        tvInfluencedByArtistDetail.setText(artist.getInfluenced_by());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, MainActivity.this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource imageSource, int i) {

            }

            @Override
            public void onImagesPicked(@NonNull List<File> list, EasyImage.ImageSource imageSource, int i) {
                if (list.size() > 0) {
                    final File file = list.get(0);
                    Uri uri = Uri.fromFile(file);

                    Uri uriTemporal = Uri.fromFile(new File(uri.getPath()));
                    String extension = uriTemporal.getLastPathSegment().substring(uriTemporal.getLastPathSegment().indexOf("."));
                    FirebaseStorage mStorage = FirebaseStorage.getInstance();
                    StorageReference raiz = mStorage.getReference();
                    Date time = Calendar.getInstance().getTime();
                    StorageReference pictureNew = raiz.child("fotos").child(time + extension);
                    UploadTask uploadTask = pictureNew.putFile(uri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });

                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource imageSource, int i) {

            }
        });
    }


    View.OnClickListener chatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
        }
    };

}
