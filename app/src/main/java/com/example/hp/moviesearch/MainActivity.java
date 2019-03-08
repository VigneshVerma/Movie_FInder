package com.example.hp.moviesearch;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText etMovieName;
    Button btnSearch;
    TextView tvSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMovieName = findViewById(R.id.etMovieName);
        btnSearch = findViewById(R.id.btnSearch);
        tvSearchResult = findViewById(R.id.tvSearchResult);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mn = etMovieName.getText().toString();
                if (mn.length() == 0)
                {
                    Toast.makeText(MainActivity.this, "Movie Name is empty", Toast.LENGTH_SHORT).show();
                    etMovieName.requestFocus();
                    return;
                }
                MyTask t1 = new MyTask();
                t1.execute("http://www.omdbapi.com/?apikey=d505d8&s=" +mn);
            }
        });
    }

    class MyTask extends AsyncTask<String, Void, String>
    {
        String jsonStr = "";
        String line = "";
        String searchResult = "";

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));



                while ((line = reader.readLine()) != null)
                {

                    jsonStr += line + "\n";
                }


                if (jsonStr != null)
                {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray movieArray = jsonObject.getJSONArray("Search");

                    for (int i = 0; i < movieArray.length(); i++)
                    {
                        JSONObject movie = movieArray.getJSONObject(i);
                        String title = movie.getString("Title");
                        String year = movie.getString("Year");
                        searchResult += title + " " + year + "\n";
                    }

                }

            } catch (Exception e) { }
            return searchResult;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvSearchResult.setText(s);
        }
    }


}
