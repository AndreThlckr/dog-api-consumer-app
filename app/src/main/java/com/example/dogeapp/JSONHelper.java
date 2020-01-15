package com.example.dogeapp;

import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class JSONHelper {

    public static ArrayList getListData(JSONArray arrJson) {
        ArrayList jsonData = new ArrayList();

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

    public static String capitalize(String str) {
        if(str== null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
