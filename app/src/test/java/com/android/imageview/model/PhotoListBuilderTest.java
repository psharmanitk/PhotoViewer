package com.android.imageview.model;

import android.media.Image;
import android.os.AsyncTask;

import com.android.imageview.R;
import com.android.imageview.view.ImageAdapter;
import com.android.imageview.view.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Nested;
import org.powermock.api.mockito.PowerMockito;

public class PhotoListBuilderTest {
    private PhotoListBuilder listBuilder;

    @Before
    public void setUp() throws Exception {
        MainActivity activity = PowerMockito.mock(MainActivity.class);
        LoadDataCallback callback = PowerMockito.mock(LoadDataCallback.class);
        BitmapCache cache = new BitmapCache(activity.getApplicationContext(), BitmapCache.getCacheSize(), callback);
        ImageAdapter myAdapter = new ImageAdapter(activity.getApplicationContext(), R.layout.item_layout, null, cache );
        GetObjectOnFlickerTask getObjectOnFlickerTask = new GetObjectOnFlickerTask();
        listBuilder = new PhotoListBuilder("Kittens", callback, myAdapter);
    }

    @Test
    public void loadObjects() {
        listBuilder.loadObjects();
    }

    @Test
    public void loadNextBitmaps() {
    }

    @Nested
    class GetObjectOnFlickerTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }
}