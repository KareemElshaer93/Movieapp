package com.example.hp.movieapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hp.movieapp.R;
import com.example.hp.movieapp.models.Movie;
import com.example.hp.movieapp.utilities.Listener;

public class MainActivity extends AppCompatActivity implements Listener{
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        MainActivityFragment mMainFragment = new MainActivityFragment();
        mMainFragment.setListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.flm, mMainFragment, "").commit();

        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
           /* if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }*/
        } else {
            mTwoPane = false;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedMovie(Movie movie) {
        if (!mTwoPane) {
            Intent intent = new Intent(this, DetailActivity.class);
            Bundle extras= new Bundle();
            extras.putParcelable("movie",movie);
            intent.putExtras(extras);
            startActivity(intent);
        } else {
            //Case Two-PAne
            DetailActivityFragment mDetailsFragment= new DetailActivityFragment();
            Bundle extras= new Bundle();
            extras.putParcelable("movie",movie);
            mDetailsFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, mDetailsFragment,"").commit();
        }
    }
}
