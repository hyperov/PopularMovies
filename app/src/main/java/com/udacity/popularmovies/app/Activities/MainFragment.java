package com.udacity.popularmovies.app.activities;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.arrayadapter.MoviesArrayAdapter;
import com.udacity.popularmovies.app.cursoradapter.MyGridCursorAdapter;
import com.udacity.popularmovies.app.db.tables.MoviesEntry;
import com.udacity.popularmovies.app.db.tables.MoviesTable;
import com.udacity.popularmovies.app.loader.MainFragmentLoaderMovies;

import java.util.ArrayList;

/**
 * Created by DELL I7 on 1/28/2016.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private RecyclerView recyclerView;
    public static int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    //id for sursor loader
    public static final int MOVIES_LOADER = 0;
    //adapter to use cursor adapter with recyclerView
    public MyGridCursorAdapter moviesAdapter;

    //array adapter if not favourite
    public MoviesArrayAdapter moviesArrayAdapter;

    //no connectivity
    TextView connectivity;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        connectivity = (TextView) rootView.findViewById(R.id.no_connection);

        if (!isNetworkAvailable()) {

            if (!ApiCalls.getSettings(getContext()).equals(getContext().getString(R.string.pref_movies_label_fav))) {
                connectivity.setVisibility(View.VISIBLE);
            } else {
                connectivity.setVisibility(View.GONE);
            }
        }


        moviesAdapter = new MyGridCursorAdapter(getActivity(), null);
        moviesArrayAdapter = new MoviesArrayAdapter(new ArrayList<MoviesEntry>(), getActivity());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(gridLayoutManager);


        if (ApiCalls.getSettings(getContext()).equals(getContext().getString(R.string.pref_movies_label_fav))) {
            //favourites
            recyclerView.setAdapter(moviesAdapter);
        } else {
            recyclerView.setAdapter(moviesArrayAdapter);
            recyclerView.setHasFixedSize(true);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (ApiCalls.getSettings(getActivity()).equals(getString(R.string.pref_movies_label_fav))) {
            getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        } else {
            if (isNetworkAvailable())
                getLoaderManager().initLoader(1, null, moviesLoaderCallbacks).forceLoad();

        }
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public void restartLoader() {
        if (ApiCalls.getSettings(getActivity()).equals(getString(R.string.pref_movies_label_fav))) {
            getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
        } else {
            if (isNetworkAvailable())
                getLoaderManager().restartLoader(1, null, moviesLoaderCallbacks).forceLoad();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */

        void onItemSelected(String movieId, Parcelable movieEntry);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (ApiCalls.getSettings(getActivity()).equals(getString(R.string.pref_movies_label_fav))) {
            moviesAdapter.swapCursor(data);
            recyclerView.setAdapter(moviesAdapter);
            if (mPosition != ListView.INVALID_POSITION) {
                // If we don't need to restart the loader, and there's a desired position to restore
                // to, do so now.
                recyclerView.smoothScrollToPosition(mPosition);
            }
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Cursor old = moviesAdapter.swapCursor(null);
        if (old != null)
            old.close();
//        getLoaderManager().destroyLoader(loader.getId());
    }

    public LoaderManager.LoaderCallbacks<ArrayList<MoviesEntry>> moviesLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<ArrayList<MoviesEntry>>() {
                @Override
                public Loader<ArrayList<MoviesEntry>> onCreateLoader(int id, Bundle args) {
                    return new MainFragmentLoaderMovies(getActivity());
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<MoviesEntry>> loader, ArrayList<MoviesEntry> data) {
                    moviesArrayAdapter = new MoviesArrayAdapter(data, getActivity());
                    recyclerView.setAdapter(moviesArrayAdapter);
                    if (mPosition != ListView.INVALID_POSITION) {
                        // If we don't need to restart the loader, and there's a desired position to restore
                        // to, do so now.
                        recyclerView.smoothScrollToPosition(mPosition);
                    }

                }

                @Override
                public void onLoaderReset(Loader<ArrayList<MoviesEntry>> loader) {

                    recyclerView.setAdapter(null);

                }
            };
}
