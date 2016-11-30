package com.example.hp.movieapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.movieapp.R;
import com.example.hp.movieapp.activities.MainActivity;
import com.example.hp.movieapp.models.Movie;
import com.example.hp.movieapp.utilities.DatabaseHelper;
import com.example.hp.movieapp.utilities.HttpHandler;
import com.example.hp.movieapp.utilities.Listener;
import com.example.hp.movieapp.utilities.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ArrayList<Movie> movies ;
    private MovieAdapter movieAdapter;
    private GridView gridView;
    private String  TAG=MainActivity.class.getSimpleName();

    private DatabaseHelper database;
    private Cursor cursor;



    public MainActivityFragment() {
    }
    private Listener mListener;
    void setListener(Listener listener) {
        this.mListener=listener;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);




        gridView = (GridView) rootView.findViewById(R.id.gridView);
        movies=new ArrayList<>();










        Log.i(TAG, "onCreateView: size movies"+movies.size());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("title", movies.get(position).getTitle());
                Movie movie=movies.get(position);
                mListener.setSelectedMovie(movie);

            }
        });


        return rootView;


    }



    public void updateImages() {
        LoadImages loadImages = new LoadImages();
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        database=new DatabaseHelper(getContext(),"favourites.sqlite");

        String sort= preferences.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_popularity));

        if (sort.equals("favourites")){

                readData();


        }else {
            ConnectivityManager connec = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connec != null && (
                    (connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) ||
                            (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED))) {

                //You are connected, do something online.

                loadImages.execute("http://api.themoviedb.org/3/discover" +
                        "/movie?sort_by=" + sort + ".desc&api_key=f9312b216d985ada8ec33d3bf7c22076");

            } else if (connec != null && (
                    (connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) ||
                            (connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED ))) {

                //Not connected.
                Toast.makeText(getContext(), "You must be connected to the internet", Toast.LENGTH_LONG).show();
                getActivity().finish();


            }


        }
    }
    public void readData(){
        movies.clear();

        cursor =database.selectAllRaws("movie");
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++){
                Movie movie=new Movie(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
                movies.add(movie);

                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        if(movieAdapter==null) {
            movieAdapter = new MovieAdapter(movies, getActivity());
        }
        gridView.setAdapter(movieAdapter);




    }


    @Override
    public void onStart() {
        super.onStart();
        updateImages();


    }


    public class LoadImages extends AsyncTask<String, Void, ArrayList<Movie>> {
        private final String TAG = LoadImages.class.getSimpleName();


        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            HttpHandler handler = new HttpHandler();
            String jsonStr = handler.makeServiceCall(params[0]);
            if(movies!=null)
                movies.clear();
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray results = jsonObject.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {

                    JSONObject movieDetail = results.getJSONObject(i);
                    Movie movie = new Movie(movieDetail.getString("original_title"), movieDetail.getString("overview"), movieDetail.getString("vote_average"),
                            movieDetail.getString("release_date"), movieDetail.getString("poster_path"),movieDetail.getString("id"));
                    Log.d("Title: ", movie.getTitle());
                    Log.d("id: ", movie.getId());

                    movies.add(movie);

                }

                Log.i(TAG, "onbackground:movies size " + movies.size());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies1) {
            super.onPostExecute(movies1);


                if(movieAdapter==null) {
                    movieAdapter = new MovieAdapter(movies1, getActivity());
                }
                gridView.setAdapter(movieAdapter);
           // mListener.setSelectedMovie(movies1.get(0));
                Log.i(TAG, "onPostExecute:movies size " + movies.size());
        }
    }
}
