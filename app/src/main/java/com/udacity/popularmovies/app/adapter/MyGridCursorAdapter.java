package com.udacity.popularmovies.app.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.db.tables.MoviesEntry;
import com.udacity.popularmovies.app.db.tables.MoviesTable;

/**
 * Created by DELL I7 on 2/4/2016.
 */
public class MyGridCursorAdapter extends CursorRecyclerViewAdapter<MyGridCursorAdapter.ViewHolder> {

    public MyGridCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        setHasStableIds(true);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView recycleItemImageView;

        public ViewHolder(View view) {
            super(view);
            recycleItemImageView = (ImageView) view.findViewById(R.id.recycle_image_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        itemView.setTag(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
//        List<MoviesEntry> movies = MoviesTable.getRows(cursor, false);
//        cursor.setNotificationUri(viewHolder.itemView.
//                getContext().getContentResolver(), MoviesTable.CONTENT_URI);
//        cursor.close();

        MoviesEntry movie = MoviesTable.getRow(cursor, false);
        Picasso.with(viewHolder.itemView.getContext()).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + movie.column_poster).
                into(viewHolder.recycleItemImageView);


//        cursor.close();


    }


}


