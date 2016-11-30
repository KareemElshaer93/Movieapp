package com.example.hp.movieapp.activities;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hp.movieapp.R;
import com.example.hp.movieapp.models.Movie;
import com.example.hp.movieapp.models.Review;
import com.example.hp.movieapp.models.Trailer;
import com.example.hp.movieapp.utilities.DatabaseHelper;
import com.example.hp.movieapp.utilities.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Movie movie;
    private ListView reviewsListView;
    private ListView trailersListView;
    private ArrayList<Trailer> trailers=new ArrayList<>();
    private ArrayList<String> trailersNames=new ArrayList<>();
    private ArrayList<Review>reviews=new ArrayList<>();
    private ArrayList<String> reviewsContent=new ArrayList<>();
    private ArrayAdapter<String> adapterReviews;
    private ArrayAdapter<String> adapterTrailers;
    private Button b_favourites;

    private DatabaseHelper database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_detail, container, false);
        //Intent intent = getActivity().getIntent();

        //Bundle sentBundle = intent.getExtras();
        Bundle twoPaneBundle=getArguments();
        movie= twoPaneBundle.getParcelable("movie");
        if (movie!=null) {
            //movie= sentBundle.getParcelable("movie");

            ((TextView) rootView.findViewById(R.id.tv_title)).setText(movie.getTitle());
            ((TextView) rootView.findViewById(R.id.tv_year)).setText(movie.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.tv_rating)).setText(movie.getRating());
            ((TextView) rootView.findViewById(R.id.tv_overView)).setText(movie.getOverView());

            database = new DatabaseHelper(getContext(), "favourites.sqlite");
            final ContentValues contentValues = new ContentValues();
            b_favourites = (Button) rootView.findViewById(R.id.b_favorite);
            b_favourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b_favourites.setClickable(false);
                    contentValues.put("title", movie.getTitle());
                    contentValues.put("overview", movie.getOverView());
                    contentValues.put("rating", movie.getRating());
                    contentValues.put("releasedate", movie.getReleaseDate());
                    contentValues.put("poster", movie.getPoster());
                    contentValues.put("id", movie.getId());
                    try {
                        database.insertRow("movie", null, contentValues);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    b_favourites.setText("Added to Favourites");


                }
            });


            reviewsListView = (ListView) rootView.findViewById(R.id.lv_reviews);
            TextView textV = new TextView(getContext());
            textV.setTextSize(20);
            textV.setText("Reviews");
            reviewsListView.addHeaderView(textV);
            trailersListView = (ListView) rootView.findViewById(R.id.lv_trailers);
            TextView textView = new TextView(getContext());
            textView.setTextSize(20);
            textView.setText("Trailers");
            trailersListView.addHeaderView(textView);
            trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    watchVideo(trailers.get(position - 1).getKey());
                    Log.i("DetailActivity", "onItemClick: trailer name: " + trailers.get(position - 1).getName());
                    Log.i("DetailActivity", "onItemClick: trailersNames name: " + trailersNames.get(position - 1));


                }
            });


            updateReviews();
            updateTrailers();

            ImageView imageView = (ImageView) rootView.findViewById(R.id.iv_poster);
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movie.getPoster()).placeholder(R.drawable.loading).into(imageView);

        }
        return rootView;
    }

    public void watchVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public void updateTrailers() {

        LoadTrailers loadTrailers = new LoadTrailers();
        String id= movie.getId();
        loadTrailers.execute("http://api.themoviedb.org/3" +
                "/movie/"+id+"/videos?api_key=f9312b216d985ada8ec33d3bf7c22076");



    }
    public void updateReviews() {

        LoadReviews loadReviews = new LoadReviews();
        String id= movie.getId();
        loadReviews.execute("http://api.themoviedb.org/3" +
                "/movie/"+id+"/reviews?api_key=f9312b216d985ada8ec33d3bf7c22076");


    }

    public class LoadTrailers extends AsyncTask<String, Void, ArrayList<Trailer>> {
        private final String TAG = LoadTrailers.class.getSimpleName();


        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            HttpHandler handler = new HttpHandler();
            String jsonStr = handler.makeServiceCall(params[0]);


            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray results = jsonObject.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {

                    JSONObject movieDetail = results.getJSONObject(i);
                    String key = movieDetail.getString("key");
                    String trailerName = movieDetail.getString("name");
                    Trailer trailer =new Trailer(trailerName,key);
                    trailers.add(trailer);
                    Log.d("trailer name:", trailerName);



                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


            return trailers;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {
            super.onPostExecute(trailers);
            for (int i=0;i<trailers.size();i++){
                trailersNames.add(trailers.get(i).getName());
            }
            if (adapterTrailers==null){
                adapterTrailers=new ArrayAdapter<String>(getContext(), R.layout.list_item, R.id.tv_listItem,trailersNames);
            }
            trailersListView.setAdapter(adapterTrailers);
            Log.i(TAG, "onPostExecute: trailers size:"+trailers.size());
            Log.i(TAG, "onPostExecute: trailers Names size: "+trailersNames.size());


        }
    }
    public class LoadReviews extends AsyncTask<String, Void, ArrayList<Review>> {
        private final String TAG = LoadTrailers.class.getSimpleName();


        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            HttpHandler handler = new HttpHandler();
            String jsonStr = handler.makeServiceCall(params[0]);

            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray results = jsonObject.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {

                        JSONObject movieDetail = results.getJSONObject(i);
                        String author = movieDetail.getString("author");
                        String content = movieDetail.getString("content");
                    reviews.add(new Review(author,content));
                        Log.d("author name:", author);




                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


            return reviews;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            super.onPostExecute(reviews);
            for (int i=0;i<reviews.size();i++){
                reviewsContent.add(reviews.get(i).getAuthor()+" : "+reviews.get(i).getContent());
                Log.i(TAG, "onPostExecute: author:"+ reviews.get(i).getAuthor());
                Log.i(TAG, "onPostExecute: size"+reviews.size());

            }
            if (adapterReviews==null){
                adapterReviews=new ArrayAdapter<>(getContext(), R.layout.list_item, R.id.tv_listItem,reviewsContent);
            }
            reviewsListView.setAdapter(adapterReviews);
        }
    }

}
