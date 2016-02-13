package com.udacity.popularmovies.app.loader;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.db.tables.ReviewsEntry;
import com.udacity.popularmovies.app.db.tables.ReviewsTable;
import com.udacity.popularmovies.app.handler.JsonHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by DELL I7 on 2/13/2016.
 */
public class DetailFragmentLoaderReviews extends AsyncTaskLoader<ArrayList<ReviewsEntry>> {

    public DetailFragmentLoaderReviews(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<ReviewsEntry> loadInBackground() {
        JsonHandler handler = new JsonHandler();
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
        return null;
    }

    @Override
    public void deliverResult(ArrayList<ReviewsEntry> data) {
        if (isStarted())
            super.deliverResult(data);
    }
}
