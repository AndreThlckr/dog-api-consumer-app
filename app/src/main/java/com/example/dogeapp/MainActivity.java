package com.example.dogeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String url = "https://dog.ceo/api/breeds/list";
    public static final int SHOW_BREED_DETAILS = 987;
    public static final int SHOW_SUB_BREED_DETAILS = 654;

    private ListView list;
    private ArrayList<String> listaCachorros = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);

        //Criamos a request queue
        RequestQueue queue = Volley.newRequestQueue(this);

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

    protected void setListData(ArrayList<String> lista){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
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
}
