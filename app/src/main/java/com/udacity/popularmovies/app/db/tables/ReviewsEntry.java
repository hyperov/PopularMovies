package com.udacity.popularmovies.app.db.tables;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by DELL I7 on 1/25/2016.
 */
@SimpleSQLTable(table = "reviews", provider = "PopularProvider")
public class ReviewsEntry {

    public ReviewsEntry() {
    }

    public ReviewsEntry( String column_movie_id, String column_author, String column_review) {
      //  this._id = _id;
        this.column_movie_id = column_movie_id;
        this.column_author = column_author;
        this.column_review = column_review;
    }

    @SimpleSQLColumn(value = "_id", primary = true,autoincrement = true)
    public int _id;

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
