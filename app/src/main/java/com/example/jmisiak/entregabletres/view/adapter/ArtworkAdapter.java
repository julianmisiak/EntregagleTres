package com.example.jmisiak.entregabletres.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jmisiak.entregabletres.R;
import com.example.jmisiak.entregabletres.model.Artwork;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ArtworkViewHolder> {
    private List<Artwork> artworkList;
    private Integer resources;
    private FirebaseStorage mStorage;
    private StorageReference raiz;
    private ArtistDetailListener artistDetailListener;

    public ArtworkAdapter(List<Artwork> artworkList, Integer resources, ArtistDetailListener artistDetailListener) {
        this.artworkList = artworkList;
        this.resources = resources;
        this.artistDetailListener = artistDetailListener;

        mStorage = FirebaseStorage.getInstance();
        raiz = mStorage.getReference();
    }

    @NonNull
    @Override
    public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resources, parent, false);
        return new ArtworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtworkViewHolder artworkViewHolder, int position) {
        Artwork artwork = artworkList.get(position);
        artworkViewHolder.load(artwork);

    }

    @Override
    public int getItemCount() {
        return artworkList.size();
    }

    public interface ArtistDetailListener {
        void onClickArtwork(Integer artistId, byte[] imageArtworkByte);
    }

    public class ArtworkViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPictureArtwork;
        private TextView tvArtistName;

        public ArtworkViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPictureArtwork = itemView.findViewById(R.id.imgPictureArtwork);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Artwork artwork = artworkList.get(getAdapterPosition());
                    artistDetailListener.onClickArtwork(artwork.getArtistId(), artwork.getImageByte());
                }
            });
        }

        public void load(final Artwork artwork) {
            Glide.with(itemView.getContext()).load(artwork.getImageByte()).into(imgPictureArtwork);
            tvArtistName.setText(artwork.getName());

        }
    }

    public void setArtworkList(List<Artwork> artworkList) {
        this.artworkList = artworkList;
        notifyDataSetChanged();
    }
}
