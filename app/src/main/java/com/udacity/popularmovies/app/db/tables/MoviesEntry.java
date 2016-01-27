package com.udacity.popularmovies.app.db.tables;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by DELL I7 on 1/25/2016.
 */
@SimpleSQLTable(table = "movies", provider = "PopularProvider")
public class MoviesEntry {

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

    @SimpleSQLColumn("favourite")
    public String column_favourite;
}
