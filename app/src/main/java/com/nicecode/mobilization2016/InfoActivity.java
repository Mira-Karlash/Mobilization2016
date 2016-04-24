package com.nicecode.mobilization2016;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by Mira on 24.04.2016.
 */
public class InfoActivity extends ActionBarActivity{

    TextView name;
    ImageView image;
    TextView genres;
    TextView tracks;
    TextView albums;
    TextView decription;
    TextView link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
    }

    private void init(){
        initToolbar();
        initTextView();
        initImageView();
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
            setTitle(" ");

        }
        name = (TextView) findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("name"));

    }

    private void initTextView(){
        genres = (TextView) findViewById(R.id.genres);
        genres.setText(getIntent().getStringExtra("genres"));
    }
    private void initImageView(){
        //image = (ImageView) findViewById(R.id.image_view_big);
        Picasso.with(InfoActivity.this).load(getIntent().getStringExtra("image")).into((ImageView) findViewById(R.id.image_view_big));
    }

}
