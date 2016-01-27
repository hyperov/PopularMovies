package com.udacity.popularmovies.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.app.R;
import com.udacity.popularmovies.app.api.ApiCalls;
import com.udacity.popularmovies.app.handler.JsonHandler;
import com.udacity.popularmovies.app.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter adapter;
    ArrayList<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "so what is this", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this,
                2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int mod = (position + 1) % 3;

                if (mod == 0)
                    return 2;
                else
                    return 1;
            }
        });

        movieList = new ArrayList<Movie>();
        ;
        adapter = new MovieAdapter(movieList, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        new Connection().execute(ApiCalls.getMovies(this));
        //  adapter=new MovieAdapter()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public class MovieAdapter extends
            RecyclerView.Adapter<MovieAdapter.PersonViewHolder> {


        ArrayList<Movie> movies;
        private Context context;

        public MovieAdapter(ArrayList<Movie> movies,
                            Context context) {

            this.context = context;
            this.movies = movies;
        }

        class PersonViewHolder extends RecyclerView.ViewHolder implements
                View.OnClickListener {


            ImageView listItem;

            PersonViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);

                listItem = (ImageView) itemView.findViewById(R.id.itemlist);
            }

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "text", Toast.LENGTH_SHORT).show();
                Movie m1 = movieList.get(getPosition());

                /*Parcel parcel=Parcel.obtain();
                parcel.writeParcelable(m1,1);*/

                Bundle bundle = new Bundle();
                bundle.putParcelable("pojo.Movie", m1);

                Intent detail = new Intent(getApplicationContext(), DetailedActivity.class);
                detail.putExtras(bundle);
                startActivity(detail);


            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            // TODO Auto-generated method stub
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            // TODO Auto-generated method stub
            return movies.size();
        }

        @Override
        public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {


            Picasso.with(context).load(ApiCalls.BASE_IMAGE_URL_AND_WIDTH + movies.get(i).getImg())
                    .into(personViewHolder.listItem);

            // personViewHolder.listItem.setImageResource(R.mipmap.ic_launcher);


        }

        @Override
        public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = null;
            int mod = (i + 1) % 3;
            // if (mod == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.recycler_item, viewGroup, false);
            // }else{
            // view = LayoutInflater.from(viewGroup.getContext()).inflate(
            // R.layout.fragment_new_offers_pair, viewGroup, false);
            // }
            // } else {
            // view = LayoutInflater.from(viewGroup.getContext()).inflate(
            // R.layout.fragment_new_offers_pair, viewGroup, false);
            // }

            PersonViewHolder pvh = new PersonViewHolder(view);
            view.setTag(pvh);
            return pvh;

        }

    }

    public class Connection extends AsyncTask<String, Void, String> {

        ArrayList<Movie> movies;


        @Override
        protected String doInBackground(String... params) {
            movies = new ArrayList<Movie>();

            JsonHandler handler = new JsonHandler();
            String jsonText = handler.getJsonString(params[0]);
            try {
                JSONObject object = new JSONObject(jsonText);
                JSONArray results = object.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObject = results.getJSONObject(i);
                    ApiCalls.API_CALL_MOVIE_ID = movieObject.getString("id");
                    Movie movie = new Movie(movieObject.getString("title"), movieObject.getString("poster_path"),
                            movieObject.getString("overview"), movieObject.getString("vote_average"),
                            movieObject.getString("release_date"), ApiCalls.API_CALL_MOVIE_ID);
                    movieList.add(movie);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter = new MovieAdapter(movieList, getApplicationContext());
            recyclerView.setAdapter(adapter);

        }
    }


}
