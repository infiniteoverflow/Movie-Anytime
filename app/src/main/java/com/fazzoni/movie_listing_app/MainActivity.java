package com.fazzoni.movie_listing_app;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    String jsonResponse = "";
    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;
    URL url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetJsonData task = new GetJsonData();
        task.execute("https://api.themoviedb.org/3/movie/popular?api_key=78b07df68ec17f6b35752939f762a28e&id=3");

    }

    // Class for getting the JSON Data from the website and set the layouts

    public class GetJsonData extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            try {

                if(urls[0] == null) {
                    return jsonResponse;
                }
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if(urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray results = jsonObject.optJSONArray("results");
                Log.i("API data",results.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String readFromStream(InputStream inputStream) {
            StringBuilder output = new StringBuilder();

            if(inputStream!=null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);

                try {
                    String line = reader.readLine();
                    while(line != null) {
                        output.append(line);
                        line = reader.readLine();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return output.toString();
        }
    }
}

