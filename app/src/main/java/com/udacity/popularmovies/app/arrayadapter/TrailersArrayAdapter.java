package com.udacity.popularmovies.app.arrayadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.popularmovies.app.activities.DetailFragment;
import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.db.tables.TrailersEntry;

import java.util.ArrayList;

/**
 * Created by DELL I7 on 2/13/2016.
 */
public class TrailersArrayAdapter extends ArrayAdapter<TrailersEntry> {

    ArrayList<TrailersEntry> trailersEntryArrayList;

    public TrailersArrayAdapter(Context context, int resource, ArrayList<TrailersEntry> objects) {
        super(context, resource, objects);
        this.trailersEntryArrayList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trailer_item, parent, false);


        }
        TrailersEntry trailersEntry = trailersEntryArrayList.get(position);
        ViewHolder viewHolder = new ViewHolder(convertView);

        viewHolder.trailerName.setText(trailersEntry.column_trailer_name);

        DetailFragment.trailerIntentText=trailersEntryArrayList.get(0).column_trailer;


        return convertView;
    }

    class ViewHolder {
        TextView trailerName;

        public ViewHolder(View itemView) {
            trailerName = (TextView) itemView.findViewById(R.id.trailer_text_view);



        }
    }
}
