package com.android.imageview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.imageview.R;
import com.android.imageview.model.BitmapCache;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private static String LOG_TAG = "PhotoViewer/ImageAdapter";
    private final LayoutInflater mInflater;
    private Context mContext;
    private int resource;
    private static ArrayList<URL> mPhotoUrlList;
    private BitmapCache cache;

    public ImageAdapter(Context context, int resource, List objects, BitmapCache cache) {
        super();
        Log.d(LOG_TAG, " ImageAdapter ");
        mContext = context;
        mPhotoUrlList = new ArrayList<>();
        this.resource = resource;
        this.cache = cache;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(LOG_TAG, " onCreateViewHolder " + i);
        View imageView = mInflater.inflate(resource, viewGroup, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(imageView, mPhotoUrlList.get(i));
        //imageViewHolder.setIsRecyclable(true);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int position) {
        Log.d(LOG_TAG, " onBindViewHolder " + imageViewHolder.getUrl());
        imageViewHolder.setUrl(mPhotoUrlList.get(position));
        cache.setBitmapOrDownload( mPhotoUrlList.get(position).toString(), imageViewHolder);
    }

    @Override
    public int getItemCount() {
        Log.d("priya", "getItemCount");
        int size = mPhotoUrlList.size();
        //int size = mPhotoObjectList.size( );
        //Log.d(LOG_TAG, " getItemCount " + size);
        return size;
    }

    synchronized public void addItems(List<URL> list) {
        Log.d(LOG_TAG, "addItems "+ list.toString());
        //mPhotoObjectList.addAll(list);
        mPhotoUrlList.addAll(list);
        notifyDataSetChanged();
        notifyAll();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private URL url;

        public ImageViewHolder(@NonNull View itemView, URL url) {
            super(itemView);
            this.url = url;
            imageView = itemView.findViewById(R.id.image);
        }

        public void setUrl(URL url) {
            this.url = url;
        }

        public URL getUrl() {
            return url;
        }

        public void setImageView(Bitmap image) {
            imageView.setImageBitmap(image);
        }
    }
}
