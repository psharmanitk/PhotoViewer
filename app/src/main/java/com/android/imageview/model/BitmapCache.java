package com.android.imageview.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.imageview.R;
import com.android.imageview.view.ImageAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BitmapCache extends LruCache<String, Bitmap> {
    private static final String LOG_TAG = "PhotoViewer/BitmapCache";
    private ExecutorService executorService;
    private Context mContext;
    private ImageAdapter adapterHandle;
    Handler myHandler = new Handler(Looper.getMainLooper());
    LoadDataCallback dataCallback;

    public BitmapCache(Context mContext, int maxSize, LoadDataCallback dataCallback) {
        super(maxSize);
        this.mContext = mContext;
        executorService = Executors.newFixedThreadPool(12);
        this.dataCallback = dataCallback;
    }
    public static int getCacheSize() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // use 1/8th of the available memory for this memory cache.
        return maxMemory / 8;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount() / 1024;
    }

    public Bitmap getBitmap(String key) {
        return this.get(key);
    }

    public void setBitmapOrDownload(final String key, ImageAdapter.ImageViewHolder holder) {
        if (hasBitmap(key)) {
            Log.d(LOG_TAG, "setBitmapOrDownload in hash key is and size is " + key + "width :" + getBitmap(key).getWidth() + " height :"+ getBitmap(key).getHeight());
            if(getBitmap(key) != null)
                holder.setImageView(getBitmap(key));
            else {
                Bitmap bitmap = (Bitmap) BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher_background);
                holder.setImageView(bitmap);
            }
        } else {
            Log.d(LOG_TAG, "setBitmapOrDownload not in hash " + holder.getUrl());
            executorService.submit(new PhotoLoader(mContext, holder, key));
            //Thread t = new PhotoLoader(holder, key);
        }
    }

    public boolean hasBitmap(String key) {
        return getBitmap(key) != null;
    }

    class PhotoLoader implements Runnable {
        private ImageAdapter.ImageViewHolder holder;
        private URL url;
        private String key;
        private Context mContext;

        PhotoLoader(Context ctx, ImageAdapter.ImageViewHolder holder, String key) {
            this.holder = holder;
            this.key = key;
            this.mContext = ctx;
        }

        @Override
        public void run() {
            Log.d(LOG_TAG, " PhotoLoader run " + holder.getUrl() + " key " + key);
            try {
                HttpURLConnection con = (HttpURLConnection) holder.getUrl().openConnection();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                //options.inSampleSize = 2;
                options.inScaled = true;
                Bitmap bitmap = BitmapFactory.decodeStream(con.getInputStream(), null, options);
                //Bitmap bitmap = BitmapFactory.decodeStream(con.getInputStream());
                //iv.setImageView(bitmap);
                if (bitmap == null)
                    return;
                put(key, bitmap);
                BitmapDisplayer bd = new BitmapDisplayer(mContext, holder, bitmap);
/*                Thread displayThread = new Thread(bd);
                displayThread.start();*/
                myHandler.post(bd);
            } catch (UnknownHostException e) {
                Log.d(LOG_TAG, "Unknown host continue");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class BitmapDisplayer implements Runnable {
        private ImageAdapter.ImageViewHolder holder;
        private Bitmap bitmap;
        private Context mContext;

        BitmapDisplayer(Context ctx, ImageAdapter.ImageViewHolder holder, Bitmap bitmap) {
            //Log.d(LOG_TAG, "BitmapDisplayer run " + holder.toString());
            this.holder = holder;
            this.bitmap = bitmap;
            this.mContext = ctx;
        }

        @Override
        public void run() {
            Log.d(LOG_TAG, "BitmapDisplayer run " + holder.toString());
            if (bitmap == null) {
                Bitmap bitmap = (Bitmap) BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher_background);
                holder.setImageView(bitmap);
            } else
                holder.setImageView(bitmap);
            Log.d("priya", "holder updated of index "+ holder.getLayoutPosition());
        }
    }
}

