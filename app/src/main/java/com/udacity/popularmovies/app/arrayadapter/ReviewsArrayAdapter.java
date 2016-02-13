package com.udacity.popularmovies.app.arrayadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.db.tables.ReviewsEntry;

import java.util.ArrayList;


/**
 * Created by DELL I7 on 2/13/2016.
 */
public class ReviewsArrayAdapter extends ArrayAdapter<ReviewsEntry> {

    ArrayList<ReviewsEntry> reviewsEntryArrayList;

    public ReviewsArrayAdapter(Context context, int resource, ArrayList<ReviewsEntry> reviewsEntryArrayList) {
        super(context, resource,reviewsEntryArrayList);
        this.reviewsEntryArrayList = reviewsEntryArrayList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.review_item, parent, false);


        }
        ReviewsEntry reviewsEntry = reviewsEntryArrayList.get(position);
        ViewHolder viewHolder = new ViewHolder(convertView);

        viewHolder.author.setText(reviewsEntry.column_author);
        viewHolder.content.setText(reviewsEntry.column_review);

        return convertView;
    }

    class ViewHolder {
        TextView author, content;

        public ViewHolder(View itemView) {
            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);


        }
    }
}
