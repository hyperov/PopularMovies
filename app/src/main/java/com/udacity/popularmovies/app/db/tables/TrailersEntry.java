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

    public TrailersEntry(String column_movie_id, String column_trailer, String column_trailer_name) {
        this.column_movie_id = column_movie_id;
        this.column_trailer = column_trailer;
        this.column_trailer_name = column_trailer_name;
    }

    @SimpleSQLColumn(value = "_id", primary = true,autoincrement = true)
    public int _id;

    @SimpleSQLColumn("movie_id")
    public String column_movie_id;

    // the string in this column is to be added to full youtube url
    @SimpleSQLColumn("trailer")
    public String column_trailer;

   //name of trailer
    @SimpleSQLColumn("trailer_name")
    public String column_trailer_name;
}
