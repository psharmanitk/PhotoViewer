package com.android.imageview.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.imageview.view.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PhotoListBuilder {
    private final int CONNECTION_TIMEOUT_VALUE = 5000;
    private String LOG_TAG = "PhotoViewer/PhotoListBuilder";
    private String flickUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1&text=";
    private String keyword;
    private URL uri;
    private PhotoListBuilder myObject;
    private int lastPageLoaded;
    private int totalPages;
    private ArrayList<PhotoObject> mPhotoObjectList;
    private ArrayList<Bitmap> list;
    private ImageAdapter myAdapter;
    LoadDataCallback callback;

    public PhotoListBuilder(String keyword, LoadDataCallback callback, ImageAdapter adapter) {
        this.keyword = keyword;
        this.callback = callback;
        this.myAdapter = adapter;
        this.mPhotoObjectList = new ArrayList<>();
        lastPageLoaded = 0;
    }

    public void loadObjects() {
        lastPageLoaded++;
        GetObjectOnFlickerTask getObjectOnFlickerTask = new GetObjectOnFlickerTask();
        getObjectOnFlickerTask.execute(keyword);
    }

    public class GetObjectOnFlickerTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            flickUrl += strings[0] + "&page="+lastPageLoaded;
            HttpURLConnection con;
            InputStream inputStream = null;
            try {
                uri = new URL(flickUrl);
                con = (HttpURLConnection) uri.openConnection();
                con.setConnectTimeout(CONNECTION_TIMEOUT_VALUE);
                con.setReadTimeout(CONNECTION_TIMEOUT_VALUE);
                con.connect();
                inputStream = con.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            StringBuilder sBuilder = new StringBuilder();
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                sBuilder = new StringBuilder();
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    Log.d(LOG_TAG, "line " + line);
                    sBuilder.append(line + "\n");
                }
                inputStream.close();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error converting result " + e.toString());
            }
            return sBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jObject = new JSONObject(result);
                JSONArray photoList = (JSONArray) ((JSONObject) jObject.get("photos")).get("photo");
                totalPages = (int)((JSONObject) jObject.get("photos")).get("pages");
                Log.d(LOG_TAG, "JSON array size is" + photoList.length());
                for (int i = 0; i < photoList.length(); i++) {
                    JSONObject jsonObject = photoList.getJSONObject(i);
                    PhotoObject mPhoto = new PhotoObject();
                    mPhoto.setFarm(jsonObject.get("farm").toString());
                    mPhoto.setId(jsonObject.get("id").toString());
                    mPhoto.setServer(jsonObject.get("server").toString());
                    mPhoto.setSecret(jsonObject.get("secret").toString());
                    mPhoto.setPhotoUrl();
                    mPhotoObjectList.add(mPhoto);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error: " + e.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            loadNextBitmaps();
        }
    }

    public void loadNextBitmaps() {
        Log.d(LOG_TAG, " loadNextBitmaps " + lastPageLoaded);
        ArrayList<URL> newList = new ArrayList<URL>();
        //ArrayList<URL> newList = new ArrayList<URL>();
        for (int i = 0; i < mPhotoObjectList.size(); i++) {
            newList.add(mPhotoObjectList.get(i).getPhotoUrl());
        }
        boolean moreDataAvailable = true;
        if(totalPages == lastPageLoaded)
            moreDataAvailable = false;
        Log.d(LOG_TAG, "LoadBitMaps onPostExecute list is " + newList.toString() + " lastPageLoaded " +  lastPageLoaded);
        callback.loaded(newList, moreDataAvailable);
    }
}
