package com.udacity.popularmovies.app.db.tables;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by DELL I7 on 1/25/2016.
 */
@SimpleSQLTable(table = "trailers", provider = "PopularProvider")
public class TrailersEntry {

    @SimpleSQLColumn(value = "_id", primary = true)
    public String _id;

    @SimpleSQLColumn("movie_id")
    public String column_movie_id;

    // the string in this column is to be added to full youtube url
    @SimpleSQLColumn("trailer")
    public String column_trailer;
}
