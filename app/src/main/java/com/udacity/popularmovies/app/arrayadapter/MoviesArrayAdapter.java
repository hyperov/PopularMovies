package com.udacity.popularmovies.app.arrayadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.db.tables.MoviesEntry;

import java.util.ArrayList;

/**
 * Created by DELL I7 on 2/13/2016.
 */
public class MoviesArrayAdapter extends ArrayAdapter<ArrayList<MoviesEntry>> {

    Context context;
    ArrayList<MoviesEntry> moviesEntryArrayList;

    public MoviesArrayAdapter(Context context, int resource, ArrayList<ArrayList<MoviesEntry>> objects) {
        super(context, resource, objects);
        this.context = context;
        this.moviesEntryArrayList = moviesEntryArrayList;
    }

    public class

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.recycler_item, parent, false);
        }


        return convertView;
    }
}
