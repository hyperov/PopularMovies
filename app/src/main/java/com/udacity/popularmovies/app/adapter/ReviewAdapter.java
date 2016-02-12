package com.udacity.popularmovies.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.db.tables.ReviewsEntry;
import com.udacity.popularmovies.app.db.tables.ReviewsTable;

/**
 * Created by DELL I7 on 2/9/2016.
 */
public class ReviewAdapter extends CursorAdapter {

    public Cursor cursor;

    public ReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.cursor = c;
    }

    public class ViewHolder {
        public TextView content, author;


        public ViewHolder(View itemView) {

            content = (TextView) itemView.findViewById(R.id.content);
            author = (TextView) itemView.findViewById(R.id.author);

        }

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        ReviewsEntry reviewItem = ReviewsTable.getRow(cursor, true);
        int i = cursor.getPosition();
//        List<ReviewsEntry> reviewsEntryList = ReviewsTable.getRows(cursor, true);
//        ReviewsEntry reviewItem = reviewsEntryList.get(cursor.getPosition());

        viewHolder.author.setText(reviewItem.column_author);
        viewHolder.content.setText(reviewItem.column_review);

    }

//    @Override
//    public int getCount() {
//        if (cursor != null)
//            return getCursor().getCount();
//        return 0;
//    }
//
//    @Override
//    public Cursor getCursor() {
//        if (cursor != null)
//            return cursor;
//        return null;
//    }


}
