package com.example.dogeapp;

import java.util.ArrayList;

public class Breed {
    private String breedName;
    private String imageUrl;
    private ArrayList<String> subBreeds;
    private boolean isFavorite;

    public Breed(String breedName) {
        this.breedName = breedName;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getSubBreeds() {
        return subBreeds;
    }

    public void setSubBreeds(ArrayList<String> subBreeds) {
        this.subBreeds = subBreeds;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public static ArrayList<Breed> generateBreedListFromNames(ArrayList<String> dogBreedNames){
        ArrayList<Breed> breedsList = new ArrayList<>();

        for (String breedName: dogBreedNames) {
            breedsList.add(new Breed(breedName));
        }

        return breedsList;
    }
}
