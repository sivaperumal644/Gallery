package com.example.siva.galleryflickr;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by siva on 16/9/18.
 */

public class FlickrLoader extends AsyncTaskLoader<List<ImageListActivity>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ImageListActivity.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link FlickrLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public FlickrLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<ImageListActivity> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<ImageListActivity> book = FlickrQueryUtils.fetchTechData(mUrl);
        return book;
    }

}
