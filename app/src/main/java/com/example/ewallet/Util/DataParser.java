package com.example.ewallet.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public class DataParser {

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> placeDetails = new HashMap<>();
        String placeName;
        JSONArray jsonArray;
        JSONObject jsonObject;

        Log.d("DataParser", "jsonobject =" + googlePlaceJson.toString());
        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
                placeDetails.put("place_name", placeName);
            }
            if (!googlePlaceJson.isNull("structured_formatting")) {
                jsonObject = googlePlaceJson.getJSONObject("structured_formatting");
                placeName = jsonObject.getString("main_text");
                placeDetails.put("place_name", placeName);
            }
            if (!googlePlaceJson.isNull(("types"))) {
                jsonArray =(googlePlaceJson.getJSONArray("types"));
                int i;
                for (i = 0; i < jsonArray.length(); i++) {
                    placeDetails.put(Integer.toString(i), jsonArray.getString(i));
                }
                placeDetails.put("length", Integer.toString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return placeDetails;

    }

    public HashMap<String, String> parse(String jsonData) {
        HashMap<String, String> placeMap = null;
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

            placeMap = getPlace((JSONObject) jsonArray.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("predictions");

            placeMap = getPlace((JSONObject) jsonArray.get(0));
        } catch (JSONException s){
            s.printStackTrace();
        }

        return placeMap;
    }
}

