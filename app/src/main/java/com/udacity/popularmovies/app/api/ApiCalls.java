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

    public static final String BASE_IMAGE_URL_AND_WIDTH = BASE_IMAGE_POSTER_URL + BASE_IMAGE_WIDTH_185;

//    public static final String BASE_API_CALL_MOVIE = "https://api.themoviedb.org/3/movie/" +
//            "?api_key="
//            + API_KEY + "&sort_by=";

    //use it for api call for reviews and trailers
    public static String API_CALL_MOVIE_ID;

    public static final String BASE_TRAILERS_REVIEWS_MOVIES = "https://api.themoviedb.org/3/movie/";

    //add this string before trailer url
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    // where select strings for preference

    public static final String FAV_SELECT = "favourite =? ";


    // where select args for preference
    public static final String FAV_SELECT_ARGS_false = "f";
    public static final String FAV_SELECT_ARGS_true = "t";
    public static final String POPULARITY_SORT_ORDER = MoviesTable.FIELD_POPULARITY + " DESC";
    public static final String RATING_SORT_ORDER = MoviesTable.FIELD_USER_RATING + " DESC";

    public static int RESULTS_PER_PAGE;


    public static String getApiCallReviews() {
        return BASE_TRAILERS_REVIEWS_MOVIES + API_CALL_MOVIE_ID +
                "/reviews?api_key=" + API_KEY;
    }

    /**
     * @return url of reviews
     */
    public static String getApiCallTrailers() {
        return BASE_TRAILERS_REVIEWS_MOVIES + API_CALL_MOVIE_ID +
                "/videos?api_key=" + API_KEY;
    }

    /**
     * @param context
     * @return url of movies
     */
    public static String getMovies(Context context) {

        if (getSettings(context) != context.getString(R.string.pref_movies_label_fav))
            return BASE_TRAILERS_REVIEWS_MOVIES + getSettings(context)+"?api_key="+API_KEY;

        //favourites
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
