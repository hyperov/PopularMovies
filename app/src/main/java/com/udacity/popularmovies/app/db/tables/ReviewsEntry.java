package com.udacity.popularmovies.app.db.tables;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by DELL I7 on 1/25/2016.
 */
@SimpleSQLTable(table = "reviews", provider = "PopularProvider")
public class ReviewsEntry {

    @SimpleSQLColumn(value = "_id", primary = true)
    public String _id;

    //        public static final String TABLE_NAME = "reviews";
    @SimpleSQLColumn("movie_id")
    public String column_movie_id;

    //author of review
    @SimpleSQLColumn("author")
    public String column_author;

    //the content of review
    @SimpleSQLColumn("review")
    public String column_review;


}
