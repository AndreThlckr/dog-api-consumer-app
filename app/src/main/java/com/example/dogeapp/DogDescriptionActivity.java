package com.example.dogeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DogDescriptionActivity extends AppCompatActivity {
    private ImageView imageView;
    private RecyclerView rv;

    private BreedAdapter adapter;
    private RequestQueue requestQueue;
    private ArrayList<Breed> listaCachorros = new ArrayList<>();

    private String imageUrl;
    private String subBreedUrl = "";
    private String breedName;
    private String subBreedName;
    private boolean isSubBreed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_description);

        TextView textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        rv = findViewById(R.id.rv);

        Intent intent = getIntent();
        breedName = intent.getStringExtra("breedName");

        int requestCode = intent.getIntExtra("requestCode", MainActivity.SHOW_BREED_DETAILS);

        if(requestCode == MainActivity.SHOW_BREED_DETAILS) {
            isSubBreed = false;
        } else {
            isSubBreed = true;
            subBreedName = intent.getStringExtra("subBreedName");
        }

        requestQueue = Volley.newRequestQueue(this);
        setUrls();

        loadImageFromJSON(imageUrl);

        if(isSubBreed) {
            textView.setText(subBreedName);
        } else {
            textView.setText(breedName);
            setRecyclerView();
            loadSubBreedList();
        }
    }

    protected void setRecyclerView(){
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        adapter = new BreedAdapter(listaCachorros);
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new ItemClickListener() {

            @Override
            public void onItemClick(int position) {
                String dogName = listaCachorros.get(position).getBreedName();

                showSubBreedDetails(dogName);
            }

            @Override
            public void onFavoriteClick(int position) {
                Toast.makeText(DogDescriptionActivity.this, "Favoritado!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSubBreedDetails(String subBreed){
        Intent showDetails = new Intent(this, DogDescriptionActivity.class);

        showDetails.putExtra("requestCode", MainActivity.SHOW_SUB_BREED_DETAILS);
        showDetails.putExtra("breedName", breedName);
        showDetails.putExtra("subBreedName", subBreed);

        startActivityForResult(showDetails, MainActivity.SHOW_SUB_BREED_DETAILS);
    }

    private void setUrls(){
        String subBreedUrlStart = "https://dog.ceo/api/breed/";
        String subBreedUrlEnd = "/list";
        String imageUrlStart = "https://dog.ceo/api/breed/" + breedName;
        String imageUrlEnd = "/images/random";

        if(isSubBreed){
            imageUrl = imageUrlStart + "/" + subBreedName  + imageUrlEnd;
        } else {
            subBreedUrl = subBreedUrlStart + breedName + subBreedUrlEnd;
            imageUrl = imageUrlStart + imageUrlEnd;
        }
    }

    private void loadImageFromJSON(String imageUrl){
        JsonObjectRequest imageBreedRequest = new JsonObjectRequest
                (Request.Method.GET, imageUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String url = response.getString("message");
                            setImageView(url, imageView);
                            setImageUrlInPreferences(url);
                        } catch (JSONException e) {
                            Log.e("JSONRequest", "Unexpected JSON exception", e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(DogDescriptionActivity.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(imageBreedRequest);
    }

    protected void setImageView(String url, ImageView imageView) {
        Picasso.get().load(url).resize(225, 225).into(imageView);
    }

    private void loadSubBreedList(){
            JsonObjectRequest dogSubBreedsArrayRequest = new JsonObjectRequest
                    (Request.Method.GET, subBreedUrl, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                ArrayList subBreedNameList = JSONHelper.getListData(response.getJSONArray("message"));

                                listaCachorros.clear();

                                listaCachorros.addAll(Breed.generateBreedListFromNames(subBreedNameList));

                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                Log.e("JSONRequest", "Unexpected JSON exception", e);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(DogDescriptionActivity.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
                        }
                    });

            requestQueue.add(dogSubBreedsArrayRequest);
    }

    private void setImageUrlInPreferences(String imageUrl){
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.last_image_preference), Context.MODE_PRIVATE);

        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MainActivity.LAST_IMAGE_URL, imageUrl);
        editor.apply();
    }
}
