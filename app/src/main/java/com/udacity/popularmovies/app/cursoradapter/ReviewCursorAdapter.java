package com.udacity.popularmovies.app.cursoradapter;

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
public class ReviewCursorAdapter extends CursorAdapter {

    public Cursor cursor;

    public ReviewCursorAdapter(Context context, Cursor c, int flags) {
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

        cursor.moveToPosition(cursor.getPosition());
        ReviewsEntry reviewItem = ReviewsTable.getRow(cursor, false);

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.author.setText(reviewItem.column_author);
        viewHolder.content.setText(reviewItem.column_review);

//        if (!cursor.moveToNext())
//            cursor.close();

    }


}
