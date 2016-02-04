package com.udacity.popularmovies.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.List;

/**
 * Created by DELL I7 on 1/30/2016.
 */
public class MyGridCursorAdapter extends CursorRecyclerViewAdapter<MyGridCursorAdapter.ViewHolder> {


    public MyGridCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView recycleItemImageView;

        public ViewHolder(View view) {
            super(view);
            recycleItemImageView = (ImageView) view.findViewById(R.id.recycle_image_view);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
//            cursor = (Cursor) v;
//            cursor.get
//            Cursor cursor = (Cursor) v.getAdapterPosition();
//            View view = v.getRootView();
//            itemView.get;
            MoviesEntry movie = MoviesTable.getRow(getCursor(), true);
            String movieId = movie.column_movie_id;
            ((MainFragment.Callback) this).onItemSelected(movieId);
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
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor,Context context,int position) {
//        viewHolder.recycleItemImageView.setOnClickListener((View.OnClickListener) this);
        List<MoviesEntry> movies = MoviesTable.getRows(cursor, true);
        MoviesEntry movie = movies.get(position);
//        MoviesEntry movie = movies.get(cursor.getPosition());
        Picasso.with(context).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + movie.column_poster)
                .into(viewHolder.recycleItemImageView);

        Log.e( "onBindViewHolder: ",movie.column_movie_name);

    }
}
