package com.android.imageview.model;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.List;

/* Interface to be called when Photos are loaded from url*/
public interface LoadDataCallback {
    void loaded(List<URL> list, boolean moreDataAvailable);
    void bitmapLoaded(int index);
}
