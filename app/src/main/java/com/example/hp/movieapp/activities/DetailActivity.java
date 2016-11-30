package com.example.hp.movieapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.hp.movieapp.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent sentIntent = getIntent();
        Bundle sentBundle = sentIntent.getExtras();


            DetailActivityFragment mDetailsFragment = new DetailActivityFragment();
        mDetailsFragment.setArguments(sentBundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,mDetailsFragment).commit();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



    }

}
