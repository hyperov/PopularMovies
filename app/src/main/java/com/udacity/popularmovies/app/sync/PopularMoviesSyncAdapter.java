package com.udacity.popularmovies.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import com.udacity.popularmovies.app.R;
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
 * Created by DELL I7 on 1/27/2016.
 */
public class PopularMoviesSyncAdapter extends AbstractThreadedSyncAdapter {

    // Interval at which to sync with the Movies Api, in seconds.
    // 12 hours *  60 *60 (in sec)
    public static final int SYNC_INTERVAL = 12 * 60 * 60;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    public PopularMoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {


        // movies = new ArrayList<Movie>();

        JsonHandler handler = new JsonHandler();

        //not setting
        if (ApiCalls.getMovies(getContext()) != null) {
            String jsonMovies = handler.getJsonString(ApiCalls.getMovies(getContext()));
            try {
                JSONObject object = new JSONObject(jsonMovies);
                JSONArray results = object.getJSONArray("results");

                //add content values of movies to vector
                Vector<ContentValues> moviesVector = new Vector<ContentValues>(results.length());


                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObject = results.getJSONObject(i);
                    ApiCalls.API_CALL_MOVIE_ID = movieObject.getString("id");

                    /**********************************************************************************************/
                    //json parsing for reviews
                    String jsonReviews = handler.getJsonString(ApiCalls.API_CALL_REVIEWS);
                    JSONObject reviewObject = new JSONObject(jsonReviews);

                    JSONArray reviewResults=reviewObject.getJSONArray("results");

                    Vector<ContentValues> reviewsVector = new Vector<ContentValues>(reviewResults.length());

                    for (int k = 0; k < reviewResults.length(); k++) {
                        JSONObject reviewItem=reviewResults.getJSONObject(k);

                        ReviewsEntry review=new ReviewsEntry("",ApiCalls.API_CALL_MOVIE_ID
                                ,reviewItem.getString("author"),reviewItem.getString("content"));

                        //add reviews content values to vector
                        reviewsVector.add(ReviewsTable.getContentValues(review, false));
                    }

                    if ( reviewsVector.size() > 0 ) {
                        ContentValues[] cvrArray = new ContentValues[reviewsVector.size()];
                        reviewsVector.toArray(cvrArray);
                        getContext().getContentResolver().bulkInsert(ReviewsTable.CONTENT_URI,cvrArray);
                    }

                    //delete old reviews which their movie no longer exist in the database

                    /***********************************************************************************************/
                    //json parsing for trailers
                    String jsonTrailers = handler.getJsonString(ApiCalls.API_CALL_REVIEWS);
                    JSONObject trailerObject = new JSONObject(jsonTrailers);

                    JSONArray trailerResults=trailerObject.getJSONArray("results");

                    Vector<ContentValues> trailersVector = new Vector<ContentValues>(trailerResults.length());

                    for (int v = 0; v < trailerResults.length(); v++) {
                        JSONObject trailerItem=trailerResults.getJSONObject(v);

                        TrailersEntry trailer=new TrailersEntry("",ApiCalls.API_CALL_MOVIE_ID
                                ,trailerItem.getString("key"));

                        //add reviews content values to vector
                        trailersVector.add(TrailersTable.getContentValues(trailer, false));
                    }

                    if ( trailersVector.size() > 0 ) {
                        ContentValues[] cvtArray = new ContentValues[trailersVector.size()];
                        trailersVector.toArray(cvtArray);
                        getContext().getContentResolver().bulkInsert(TrailersTable.CONTENT_URI,cvtArray);
                    }

                    //delete old trailers which their movie no longer exist in the database


                    /***********************************************************************************************/
                    MoviesEntry movie = new MoviesEntry("", ApiCalls.API_CALL_MOVIE_ID, movieObject.getString("title"),
                            movieObject.getString("poster_path"), movieObject.getString("overview"),
                            movieObject.getString("vote_average"), movieObject.getString("release_date"), "f");

                    //add content values to vector
                    moviesVector.add(MoviesTable.getContentValues(movie, false));
                }

                if ( moviesVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[moviesVector.size()];
                    moviesVector.toArray(cvArray);
                    getContext().getContentResolver().bulkInsert(MoviesTable.CONTENT_URI,cvArray);
                }
                //delete old movies which aren't anymore in page 1

            } catch (JSONException e) {
                e.printStackTrace();
            }




        }

    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        PopularMoviesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

}
