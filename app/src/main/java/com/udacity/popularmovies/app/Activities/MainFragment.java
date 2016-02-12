package com.udacity.popularmovies.app.Activities;

import android.database.Cursor;
import android.os.Bundle;
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

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.adapter.MyGridCursorAdapter;
import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.db.tables.MoviesTable;

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        moviesAdapter = new MyGridCursorAdapter(getActivity(), null);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(moviesAdapter);

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
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public void restartCursorLoader() {
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String movieId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String pref = ApiCalls.getSettings(getActivity());

        //pref is favourites
        if (pref == getActivity().getString(R.string.pref_movies_label_fav)) {

            return new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                    ApiCalls.FAV_SELECT, new String[]{ApiCalls.FAV_SELECT_ARGS_true}, null);

            //pref is popular movies
        } else if (pref == getActivity().getString(R.string.pref_movies_label_popular_entry)) {

            return new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                    null, null, ApiCalls.POPULARITY_SORT_ORDER);

            //pref is high rating movies
        } else if (pref == getActivity().getString(R.string.pref_movies_label_rating_entry)) {

            return new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                    null, null, ApiCalls.RATING_SORT_ORDER);

        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        moviesAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            recyclerView.smoothScrollToPosition(mPosition);
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesAdapter.swapCursor(null);
    }
}
