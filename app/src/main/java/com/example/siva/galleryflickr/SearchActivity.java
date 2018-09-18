package com.example.siva.galleryflickr;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ImageListActivity>>, SwipeRefreshLayout.OnRefreshListener{

    public static final String LOG_TAG = SearchActivity.class.getName();
    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=6f102c62f41998d151e5a1b48713cf13&format=json&nojsoncallback=1&extras=url_s&text=cat";

    /** Adapter for the list of earthquakes */
    private AdapterActivity mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    MaterialSearchView searchView;

    /*@NonNull
    @Override
    public android.support.v4.content.Loader<List<techList>> onCreateLoader(int id, @Nullable Bundle args) {
        // Create a new loader for the given URL
        // View loadingIndicator = findViewById(R.id.loading_indicator);
        //loadingIndicator.setVisibility(View.VISIBLE);
        return new techLoader(getActivity(), USGS_REQUEST_URL);
    }*/


    @Override
    public Loader<List<ImageListActivity>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL

        mAdapter.clear();

        //View loadingIndicator = getActivity().findViewById(R.id.loading_indicator);
        //loadingIndicator.setVisibility(View.VISIBLE);

        return new FlickrLoader(this, USGS_REQUEST_URL);
    }



    @Override
    public void onLoadFinished(Loader<List<ImageListActivity>> loader, List<ImageListActivity> images) {

        // Hide loading indicator because the data has been loaded
        /*View loadingIndicator = getActivity().findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);*/
        // Set empty state text to display "No earthquakes found."
        //mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();


        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (images != null && !images.isEmpty()) {
            mAdapter.addAll(images);
            //View loadingIndicator = getActivity().findViewById(R.id.loading_indicator);
            //loadingIndicator.setVisibility(View.GONE);
        }
    }


    @Override
    public void onLoaderReset(Loader<List<ImageListActivity>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }



    public SearchActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        // Find a reference to the {@link ListView} in the layout
        final ListView techListView = (ListView) findViewById(R.id.listview);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new AdapterActivity(this, new ArrayList<ImageListActivity>());

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        techListView.setAdapter(mAdapter);

        ViewCompat.setNestedScrollingEnabled(techListView, true);

        // Start the AsyncTask to fetch the earthquake data
        SearchActivity.SearchAsyncTask task = new SearchActivity.SearchAsyncTask();
        task.execute(USGS_REQUEST_URL);


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default_img data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        }
        else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            // View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
            //loadingIndicator.setVisibility(View.GONE);

            Context context = getApplicationContext();
            CharSequence text = "No Internet Connection";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            //mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        //final ProgressBar pbar = (ProgressBar) rootView.findViewById(R.id.loading_indicator); // Final so we can access it from the other thread
        //pbar.setVisibility(View.VISIBLE);


// Create a Handler instance on the main thread
       /*final Handler handler = new Handler();

// Create and start a new Thread
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(1000);
                }
                catch (Exception e) { } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                        // Set the View's visibility back on the main UI Thread
                        pbar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();*/



        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        techListView.setEmptyView(mEmptyStateTextView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Images");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                techListView.setAdapter(mAdapter);
                SearchActivity.SearchAsyncTask task = new SearchActivity.SearchAsyncTask();
                task.execute(USGS_REQUEST_URL);
            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onRefresh() {
        SearchActivity.SearchAsyncTask task = new SearchActivity.SearchAsyncTask();
        task.execute(USGS_REQUEST_URL);
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, List<ImageListActivity>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mAdapter.clear();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //ProgressBar pbar = (ProgressBar) getActivity().findViewById(R.id.loading_indicator); // Final so we can access it from the other thread
            //pbar.setVisibility(View.GONE);
        }

        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {@link ImageListActivity}s as the result.
         */
        @Override
        protected List<ImageListActivity> doInBackground(String... urls) {

            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<ImageListActivity> result = FlickrQueryUtils.fetchTechData(urls[0]);
            return result;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of earthquake data from a previous
         * query to USGS. Then we update the adapter with the new list of earthquakes,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<ImageListActivity> data) {
            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            if(mSwipeRefreshLayout.isRefreshing())
            {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
                //ProgressBar pbar = (ProgressBar) getActivity().findViewById(R.id.loading_indicator); // Final so we can access it from the other thread
                //pbar.setVisibility(View.GONE);
            }
        }
    }


}
