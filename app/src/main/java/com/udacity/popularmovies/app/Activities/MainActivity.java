package com.udacity.popularmovies.app.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.api.ApiCalls;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {


    private String mOrder;
    //    private boolean mTwoPane;
    public static boolean mTwoPane;
    public static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mOrder = ApiCalls.getSettings(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movies_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
//            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.movies_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
//                        .commit();
//            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String order = ApiCalls.getSettings(this);


        // update if setting changes
        if (order != null && !order.equals(mOrder)) {
            MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);

            if (!isNetworkAvailable()) {
                if (order.equals(getString(R.string.pref_movies_label_fav))) {
                    mainFragment.connectivity.setVisibility(View.GONE);
                } else {
                    mainFragment.connectivity.setVisibility(View.VISIBLE);
                }
            } else {
                mainFragment.connectivity.setVisibility(View.GONE);
            }
            // update the movies in main fragment
            if (mainFragment != null) {
                mainFragment.restartLoader();
            }
            /*********************************************************/
            if (findViewById(R.id.movies_detail_container) != null) {

                DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().
                        findFragmentByTag(DETAILFRAGMENT_TAG);

                if (detailFragment != null)
                    getSupportFragmentManager().beginTransaction().remove
                            (detailFragment).commit();

                // update the location in our second pane using the fragment manager
//            if (detailFragment != null) {
//                    detailFragment.restartLoader();


            }
        }
        mOrder = order;
    }

    /**
     * figure out if there is internet connection
     *
     * @return true if there is connection
     */

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onItemSelected(String movieId, Parcelable movieEntry) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            if (ApiCalls.getSettings(this).equals(getString(R.string.pref_movies_label_fav))) {
                args.putString(DetailedActivity.MOVIE_ID_TAG, movieId);
            } else {
                args.putParcelable(DetailedActivity.MOVIE_ID_TAG, movieEntry);
            }
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailedActivity.class);
            if (ApiCalls.getSettings(this).equals(getString(R.string.pref_movies_label_fav))) {
                intent.putExtra(DetailedActivity.MOVIE_ID_TAG, movieId);
            } else {
                intent.putExtra(DetailedActivity.MOVIE_ID_TAG, movieEntry);

            }
            startActivity(intent);
        }

    }

}
