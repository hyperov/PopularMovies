package com.udacity.popularmovies.app.db.tables;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by DELL I7 on 1/25/2016.
 */
@SimpleSQLTable(table = "movies", provider = "PopularProvider")
public class MoviesEntry {

    public MoviesEntry(String _id, String column_movie_id, String column_movie_name, String column_poster,
                       String column_plot, String column_user_rating,
                       String column_release_date, String column_favourite) {
        this._id = _id;
        this.column_movie_id = column_movie_id;
        this.column_movie_name = column_movie_name;
        this.column_poster = column_poster;
        this.column_plot = column_plot;
        this.column_user_rating = column_user_rating;
        this.column_release_date = column_release_date;
        this.column_favourite = column_favourite;
    }


    public MoviesEntry() {
    }

    @SimpleSQLColumn(value = "_id", primary = true)
    public String _id;
    // Table name
//        public  String TABLE_NAME = "movies";
    @SimpleSQLColumn("movie_id")
    public String column_movie_id;

    @SimpleSQLColumn("title")
    public String column_movie_name;

    @SimpleSQLColumn("poster")
    public String column_poster;

    @SimpleSQLColumn("plot")
    public String column_plot;

    @SimpleSQLColumn("user_rating")
    public String column_user_rating;

    @SimpleSQLColumn("release_date")
    public String column_release_date;

    //t=true, f=false
    @SimpleSQLColumn("favourite")
    public String column_favourite;
}
