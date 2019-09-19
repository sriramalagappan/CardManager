package com.example.ewallet.Util;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyLocations extends AsyncTask<Object, Void, HashMap<String, String>> {

    private String googlePlacesData;
    String url;

    @Override
    protected HashMap<String,String> doInBackground(Object... obj) {
        url =(String)obj[0];
        HashMap<String, String> nearbyPlace = null;
        DataParser parser = new DataParser();
        DownloadURL downloadURL = new DownloadURL();

        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            nearbyPlace = parser.parse(googlePlacesData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nearbyPlace;
    }
}