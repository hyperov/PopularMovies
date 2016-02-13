package com.udacity.popularmovies.app.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.api.ApiCalls;

public class DetailedActivity extends AppCompatActivity {


    public static final String MOVIE_ID_TAG = "movieId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            if (ApiCalls.getSettings(this) == getString(R.string.pref_movies_label_fav)) {
                arguments.putString(MOVIE_ID_TAG,
                        getIntent().getExtras().getString(MOVIE_ID_TAG));
            } else {
                arguments.putParcelable(MOVIE_ID_TAG,
                        getIntent().getExtras().getParcelable(MOVIE_ID_TAG));
            }


            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movies_detail_container, fragment)
                    .commit();
        }


    }


}
