package com.nicecode.mobilization2016;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mira on 02.04.2016.
 */
public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    public static String LOG_TAG = "my_log";
    BoxAdapter boxAdapter;
    ListView lvMain;
    Button button;
    private ArrayList<Artist> list = new ArrayList<>();
    boolean isFiltred = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            File httpCacheDir = new File(getCacheDir(), "http");
            long httpCacheSize = 25 * 1024 * 1024; // 25 MiB
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            Log.d("CacheDir", httpResponseCacheNotAvailable.toString());
        }
        new ParseTask().execute();

        // настраиваем список
        lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);

                if (isFiltred) {
                    intent.putExtra("artist", BoxAdapter.list.get(position));
                } else {
                    intent.putExtra("artist", list.get(position));
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        lvMain.setTextFilterEnabled(true);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ParseTask().execute();
            }
        });
        initToolbar();


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle(" ");

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");


        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            lvMain.clearTextFilter();
            isFiltred = false;
        } else {
            lvMain.setFilterText(newText);
            isFiltred = true;
        }
        return true;
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
            list = Parse(strJson);
            // создаем адаптер

            boxAdapter = new BoxAdapter(list, MainActivity.this);
            lvMain.setAdapter(boxAdapter);
            button.setVisibility(View.GONE);

        }
        public ArrayList<Artist> Parse (String strJson){
            ArrayList<Artist> list = new ArrayList<>();

            try {
                JSONArray artistsJsonArray = new JSONArray(strJson);
                for (int i = 0; i < artistsJsonArray.length(); i++) {
                    JSONObject artistJsonObject = artistsJsonArray.getJSONObject(i);

                    // получение списка жанров в формате json
                    JSONArray genresJsonArray = artistJsonObject.getJSONArray("genres");
                    ArrayList<String> genresList = new ArrayList<>();

                    // заполнение списка жанорв
                    for (int genrePosition = 0; genrePosition < genresJsonArray.length(); genrePosition++) {
                        genresList.add(genresJsonArray.getString(genrePosition));
                    }

                    Artist artist = new Artist(artistJsonObject.getInt("id"),
                            getJSONObjectString(artistJsonObject, "name"),
                            genresList,
                            artistJsonObject.getInt("tracks"),
                            artistJsonObject.getInt("albums"),
                            getJSONObjectString(artistJsonObject, "link"),
                            getJSONObjectString(artistJsonObject, "description"),
                            getJSONObjectString(artistJsonObject.getJSONObject("cover"), "small"),
                            getJSONObjectString(artistJsonObject.getJSONObject("cover"), "big"));


                    list.add(artist);


                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.w("Log", "invalid json format");
            }

            return list;
        }

        // проверка на наличие строки в объекте json

        private String getJSONObjectString(JSONObject jsonObject, String key) {
            try {
                return jsonObject.getString(key);
            } catch (JSONException ex) {
                Log.w("Log", "can't parse \"" + key + "\" value");
                return "";
            }
        }
    }
}
