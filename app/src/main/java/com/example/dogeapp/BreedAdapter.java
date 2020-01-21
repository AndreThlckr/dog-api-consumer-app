package com.example.dogeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BreedAdapter extends RecyclerView.Adapter<BreedAdapter.BreedsViewHolder> {

    private List<Breed> breeds;
    private ItemClickListener itemClickListener;

    public BreedAdapter(List<Breed> breeds) {
        this.breeds = breeds;
    }

    public BreedAdapter(ArrayList<String> breedNames) {
        this.breeds = Breed.generateBreedListFromNames(breedNames);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public BreedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new BreedsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (breeds != null && breeds.size() > 0) ? breeds.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull BreedsViewHolder holder, int position) {
        Breed breed = breeds.get(position);
        String breedName = breed.getBreedName();

        breedName = breedName.substring(0, 1).toUpperCase() + breedName.substring(1);

        holder.textBreedName.setText(breedName);
        holder.setAsFavorite(breed.isFavorite());
    }

    public class BreedsViewHolder extends RecyclerView.ViewHolder {

        private TextView textBreedName;
        private ImageView favoriteButton;

        public BreedsViewHolder(@NonNull View itemView) {
            super(itemView);

            Breed breed = breeds.get(getAdapterPosition());

            textBreedName = itemView.findViewById(R.id.txt_name);
            favoriteButton = itemView.findViewById(R.id.ItemListStar);

            textBreedName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        itemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        itemClickListener.onFavoriteClick(getAdapterPosition());
                    }
                }
            });
        }

        protected void setAsFavorite(boolean isFavorite){
            if(isFavorite){
                favoriteButton.setImageResource(R.drawable.ic_star_checked_24dp);
            } else {
                favoriteButton.setImageResource(R.drawable.ic_star_unchecked_24dp);
            }
        }
    }
}