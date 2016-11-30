package com.example.hp.movieapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hp.movieapp.R;
import com.example.hp.movieapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hp on 21/10/2016.
 */
public class MovieAdapter extends BaseAdapter {

    ArrayList<Movie> movies;
    Context context;
    LayoutInflater inflater;

    public MovieAdapter(ArrayList<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("size", movies.size() + "");
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = inflater.inflate(R.layout.movie_image, null);

        Log.d("title", movies.get(position).getTitle());


        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        Log.i("temp", "getView: " + "http://image.tmdb.org/t/p/w185/" + movies.get(position).getPoster());

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + movies.get(position).getPoster()).placeholder(R.drawable.loading).fit().into(imageView);


        return view;
    }
    public void clearAndAdd(ArrayList<Movie> movies1){

        this.movies.addAll(movies1);

    }

}
