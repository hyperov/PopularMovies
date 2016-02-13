package com.udacity.popularmovies.app.loader;

import android.content.Context;

import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.db.tables.MoviesEntry;
import com.udacity.popularmovies.app.handler.JsonHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL I7 on 2/13/2016.
 */
public class MainFragmentLoaderMovies extends android.support.v4.content.AsyncTaskLoader<ArrayList<MoviesEntry>> {

    public MainFragmentLoaderMovies(Context context) {
        super(context);
    }




    @Override
    public ArrayList<MoviesEntry> loadInBackground() {
        JsonHandler handler = new JsonHandler();
        ArrayList<MoviesEntry> moviesEntryArrayList = new ArrayList<>();
        //not setting
        String jsonMovies = handler.getJsonString(ApiCalls.getMovies(getContext()));
        try {
            JSONObject object = new JSONObject(jsonMovies);
            JSONArray results = object.getJSONArray("results");

            //add content values of movies to vector
//            Vector<ContentValues> moviesVector = new Vector<ContentValues>(results.length());


            ApiCalls.RESULTS_PER_PAGE = results.length();
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieObject = results.getJSONObject(i);
               // ApiCalls.API_CALL_MOVIE_ID = movieObject.getString("id");

                MoviesEntry movie = new MoviesEntry(movieObject.getString("id"), movieObject.getString("title"),
                        movieObject.getString("poster_path"), movieObject.getString("overview"),
                        movieObject.getDouble("vote_average"), movieObject.getString("release_date"),
                        "f", movieObject.getDouble("popularity"));

                //add content values to vector
//                moviesVector.add(MoviesTable.getContentValues(movie, false));
                moviesEntryArrayList.add(movie);
            }

//            if (moviesVector.size() > 0) {
//                ContentValues[] cvArray = new ContentValues[moviesVector.size()];
//                moviesVector.toArray(cvArray);
//                getContext().getContentResolver().bulkInsert(MoviesTable.CONTENT_URI, cvArray);
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moviesEntryArrayList;
    }

    @Override
    public void deliverResult(ArrayList<MoviesEntry> data) {
        if (isStarted())
            super.deliverResult(data);
    }
}
