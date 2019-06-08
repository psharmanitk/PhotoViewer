package com.android.imageview.view;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.imageview.R;
import com.android.imageview.model.BitmapCache;
import com.android.imageview.model.LoadDataCallback;
import com.android.imageview.model.PhotoListBuilder;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoadDataCallback {
    private RecyclerView gridView;
    private ProgressBar mProgressBar;
    private ProgressBar moreLoadingBar;
    private EditText userInput;
    private Button go;
    private PhotoListBuilder photoBuilder;
    private InfiniteScroll infiniteScroll;
    private ImageAdapter myAdapter;
    private Parcelable savedState;
    private BitmapCache cache = null;
    private boolean loading;
    private Context mContext;

    private static String LOG_TAG = "PhotoViewer/MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        userInput = (EditText) findViewById(R.id.user_query);
        mProgressBar.setVisibility(View.GONE);
        moreLoadingBar = findViewById(R.id.loading_bar);
        moreLoadingBar.setVisibility(View.GONE);
        go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable inputText = userInput.getText();
                infiniteScroll.setDataAvailable(true);
                cache.clearCache();
                photoBuilder = new PhotoListBuilder(inputText.toString(), MainActivity.this, myAdapter);
                photoBuilder.loadObjects();
                moreLoadingBar.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                hideKeyboard();
            }
        });
        infiniteScroll = new InfiniteScroll();
        gridView = (RecyclerView) findViewById(R.id.grid_view);
        gridView.addOnScrollListener(infiniteScroll);
        gridView.setVisibility(View.GONE);
        gridView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            Log.d(LOG_TAG, "action bar not null");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        loading = false;
        cache = new BitmapCache(mContext, BitmapCache.getCacheSize(), this);
        myAdapter = new ImageAdapter(this, R.layout.item_layout, null, cache);
        gridView.setAdapter(myAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void loaded(List<URL> list, boolean moreDataAvailable) {
        Log.d(LOG_TAG, "loaded list size is "+ list.toString() +" moreDataAvailable "+ moreDataAvailable);
        //ImageAdapter myAdapter = new ImageAdapter(this, 0, list);
        infiniteScroll.setDataAvailable(moreDataAvailable);
        loading = false;
        int addedCount = 0;
        myAdapter.addItems(list);
        mProgressBar.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        if (savedState != null) {
            gridView.getLayoutManager().onRestoreInstanceState(savedState);
        }
        moreLoadingBar.setVisibility(View.GONE);
    }

    @Override
    synchronized public void bitmapLoaded(int index) {
/*        Log.d("priya", "bitmap loaded for index " + index);
        myAdapter.notifyItemChanged(index);*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class InfiniteScroll extends RecyclerView.OnScrollListener{
        private int prevLastItemIndex = 0;
        private boolean isDataAvailable = false;
        /**
         * The total number of items in the data set after the last load
         */
        private int mPreviousTotal = 0;
        /**
         * True if we are still waiting for the last set of data to load.
         */
        private boolean mLoading = true;
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            Log.d(LOG_TAG, " onScrolled dx and dy are "+ dx + " " + dy);
            Log.d(LOG_TAG, " firstVisibleItem visibleItemCount and totalItemCount are "+ firstVisibleItem + " " + visibleItemCount + " "+totalItemCount);
            if (mLoading) {
                if (totalItemCount > mPreviousTotal) {
                    Log.d(LOG_TAG, " onScrolled dx and dy are "+ mLoading + mPreviousTotal);
                    mLoading = false;
                    mPreviousTotal = totalItemCount;
                }
            }
            int visibleThreshold = 5;
            if (!mLoading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                moreLoadingBar.setVisibility(View.VISIBLE);
                savedState = recyclerView.getLayoutManager().onSaveInstanceState();
                photoBuilder.loadObjects();
                mLoading = true;
            }
        }
/* @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Log.d(LOG_TAG, "onScroll firstVisibleItem "+ firstVisibleItem +" visibleItemCount " + " totalItemCount "+ totalItemCount);
            if (gridView.getLastVisiblePosition() + 1 == totalItemCount && !loading) {
                loading = true;
                if (isDataAvailable) {
                    Log.d(LOG_TAG, "onScroll Data Available");
                    photoBuilder.loadNextBitmaps();
                }
            } else {
                loading = false;
            }
        }*/

        public void setDataAvailable(boolean dataAvailable) {
            Log.d(LOG_TAG, "Infinite Scroll setDataAvailable "+dataAvailable);
            isDataAvailable = dataAvailable;
        }
    }
}
