package com.udacity.popularmovies.app.Activities;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.db.tables.MoviesEntry;
import com.udacity.popularmovies.app.db.tables.MoviesTable;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    TextView title, plot, release, rating;
    ImageView poster;
    public String movie_id;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            movie_id = arguments.getString(DetailedActivity.MOVIE_ID_TAG);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        title = (TextView) rootView.findViewById(R.id.title1);
        plot = (TextView) rootView.findViewById(R.id.plotsubject);
        release = (TextView) rootView.findViewById(R.id.releasedate);
        rating = (TextView) rootView.findViewById(R.id.ratingnumber);
        poster = (ImageView) rootView.findViewById(R.id.imageView);

        return rootView;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(MainFragment.MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void restartCursorLoader() {
        getLoaderManager().restartLoader(MainFragment.MOVIES_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String pref = ApiCalls.getSettings(getActivity());

        //pref is favourites
        if (pref == getActivity().getString(R.string.pref_movies_label_fav)) {

            return new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                    ApiCalls.FAV_SELECT, new String[]{ApiCalls.FAV_SELECT_ARGS}, null);

            //pref is popular movies
        } else if (pref == getActivity().getString(R.string.pref_movies_label_popular_entry)) {

            return new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                    MoviesTable.FIELD_MOVIE_ID, new String[]{movie_id}, ApiCalls.POPULARITY_SORT_ORDER);

            //pref is high rating movies
        } else if (pref == getActivity().getString(R.string.pref_movies_label_rating_entry)) {

            return new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                    MoviesTable.FIELD_MOVIE_ID, new String[]{movie_id}, ApiCalls.RATING_SORT_ORDER);

        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        MoviesEntry movie = MoviesTable.getRow(data, true);

        title.setText(movie.column_movie_name);
        plot.setText(movie.column_plot);
        release.setText(movie.column_release_date);
        rating.setText(movie.column_user_rating + "");

        Picasso.with(getActivity()).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + movie.column_poster).into(poster);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
