package com.udacity.popularmovies.app.activities;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.arrayadapter.ReviewsArrayAdapter;
import com.udacity.popularmovies.app.arrayadapter.TrailersArrayAdapter;
import com.udacity.popularmovies.app.cursoradapter.ReviewCursorAdapter;
import com.udacity.popularmovies.app.cursoradapter.TrailerCursorAdapter;
import com.udacity.popularmovies.app.db.tables.MoviesEntry;
import com.udacity.popularmovies.app.db.tables.MoviesTable;
import com.udacity.popularmovies.app.db.tables.ReviewsEntry;
import com.udacity.popularmovies.app.db.tables.ReviewsTable;
import com.udacity.popularmovies.app.db.tables.TrailersEntry;
import com.udacity.popularmovies.app.db.tables.TrailersTable;
import com.udacity.popularmovies.app.loader.DetailFragmentLoaderReviews;
import com.udacity.popularmovies.app.loader.DetailFragmentLoaderTrailers;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final int DETAIL_MOVIE_LOADER = 3;
    private static final int DETAIL_REVIEW_LOADER = 1;
    private static final int DETAIL_TRAILER_LOADER = 2;

    public static boolean loaderFinished = false;

    TextView title, plot, release, rating, trailerTv, reviewTv;
    ImageView poster;
    ListView trailers, reviews;
    FloatingActionButton fav;

    public String movie_id;

    private ShareActionProvider mShareActionProvider;

    public static String trailerIntentText;

    public TrailerCursorAdapter trailerCursorAdapter;
    public ReviewCursorAdapter reviewCursorAdapter;

    public TrailersArrayAdapter trailersArrayAdapter;
    public ReviewsArrayAdapter reviewsArrayAdapter;

    MoviesEntry movie;

    //insert in fav
    ArrayList<ReviewsEntry> reviewsEntries;
    ArrayList<TrailersEntry> trailersEntries;


    public DetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent(trailerIntentText));
        }
    }

    private Intent createShareForecastIntent(String text) {
        Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain").setText(ApiCalls.YOUTUBE_BASE_URL + text).getIntent();
        return intent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            if (ApiCalls.getSettings(getActivity()).equals(getString(R.string.pref_movies_label_fav))) {
                movie_id = arguments.getString(DetailedActivity.MOVIE_ID_TAG);
            } else {
                movie = arguments.getParcelable(DetailedActivity.MOVIE_ID_TAG);
            }


        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        title = (TextView) rootView.findViewById(R.id.title1);
        plot = (TextView) rootView.findViewById(R.id.plotsubject);
        release = (TextView) rootView.findViewById(R.id.releasedate);
        rating = (TextView) rootView.findViewById(R.id.ratingnumber);
        poster = (ImageView) rootView.findViewById(R.id.imageView);

        trailerTv = (TextView) rootView.findViewById(R.id.trailer);
        reviewTv = (TextView) rootView.findViewById(R.id.review);

        fav = (FloatingActionButton) rootView.findViewById(R.id.fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoviesToFav();
            }
        });

        trailers = (ListView) rootView.findViewById(R.id.trailers);
        reviews = (ListView) rootView.findViewById(R.id.reviews);

        reviewCursorAdapter = new ReviewCursorAdapter(getActivity(), null, 0);
        trailerCursorAdapter = new TrailerCursorAdapter(getActivity(), null, 0);

        trailersArrayAdapter = new TrailersArrayAdapter(getActivity(),
                R.layout.trailer_item, new ArrayList<TrailersEntry>());

        reviewsArrayAdapter = new ReviewsArrayAdapter(getActivity(),
                R.layout.review_item, new ArrayList<ReviewsEntry>());

        if (ApiCalls.getSettings(getActivity()).equals(getString(R.string.pref_movies_label_fav))) {
            //favourites
            trailers.setAdapter(trailerCursorAdapter);
            reviews.setAdapter(reviewCursorAdapter);

        } else {
            trailers.setAdapter(trailersArrayAdapter);
            reviews.setAdapter(reviewsArrayAdapter);

            if (movie != null) {
                title.setText(movie.column_movie_name);
                plot.setText(movie.column_plot);
                release.setText(movie.column_release_date);
                rating.setText(movie.column_user_rating + "");

                Picasso.with(getActivity()).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + movie.column_poster).into(poster);
            }
        }


        trailers.setOnItemClickListener(this);

        return rootView;
    }

    public void addMoviesToFav() {
        boolean movieExist = false;
        ContentValues[] cvArray;
        Cursor movieCursor = getActivity().getContentResolver().query(MoviesTable.CONTENT_URI,
                null, MoviesTable.FIELD_MOVIE_ID + "=?",
                new String[]{movie.column_movie_id}, null);
        if (movieCursor.moveToFirst()) {
            movieExist = true;

        }
        movieCursor.close();
        if (movieExist) {

            /**************delete movieItem,trailers,reviews*************/
            getActivity().getContentResolver().delete(MoviesTable.CONTENT_URI,
                    MoviesTable.FIELD_MOVIE_ID + "=?", new String[]{movie.column_movie_id});

            getActivity().getContentResolver().delete(ReviewsTable.CONTENT_URI,
                    MoviesTable.FIELD_MOVIE_ID + "=?", new String[]{movie.column_movie_id});

            getActivity().getContentResolver().delete(TrailersTable.CONTENT_URI,
                    MoviesTable.FIELD_MOVIE_ID + "=?", new String[]{movie.column_movie_id});

            Toast.makeText(getActivity(), "deleted from Favourites", Toast.LENGTH_SHORT).show();

            if (ApiCalls.getSettings(getActivity()).equals(getString(R.string.pref_movies_label_fav))) {
                if (!MainActivity.mTwoPane) {
                    getActivity().finish();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                }
            }

        } else {

            /**********insert movieItem,trailers,reviews*****/

            //movieIem :
            ContentValues cv = MoviesTable.getContentValues(movie, false);
            getActivity().getContentResolver().insert(MoviesTable.CONTENT_URI, cv);

            if (reviewsEntries != null) {
                //reviews
                ArrayList<ContentValues> reviewsContentValues = new ArrayList<>();
                for (int i = 0; i < reviewsEntries.size(); i++) {
                    ReviewsEntry reviewItem = reviewsEntries.get(i);
                    ContentValues reviewValues = ReviewsTable.getContentValues(reviewItem, false);
                    reviewsContentValues.add(reviewValues);
                }

                cvArray = new ContentValues[reviewsContentValues.size()];
                reviewsContentValues.toArray(cvArray);
                getActivity().getContentResolver().bulkInsert(ReviewsTable.CONTENT_URI, cvArray);
            }

            if (trailersEntries != null) {
                //trailers
                ArrayList<ContentValues> trailersContentValues = new ArrayList<>();
                for (int i = 0; i < trailersEntries.size(); i++) {
                    TrailersEntry trailerItem = trailersEntries.get(i);
                    ContentValues trailerValues = TrailersTable.getContentValues(trailerItem, false);
                    trailersContentValues.add(trailerValues);
                }

                cvArray = new ContentValues[trailersContentValues.size()];
                trailersContentValues.toArray(cvArray);
                getActivity().getContentResolver().bulkInsert(TrailersTable.CONTENT_URI, cvArray);
            }
            Toast.makeText(getActivity(), "added to Favourites", Toast.LENGTH_SHORT).show();


        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (ApiCalls.getSettings(getActivity()).equals(getString(R.string.pref_movies_label_fav))) {
            if (movie_id != null) {
                getLoaderManager().initLoader(DETAIL_REVIEW_LOADER, null, this);
                getLoaderManager().initLoader(DETAIL_TRAILER_LOADER, null, this);
                getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);
            }
        } else {
            if (movie != null) {
                getLoaderManager().initLoader(DETAIL_REVIEW_LOADER, null, reviewsLoaderCallbacks).forceLoad();
                getLoaderManager().initLoader(DETAIL_TRAILER_LOADER, null, trailersLoaderCallbacks).forceLoad();
            }
        }
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent(trailerIntentText));
        }
        super.onActivityCreated(savedInstanceState);
    }

