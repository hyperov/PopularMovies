package com.udacity.popularmovies.app.Activities;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.cursoradapter.ReviewAdapter;
import com.udacity.popularmovies.app.cursoradapter.TrailerAdapter;
import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.db.tables.MoviesEntry;
import com.udacity.popularmovies.app.db.tables.MoviesTable;
import com.udacity.popularmovies.app.db.tables.ReviewsEntry;
import com.udacity.popularmovies.app.db.tables.ReviewsTable;
import com.udacity.popularmovies.app.db.tables.TrailersEntry;
import com.udacity.popularmovies.app.db.tables.TrailersTable;
import com.udacity.popularmovies.app.handler.JsonHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final int DETAIL_MOVIE_LOADER = 0;
    private static final int DETAIL_REVIEW_LOADER = 1;
    private static final int DETAIL_TRAILER_LOADER = 2;

    TextView title, plot, release, rating;
    ImageView poster;
    ListView trailers, reviews;

    public String movie_id;

    public TrailerAdapter trailerAdapter;
    public ReviewAdapter reviewAdapter;

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

        trailers = (ListView) rootView.findViewById(R.id.trailers);
        reviews = (ListView) rootView.findViewById(R.id.reviews);

        reviewAdapter = new ReviewAdapter(getActivity(), null, 0);
        trailerAdapter = new TrailerAdapter(getActivity(), null, 0);

        trailers.setAdapter(trailerAdapter);
        reviews.setAdapter(reviewAdapter);
        trailers.setOnItemClickListener(this);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_REVIEW_LOADER, null, this);
        getLoaderManager().initLoader(DETAIL_TRAILER_LOADER, null, this);
        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void restartCursorLoader() {
        getLoaderManager().restartLoader(MainFragment.MOVIES_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        JsonHandler handler = new JsonHandler();
        String pref = ApiCalls.getSettings(getActivity());
        CursorLoader cursorLoader = null;

        switch (id) {
            case DETAIL_MOVIE_LOADER:

                //pref is favourites
                if (pref == getActivity().getString(R.string.pref_movies_label_fav)) {

                    cursorLoader = new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                            ApiCalls.FAV_SELECT + " AND " + MoviesTable.FIELD_MOVIE_ID + "=?",
                            new String[]{ApiCalls.FAV_SELECT_ARGS_true, movie_id}, null);

                    //pref is popular movies

                } else if (pref == getActivity().getString(R.string.pref_movies_label_popular_entry)) {

                    cursorLoader = new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                            MoviesTable.FIELD_MOVIE_ID + "=?", new String[]{movie_id}, ApiCalls.POPULARITY_SORT_ORDER);

                    //pref is high rating movies
                } else if (pref == getActivity().getString(R.string.pref_movies_label_rating_entry)) {

                    cursorLoader = new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                            MoviesTable.FIELD_MOVIE_ID + "=?", new String[]{movie_id}, ApiCalls.RATING_SORT_ORDER);

                }


                break;
            case DETAIL_REVIEW_LOADER:
                /************************************************************/
                String jsonReviews = handler.getJsonString(ApiCalls.getApiCallReviews());
                JSONObject reviewObject = null;
                try {
                    reviewObject = new JSONObject(jsonReviews);
                    JSONArray reviewResults = reviewObject.getJSONArray("results");

                    Vector<ContentValues> reviewsVector = new Vector<ContentValues>(reviewResults.length());

                    for (int k = 0; k < reviewResults.length(); k++) {
                        JSONObject reviewItem = reviewResults.getJSONObject(k);


                        ReviewsEntry review = new ReviewsEntry(ApiCalls.API_CALL_MOVIE_ID
                                , reviewItem.getString("author"), reviewItem.getString("content"));

                        //add reviews content values to vector
                        reviewsVector.add(ReviewsTable.getContentValues(review, false));
                    }

                    if (reviewsVector.size() > 0) {
                        ContentValues[] cvrArray = new ContentValues[reviewsVector.size()];
                        reviewsVector.toArray(cvrArray);
                        getContext().getContentResolver().bulkInsert(ReviewsTable.CONTENT_URI, cvrArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*******************************************************************************/

                cursorLoader = new CursorLoader(getActivity(), ReviewsTable.CONTENT_URI, null,
                        MoviesTable.FIELD_MOVIE_ID + "=?", new String[]{movie_id}, null);
                break;
            case DETAIL_TRAILER_LOADER:
                /****************************************************************************/
                String jsonTrailers = handler.getJsonString(ApiCalls.getApiCallTrailers());
                JSONObject trailerObject = null;
                try {
                    trailerObject = new JSONObject(jsonTrailers);
                    JSONArray trailerResults = trailerObject.getJSONArray("results");

                    Vector<ContentValues> trailersVector = new Vector<ContentValues>(trailerResults.length());

                    for (int v = 0; v < trailerResults.length(); v++) {
                        JSONObject trailerItem = trailerResults.getJSONObject(v);


                        TrailersEntry trailer = new TrailersEntry(ApiCalls.API_CALL_MOVIE_ID
                                , trailerItem.getString("key"), trailerItem.getString("name"));

                        //add reviews content values to vector
                        trailersVector.add(TrailersTable.getContentValues(trailer, false));
                    }

                    if (trailersVector.size() > 0) {
                        ContentValues[] cvtArray = new ContentValues[trailersVector.size()];
                        trailersVector.toArray(cvtArray);
                        getContext().getContentResolver().bulkInsert(TrailersTable.CONTENT_URI, cvtArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /**************************************************************************************/

                cursorLoader = new CursorLoader(getActivity(), TrailersTable.CONTENT_URI, null,
                        MoviesTable.FIELD_MOVIE_ID + "=?", new String[]{movie_id}, null);

                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case DETAIL_MOVIE_LOADER:
                MoviesEntry movie = MoviesTable.getRow(data, true);

                title.setText(movie.column_movie_name);
                plot.setText(movie.column_plot);
                release.setText(movie.column_release_date);
                rating.setText(movie.column_user_rating + "");

                Picasso.with(getActivity()).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + movie.column_poster).into(poster);
                break;
            case DETAIL_REVIEW_LOADER:
                reviewAdapter.swapCursor(data);
                break;
            case DETAIL_TRAILER_LOADER:
                trailerAdapter.swapCursor(data);
                break;
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == DETAIL_REVIEW_LOADER)
            reviewAdapter.swapCursor(null);
        if (loader.getId() == DETAIL_TRAILER_LOADER)
            trailerAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        TrailersEntry trailersEntry = TrailersTable.getRow(cursor, true);

        String trailerFullUrl = ApiCalls.YOUTUBE_BASE_URL + trailersEntry.column_trailer;

    }
}
