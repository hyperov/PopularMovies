package com.udacity.popularmovies.app.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.pojo.Movie;

public class DetailedActivity extends AppCompatActivity {

    TextView title,plot,release,rating;
    ImageView poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Bundle b = getIntent().getExtras();
        Movie obj =b.getParcelable("pojo.Movie");
               // b.getParcelable("pojo.Movie");

        title=(TextView)findViewById(R.id.title1);
        plot=(TextView)findViewById(R.id.plotsubject);
        release=(TextView)findViewById(R.id.releasedate);
        rating=(TextView)findViewById(R.id.ratingnumber);
        poster=(ImageView)findViewById(R.id.imageView);

        title.setText(obj.getTitle());
        plot.setText(obj.getPlotSynopsis());
        release.setText(obj.getReleaseDate());
        rating.setText(obj.getUserRating());

        Picasso.with(this).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + obj.getImg()).into(poster);


    }
}
