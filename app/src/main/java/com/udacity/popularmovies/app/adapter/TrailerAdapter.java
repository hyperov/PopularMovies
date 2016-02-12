package com.udacity.popularmovies.app.adapter;

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

import java.util.List;

/**
 * Created by DELL I7 on 2/9/2016.
 */
public class TrailerAdapter extends CursorAdapter {
    Cursor cursor;

    public class ViewHolder {
        public final TextView trailerName;

        public ViewHolder(View itemView) {

            trailerName = (TextView) itemView.findViewById(R.id.trailer_text_view);

        }


    }

    public TrailerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.cursor = c;
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
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        List<TrailersEntry> trailersEntryList = TrailersTable.getRows(cursor, true);
        TrailersEntry trailerItem = trailersEntryList.get(cursor.getPosition());

        viewHolder.trailerName.setText(trailerItem.column_trailer_name);


    }

    @Override
    public int getCount() {
        if (cursor != null)
            return getCursor().getCount();
        return 0;
    }

    @Override
    public Cursor getCursor() {
        if (cursor != null)
            return cursor;
        return null;
    }
}

