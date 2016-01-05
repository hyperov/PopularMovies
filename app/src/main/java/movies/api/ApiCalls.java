package movies.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import udacity.popularmovies.R;

/**
 * Created by DELL I7 on 10/13/2015.
 */
public class ApiCalls {

    public static final String API_KEY = "6bf996b135aa742c858d7489e71a0347";
    public static final String BASE_IMAGE_POSTER_URL = "http://image.tmdb.org/t/p/";
    public static final String BASE_IMAGE_WIDTH = "w342";
    public static final String BASE_IMAGE_URL_AND_WIDTH = BASE_IMAGE_POSTER_URL + BASE_IMAGE_WIDTH;
    public static final String BASE_API_CALL = "https://api.themoviedb.org/3/discover/movie?api_key="
            + API_KEY + "&sort_by=";

    public static String getMovies( Context context) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String order = pref.getString(context.getString(R.string.pref_movies_key),
                context.getString(R.string.pref_movies_label_popular_entry));

//        if (sorting == 1)
//            return BASE_API_CALL + "popularity.desc";
//        if (sorting == 2)
//            return BASE_API_CALL + "vote_average.desc";
//
//        return BASE_API_CALL + "popularity.desc";
        return BASE_API_CALL + order;
    }


}
