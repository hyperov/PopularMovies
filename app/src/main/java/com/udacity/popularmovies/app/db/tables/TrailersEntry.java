package com.udacity.popularmovies.app.db.tables;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by DELL I7 on 1/25/2016.
 */
@SimpleSQLTable(table = "trailers", provider = "PopularProvider")
public class TrailersEntry {

    public TrailersEntry() {
    }

    public TrailersEntry(String _id, String column_movie_id, String column_trailer) {
        this._id = _id;
        this.column_movie_id = column_movie_id;
        this.column_trailer = column_trailer;
    }

    @SimpleSQLColumn(value = "_id", primary = true)
    public String _id;

    @SimpleSQLColumn("movie_id")
    public String column_movie_id;

    // the string in this column is to be added to full youtube url
    @SimpleSQLColumn("trailer")
    public String column_trailer;
}
