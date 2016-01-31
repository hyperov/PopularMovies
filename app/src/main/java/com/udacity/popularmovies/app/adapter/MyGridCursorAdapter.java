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
 * Created by DELL I7 on 1/30/2016.
 */
public class MyGridCursorAdapter extends CursorRecyclerViewAdapter<MyGridCursorAdapter.ViewHolder> {

    public Context mContext;
    public Cursor cursor;

    public MyGridCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
        this.cursor = cursor;
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView recycleItemImageView;

        public ViewHolder(View view) {
            super(view);
            recycleItemImageView = (ImageView) view.findViewById(R.id.recycle_image_view);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
cursor=(Cursor)v;
//            Cursor cursor = (Cursor) v.getAdapterPosition();
//            View view = v.getRootView();
//            itemView.get;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        MoviesEntry movie = MoviesTable.getRow(cursor, true);
        Picasso.with(mContext).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + movie.column_poster)
                .into(viewHolder.recycleItemImageView);

    }
}
