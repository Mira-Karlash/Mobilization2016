package com.nicecode.mobilization2016;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mira on 02.04.2016.
 */
public class MainActivity extends ActionBarActivity {

    public static String LOG_TAG = "my_log";
    public String[][] mas_artists;
    public String[][] mas_genres;
    BoxAdapter boxAdapter;
    ListView lvMain;
    Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ParseTask().execute();

        // настраиваем список
        lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("name", mas_artists[position][1]);
                intent.putExtra("image", mas_artists[position][6]);
                String GENRES ="";
                for(int i =0; i<mas_genres.length; i++){
                    if(mas_artists[position][0].equals(mas_genres[i][0])){
                        GENRES+=mas_genres[i][1]+" ";
                    }
                }
                intent.putExtra("genres", GENRES);
                intent.putExtra("tracks", mas_artists[position][2]);
                intent.putExtra("albums", mas_artists[position][3]);
                intent.putExtra("description", mas_artists[position][4]);
                intent.putExtra("link", mas_artists[position][7]);
                startActivity(intent);
            }
        });
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ParseTask().execute();
            }
        });


    }



    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://cache-default02d.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d(LOG_TAG, strJson);


            JSONArray artists = null;

            try {
                artists = new JSONArray(strJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();

            int size_mas_genres = 0;

            try {
                for (int i = 0; i < artists.length(); i++) {   //понять сколько записей будет в стилях
                    JSONObject artist = null;
                    try {
                        artist = artists.getJSONObject(i);

                        JSONArray genres = artist.getJSONArray("genres");
                        for (int j = 0; j < genres.length(); j++) {
                            size_mas_genres++;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                mas_artists = new String[artists.length()][8];
                mas_genres = new String[size_mas_genres][2];

                int counter = 0;

                for (int i = 0; i < artists.length(); i++) {
                    JSONObject artist = null;
                    try {
                        artist = artists.getJSONObject(i);

                        mas_artists[i][0] = artist.getString("id");
                        mas_artists[i][1] = artist.getString("name");

                        JSONArray genres = artist.getJSONArray("genres");
                        for (int j = 0; j < genres.length(); j++) {
                            mas_genres[counter][0] = artist.getString("id");
                            mas_genres[counter][1] = genres.getString(j);
                            counter++;
                        }
                        mas_artists[i][2] = artist.getString("tracks");
                        mas_artists[i][3] = artist.getString("albums");
                        mas_artists[i][4] = artist.getString("description");

                        JSONObject cover = artist.getJSONObject("cover");
                        mas_artists[i][5] = cover.getString("small");
                        mas_artists[i][6] = cover.getString("big");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        mas_artists[i][7] = artist.getString("link");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mas_artists[i][7] = "0";
                    }

                }
                // создаем адаптер

                boxAdapter = new BoxAdapter(MainActivity.this, mas_artists, mas_genres);
                lvMain.setAdapter(boxAdapter);
                button.setVisibility(View.GONE);


            } catch (NullPointerException e) {
                button.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "бее", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }
    }
}