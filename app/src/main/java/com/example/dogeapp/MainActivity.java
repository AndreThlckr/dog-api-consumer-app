package com.example.dogeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity {
    public static final String url = "https://dog.ceo/api/breeds/list";
    public static final int SHOW_BREED_DETAILS = 987;
    public static final int SHOW_SUB_BREED_DETAILS = 654;
    public static String LAST_IMAGE_URL = "com.example.dogeapp.LAST_IMAGE_URL";

    private ListView list;
    private ImageView imageMain;

    private RequestQueue queue;
    private ArrayList<String> listaCachorros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);
        imageMain = findViewById(R.id.imageMain);

        //Criamos a request queue
        queue = Volley.newRequestQueue(this);

        JsonObjectRequest dogBreedsArrayRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listaCachorros = JSONHelper.getListData(response.getJSONArray("message"));
                            setListData(listaCachorros);
                        } catch (JSONException e) {
                            Log.e("JSONRequest", "Unexpected JSON exception", e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(dogBreedsArrayRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.last_image_preference), Context.MODE_PRIVATE);

        // Reading from SharedPreferences
        String imageUrl = sharedPref.getString(LAST_IMAGE_URL, "");

        if(imageUrl.isEmpty()){
            loadImageFromJSON("https://dog.ceo/api/breeds/image/random");
        } else {
            setImageView(imageUrl);
        }
    }

    protected void setListData(ArrayList<String> lista){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dogName = (String) list.getItemAtPosition(position);

                dogName = dogName.toLowerCase();

                showBreedDetails(dogName);
            }
        });
    }

    private void showBreedDetails(String dogName){
        Intent showDetails = new Intent(this, DogDescriptionActivity.class);

        showDetails.putExtra("requestCode", SHOW_BREED_DETAILS);
        showDetails.putExtra("breedName", dogName);

        startActivityForResult(showDetails, SHOW_BREED_DETAILS);
    }

    private void loadImageFromJSON(String imageJsonUrl){
        JsonObjectRequest imageBreedRequest = new JsonObjectRequest
                (Request.Method.GET, imageJsonUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setImageView(response.getString("message"));
                        } catch (JSONException e) {
                            Log.e("JSONRequest", "Unexpected JSON exception", e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(imageBreedRequest);
    }

    protected void setImageView(String url){
        Picasso.get().load(url).resize(200, 200).into(imageMain);
    }
}
