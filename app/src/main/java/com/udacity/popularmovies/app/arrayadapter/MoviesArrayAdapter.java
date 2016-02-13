package com.udacity.popularmovies.app.arrayadapter;

import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by DELL I7 on 2/13/2016.
 */
public class MoviesArrayAdapter extends RecyclerView.Adapter<MoviesArrayAdapter.ViewHolder> {

    Context context;
    ArrayList<MoviesEntry> moviesEntryArrayList;
    MoviesEntry moviesEntry;


    public MoviesArrayAdapter(ArrayList<MoviesEntry> moviesEntryArrayList, Context context) {
        this.moviesEntryArrayList = moviesEntryArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View recyclerView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(recyclerView);
        recyclerView.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        moviesEntry = moviesEntryArrayList.get(position);
        Picasso.with(holder.itemView.getContext()).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + moviesEntry.column_poster)
                .into(holder.recyclerImageView);


    }

    @Override
    public int getItemCount() {
        return moviesEntryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView recyclerImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            recyclerImageView = (ImageView) itemView.findViewById(R.id.recycle_image_view);
        }

        @Override
        public void onClick(View v) {

            ViewHolder viewHolder = (ViewHolder) v.getTag();

            ((MainFragment.Callback) v.getContext())
                    .onItemSelected(null, moviesEntryArrayList.get(viewHolder.getAdapterPosition()));


            MainFragment.mPosition = moviesEntryArrayList.indexOf(moviesEntry);
        }
    }

}
