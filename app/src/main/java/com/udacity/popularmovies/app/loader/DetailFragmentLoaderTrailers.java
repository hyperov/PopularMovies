package com.udacity.popularmovies.app.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.db.tables.TrailersEntry;
import com.udacity.popularmovies.app.handler.JsonHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL I7 on 2/13/2016.
 */
public class DetailFragmentLoaderTrailers extends AsyncTaskLoader<ArrayList<TrailersEntry>> {

    public DetailFragmentLoaderTrailers(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<TrailersEntry> loadInBackground() {

        ArrayList<TrailersEntry> trailersEntryArrayList = new ArrayList<>();
        JsonHandler handler = new JsonHandler();
        String jsonTrailers = handler.getJsonString(ApiCalls.getApiCallTrailers());
        JSONObject trailerObject = null;
        try {
            trailerObject = new JSONObject(jsonTrailers);
            JSONArray trailerResults = trailerObject.getJSONArray("results");

//            Vector<ContentValues> trailersVector = new Vector<ContentValues>(trailerResults.length());

            for (int v = 0; v < trailerResults.length(); v++) {
                JSONObject trailerItem = trailerResults.getJSONObject(v);


                TrailersEntry trailer = new TrailersEntry(ApiCalls.API_CALL_MOVIE_ID
                        , trailerItem.getString("key"), trailerItem.getString("name"));

                trailersEntryArrayList.add(trailer);
                //add reviews content values to vector
//                trailersVector.add(TrailersTable.getContentValues(trailer, false));
            }

//            if (trailersVector.size() > 0) {
//                ContentValues[] cvtArray = new ContentValues[trailersVector.size()];
//                trailersVector.toArray(cvtArray);
//                getContext().getContentResolver().bulkInsert(TrailersTable.CONTENT_URI, cvtArray);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailersEntryArrayList;
    }

    @Override
    public void deliverResult(ArrayList<TrailersEntry> data) {
        if (isStarted())
            super.deliverResult(data);
    }
}
