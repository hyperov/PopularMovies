package com.udacity.popularmovies.app.Activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.adapter.MyGridCursorAdapter;
import com.udacity.popularmovies.app.db.tables.MoviesEntry;
import com.udacity.popularmovies.app.db.tables.MoviesTable;

/**
 * Created by DELL I7 on 1/28/2016.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private RecyclerView recyclerView;
    private int mPosition = -1;

    private static final String SELECTED_KEY = "selected_position";

    //id for sursor loader
    public static final int MOVIES_LOADER = 0;
    //adapter to use cursor adapter with recyclerView
    private MyGridCursorAdapter moviesAdapter;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        moviesAdapter = new MyGridCursorAdapter(getContext(), null);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((position + 1) / 3 == 0) {
                    return 1;
                } else {
                    return 2;
                }

            }
        });
//        RecyclerView.ItemDecoration itemDecoration=new
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(moviesAdapter);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void restartCursorLoader() {
        getLoaderManager().restartLoader(MainFragment.MOVIES_LOADER, null, this);
    }

    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getContext(), MoviesTable.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviesAdapter.swapCursor(data);
        MoviesEntry movie = MoviesTable.getRow(data, true);
        recyclerView.smoothScrollToPosition(data.getPosition());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesAdapter.swapCursor(null);
    }
}
