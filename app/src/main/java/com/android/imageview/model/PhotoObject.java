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


    //http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg
    public void setPhotoUrl() throws MalformedURLException {
        this.photoUrl = new URL("https://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg");
    }

    public URL getPhotoUrl() {
        return photoUrl;
    }
}
