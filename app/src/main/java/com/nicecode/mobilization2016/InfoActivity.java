package com.nicecode.mobilization2016;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by Mira on 24.04.2016.
 */
public class InfoActivity extends ActionBarActivity{

    TextView name;
    TextView genres;
    TextView tracks_and_albums;
    TextView description;
    TextView link;
    private Artist artist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init(){
        artist = (Artist) getIntent().getSerializableExtra("artist");
        initToolbar();
        initTextView();
        initImageView();
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
            setTitle(" ");

            toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });

        }
        name = (TextView) findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("name"));

    }



    private void initTextView(){

        name = (TextView) findViewById(R.id.name);
        name.setText(artist.getName());

        genres = (TextView) findViewById(R.id.genres);
        StringBuilder sb = new StringBuilder();
        for (String genre : artist.getGenres()) {
            sb.append(genre+" ");
        }

        genres.setText(sb);


        tracks_and_albums = (TextView) findViewById(R.id.tracks_and_albums);
        tracks_and_albums.setText(artist.getAlbums()+" альбомов  ·  "+artist.getTracks()+" песен");

        description = (TextView) findViewById(R.id.description);
        description.setText(artist.getDescription());

        link = (TextView) findViewById(R.id.link);
        if (artist.getLink().equals("")) {
            link.setVisibility(View.GONE);
        } else link.setText(artist.getLink());

    }
    private void initImageView(){
        Picasso.with(InfoActivity.this).load(artist.getImage_big()).into((ImageView) findViewById(R.id.image_view_big));
    }

}
