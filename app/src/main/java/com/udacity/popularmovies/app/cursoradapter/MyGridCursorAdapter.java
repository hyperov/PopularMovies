package com.udacity.popularmovies.app.cursoradapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.app.Activities.MainFragment;
import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.db.tables.MoviesEntry;
import com.udacity.popularmovies.app.db.tables.MoviesTable;

/**
 * Created by DELL I7 on 2/4/2016.
 */
public class MyGridCursorAdapter extends CursorRecyclerViewAdapter<MyGridCursorAdapter.ViewHolder> {

    public Cursor itemCursor;
    public MoviesEntry movie;

    public MyGridCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);

    }

    //    public static
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView recycleItemImageView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            recycleItemImageView = (ImageView) view.findViewById(R.id.recycle_image_view);
        }

        @Override
        public void onClick(View v) {

            if (itemCursor != null) {
                ((MainFragment.Callback) recycleItemImageView.getContext())
                        .onItemSelected(movie.column_movie_id, null);
            }

            MainFragment.mPosition = itemCursor.getPosition();
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
        itemCursor = cursor;

        movie = MoviesTable.getRow(cursor, false);
        Picasso.with(viewHolder.itemView.getContext()).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + movie.column_poster).
                into(viewHolder.recycleItemImageView);

    }


}