//    public void restartLoader() {
//        if (ApiCalls.getSettings(getActivity()).equals(getString(R.string.pref_movies_label_fav))) {
//            getLoaderManager().restartLoader(DETAIL_MOVIE_LOADER, null, this);
//            getLoaderManager().restartLoader(DETAIL_REVIEW_LOADER, null, this);
//            getLoaderManager().restartLoader(DETAIL_TRAILER_LOADER, null, this);
//        } else {
//            if (isNetworkAvailable()) {
//                getLoaderManager().restartLoader(DETAIL_REVIEW_LOADER, null, reviewsLoaderCallbacks).forceLoad();
//                getLoaderManager().restartLoader(DETAIL_TRAILER_LOADER, null, trailersLoaderCallbacks).forceLoad();
//            }
//
//        }
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.movies_detail_container, new DetailFragment(), MainActivity.DETAILFRAGMENT_TAG)
//                .commit();
//
//    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (movie_id != null) {
            CursorLoader cursorLoader = null;
            switch (id) {
                case DETAIL_MOVIE_LOADER:


                    cursorLoader = new CursorLoader(getActivity(), MoviesTable.CONTENT_URI, null,
                            MoviesTable.FIELD_MOVIE_ID + "=?",
                            new String[]{movie_id}, null);

                    break;
                case DETAIL_REVIEW_LOADER:

                    cursorLoader = new CursorLoader(getActivity(), ReviewsTable.CONTENT_URI, null,
                            MoviesTable.FIELD_MOVIE_ID + "=?", new String[]{movie_id}, null);
                    break;
                case DETAIL_TRAILER_LOADER:

                    cursorLoader = new CursorLoader(getActivity(), TrailersTable.CONTENT_URI, null,
                            MoviesTable.FIELD_MOVIE_ID + "=?", new String[]{movie_id}, null);

                    break;
            }

            return cursorLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null) {
            loaderFinished = true;

            switch (loader.getId()) {
                case DETAIL_MOVIE_LOADER:
                    movie = MoviesTable.getRow(data, true);

                    title.setText(movie.column_movie_name);
                    plot.setText(movie.column_plot);
                    release.setText(movie.column_release_date);
                    rating.setText(movie.column_user_rating + "");

                    Picasso.with(getActivity()).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + movie.column_poster).into(poster);
                    break;
                case DETAIL_REVIEW_LOADER:

                    reviewTv.setVisibility(View.VISIBLE);
                    reviewCursorAdapter.swapCursor(data);
                    reviews.setAdapter(reviewCursorAdapter);

                    fav.setVisibility(View.VISIBLE);

                    break;
                case DETAIL_TRAILER_LOADER:

                    trailerTv.setVisibility(View.VISIBLE);

                    trailerCursorAdapter.swapCursor(data);
                    trailers.setAdapter(trailerCursorAdapter);

                    break;
            }
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent(trailerIntentText));
            }
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loaderFinished = false;
        if (loader.getId() == DETAIL_REVIEW_LOADER)
            reviewCursorAdapter.swapCursor(null);
        if (loader.getId() == DETAIL_TRAILER_LOADER)
            trailerCursorAdapter.swapCursor(null);
    }

    /**
     * trailers listView onclick
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String trailerFullUrl;
        TrailersEntry trailersEntry;

        if (ApiCalls.getSettings(getActivity()).equals(getString(R.string.pref_movies_label_fav))) {

            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            trailersEntry = TrailersTable.getRow(cursor, true);

        } else {
            trailersEntry = (TrailersEntry) parent.getItemAtPosition(position);
        }
        trailerFullUrl = ApiCalls.YOUTUBE_BASE_URL + trailersEntry.column_trailer;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerFullUrl));

        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(intent);

    }

    public LoaderManager.LoaderCallbacks<ArrayList<ReviewsEntry>> reviewsLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<ArrayList<ReviewsEntry>>() {
                @Override
                public Loader<ArrayList<ReviewsEntry>> onCreateLoader(int id, Bundle args) {
                    if (movie != null)
                        return new DetailFragmentLoaderReviews(getActivity(), movie.column_movie_id);
                    return null;
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<ReviewsEntry>> loader, ArrayList<ReviewsEntry> data) {

                    if (data != null && loader != null) {
                        reviewsArrayAdapter = new ReviewsArrayAdapter(getActivity(), R.layout.review_item, data);
                        reviewsEntries = data;
                        reviews.setAdapter(reviewsArrayAdapter);
                        loaderFinished = true;
                        fav.setVisibility(View.VISIBLE);
                        reviewTv.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<ReviewsEntry>> loader) {
                    reviews.setAdapter(null);
                    loaderFinished = false;
                }
            };

    public LoaderManager.LoaderCallbacks<ArrayList<TrailersEntry>> trailersLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<ArrayList<TrailersEntry>>() {
                @Override
                public Loader<ArrayList<TrailersEntry>> onCreateLoader(int id, Bundle args) {
                    if (movie != null)
                        return new DetailFragmentLoaderTrailers(getActivity(), movie.column_movie_id);
                    return null;
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<TrailersEntry>> loader, ArrayList<TrailersEntry> data) {
                    if (data != null && loader != null && data.size() != 0) {
                        trailersArrayAdapter = new TrailersArrayAdapter(getActivity(), R.layout.trailer_item, data);
                        trailersEntries = data;
                        trailers.setAdapter(trailersArrayAdapter);
                        loaderFinished = true;
                        trailerTv.setVisibility(View.VISIBLE);
                        if (mShareActionProvider != null) {
                            mShareActionProvider.setShareIntent(createShareForecastIntent(trailerIntentText));
                        }
                    }
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<TrailersEntry>> loader) {
                    trailers.setAdapter(null);
                    loaderFinished = false;
                }
            };
}
