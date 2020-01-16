package com.example.dogeapp;

import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

class JSONHelper {

    static ArrayList getListData(JSONArray arrJson) {
        ArrayList<String> jsonData = new ArrayList<>();

        if (arrJson != null) {
            String s;
            for (int i = 0; i < arrJson.length(); i++) {
                try {
                    s = arrJson.getString(i);
                    s = capitalize(s);
                    jsonData.add(s);
                } catch (JSONException e) {
                    Log.e("JSONRequest", "Unexpected JSON exception", e);
                }
            }
        }

        return jsonData;
    }

    private static String capitalize(String str) {
        if(str== null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
