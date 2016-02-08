package com.udacity.popularmovies.app.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.db.tables.MoviesTable;


/**
 * Created by DELL I7 on 10/13/2015.
 */
public class ApiCalls {

    public static final String API_KEY = "6bf996b135aa742c858d7489e71a0347";

    public static final String BASE_IMAGE_POSTER_URL = "http://image.tmdb.org/t/p/";

    public static final String BASE_IMAGE_WIDTH_92 = "w92";
    public static final String BASE_IMAGE_WIDTH_154 = "w154";
    public static final String BASE_IMAGE_WIDTH_185 = "w185";
    public static final String BASE_IMAGE_WIDTH_342 = "w342";
    public static final String BASE_IMAGE_WIDTH_500 = "w500";
    public static final String BASE_IMAGE_WIDTH_780 = "w780";
    public static final String BASE_IMAGE_WIDTH_original = "original";

    public static final String BASE_IMAGE_URL_AND_WIDTH = BASE_IMAGE_POSTER_URL + BASE_IMAGE_WIDTH_342;

    public static final String BASE_API_CALL_MOVIE = "https://api.themoviedb.org/3/discover/movie?api_key="
            + API_KEY + "&sort_by=";

    //use it for api call for reviews and trailers
    public static String API_CALL_MOVIE_ID;

    public static final String BASE_TRAILERS_REVIEWS = "https://api.themoviedb.org/3/movie/";

    // where select strings for preference

    public static final String FAV_SELECT = "favourite =? ";


    // where select args for preference
    public static final String FAV_SELECT_ARGS = "t";
    public static final String POPULARITY_SORT_ORDER = MoviesTable.FIELD_POPULARITY + " DESC LIMIT 20";
    public static final String RATING_SORT_ORDER = MoviesTable.FIELD_USER_RATING + " DESC LIMIT 20";

    public static int RESULTS_PER_PAGE;


    public static String getApiCallReviews() {
        return BASE_TRAILERS_REVIEWS + API_CALL_MOVIE_ID +
                "/reviews?api_key=" + API_KEY;
    }

    /**
     * @return url of reviews
     */
    public static String getApiCallTrailers() {
        return BASE_TRAILERS_REVIEWS + API_CALL_MOVIE_ID +
                "/videos?api_key=" + API_KEY;
    }

    /**
     * @param context
     * @return url of movies
     */
    public static String getMovies(Context context) {

        if (getSettings(context) != context.getString(R.string.pref_movies_label_fav))
            return BASE_API_CALL_MOVIE + getSettings(context);

        //setting
        return null;

    }

    /**
     * @param context
     * @return preference of user for popular movies or high rating movies
     */
    public static String getSettings(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String order = pref.getString(context.getString(R.string.pref_movies_key),
                context.getString(R.string.pref_movies_label_popular_entry));
        return order;
    }


}
