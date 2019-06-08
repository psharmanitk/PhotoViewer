package com.android.imageview.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/*
Class to create the photo url to load image
http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg
* */
public class PhotoObject {
    String farm;
    String server;
    String id;
    String secret;
    URL photoUrl;
    Bitmap bitmap;
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public PhotoObject() {
        this.farm = farm;
        this.server = server;
        this.id = id;
        this.secret = secret;
    }

    //http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg
    public void setPhotoUrl() {
        try {
            this.photoUrl = new URL("https://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg");
        } catch (MalformedURLException e) {
            Log.d("PhotoObject", "exception "+e.toString());
        }
    }

    public URL getPhotoUrl() {
        return photoUrl;
    }
}
