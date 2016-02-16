package com.udacity.popularmovies.app.cursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.db.tables.TrailersEntry;
import com.udacity.popularmovies.app.db.tables.TrailersTable;

/**
 * Created by DELL I7 on 2/9/2016.
 */
public class TrailerCursorAdapter extends CursorAdapter {
    public Cursor cursor;

    public TrailerCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.cursor = c;
    }

    public class ViewHolder {
        public TextView trailerName;

        public ViewHolder(View itemView) {

            trailerName = (TextView) itemView.findViewById(R.id.trailer_text_view);
        }
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        cursor.moveToPosition(cursor.getPosition());
        TrailersEntry trailerItem = TrailersTable.getRow(cursor, false);

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.trailerName.setText(trailerItem.column_trailer_name);

//        if (!cursor.moveToNext())
//            cursor.close();

    }


}

