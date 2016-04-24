package com.nicecode.mobilization2016;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mira on 25.04.2016.
 */
public class Artist implements Serializable {

    private int id;
    private String name;
    private ArrayList<String> genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private String image_small;
    private String image_big;

    public Artist(int id, String name, ArrayList<String> genres, int tracks, int albums, String link, String description, String image_small, String image_big) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.tracks = tracks;
        this.albums = albums;
        this.link = link;
        this.description = description;
        this.image_small = image_small;
        this.image_big = image_big;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public int getTracks() {
        return tracks;
    }

    public int getAlbums() {
        return albums;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_small() {
        return image_small;
    }

    public String getImage_big() {
        return image_big;
    }




}