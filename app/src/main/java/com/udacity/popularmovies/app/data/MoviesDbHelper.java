/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.udacity.popularmovies.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.udacity.popularmovies.app.data.MoviesContract.MoviesEntry;
import com.udacity.popularmovies.app.data.MoviesContract.TrailersEntry;
import com.udacity.popularmovies.app.data.MoviesContract.ReviewsEntry;


/**
 * Manages a local database for weather data.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //table of movies including favourite column for preference
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesEntry.COLUMN_Movie_ID + " TEXT UNIQUE NOT NULL, " +
                MoviesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL " +
                MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL " +
                MoviesEntry.COLUMN_USER_RATING + " TEXT NOT NULL " +
                // 1=fav ,0=default(no fav)
                MoviesEntry.COLUMN_FAVOURITE + " INTEGER NOT NULL " +
                " );";

        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " + TrailersEntry.TABLE_NAME + " (" +

                TrailersEntry._ID + " INTEGER PRIMARY KEY, " +

                // the ID of the location entry associated with this weather data
                TrailersEntry.COLUMN_TRAILER + " TEXT NOT NULL, " +
                TrailersEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +

                ReviewsEntry._ID + " INTEGER PRIMARY KEY, " +

                // the ID of the location entry associated with this weather data
                ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                ReviewsEntry.COLUMN_REVIEW + " TEXT NOT NULL, " +
                ReviewsEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL " +
                " );";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
