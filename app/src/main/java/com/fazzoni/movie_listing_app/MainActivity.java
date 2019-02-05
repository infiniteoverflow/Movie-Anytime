package com.fazzoni.movie_listing_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageButton;

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

    ImageButton pop1,pop2,pop3,act1,act2,act3,adv1,adv2,adv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pop1 = findViewById(R.id.popImage1);
        pop2 = findViewById(R.id.popImage2);
        pop3 = findViewById(R.id.popImage3);

        act1 = findViewById(R.id.actMovie1);
        act2 = findViewById(R.id.actMovie2);
        act3 = findViewById(R.id.actMovie3);

        adv1 = findViewById(R.id.advMovie1);
        adv2 = findViewById(R.id.advMovie2);
        adv3 = findViewById(R.id.advMovie3);

        GetJsonData task = new GetJsonData();
        task.execute("https://api.themoviedb.org/3/movie/popular?api_key=78b07df68ec17f6b35752939f762a28e&id=3");

    }

    // Class for getting the JSON Data from the website and set the layouts

    public class GetJsonData extends AsyncTask<String,Void,String> {

        String imageUrl;
        Bitmap bmp1,bmp2,bmp3;

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

                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray results = jsonObject.optJSONArray("results");

                JSONObject details = results.optJSONObject(0);
                imageUrl = "http://image.tmdb.org/t/p/w185\\";
                imageUrl += details.getString("poster_path");
                URL url = new URL(imageUrl);
                Log.i("API Data1",imageUrl);
                bmp1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                details = results.optJSONObject(1);
                imageUrl = "http://image.tmdb.org/t/p/w185\\";
                imageUrl += details.getString("poster_path");
                url = new URL(imageUrl);
                Log.i("API Data1",imageUrl);
                bmp2 = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                details = results.optJSONObject(2);
                imageUrl = "http://image.tmdb.org/t/p/w185\\";
                imageUrl += details.getString("poster_path");
                url = new URL(imageUrl);
                Log.i("API Data1",imageUrl);
                bmp3 = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                /*for(int i=0;i<=2;i++) {
                    JSONObject details = results.optJSONObject(i);
                    imageUrl = "http://image.tmdb.org/t/p/w185\\";
                    imageUrl += details.getString("poster_path");
                    Log.i("API Datax",imageUrl);

                    if(i==0) {
                        URL url = new URL(imageUrl);
                        Log.i("API Data1",imageUrl);
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        pop1.setImageBitmap(bmp);
                    }
                    else if(i==1) {
                        URL url = new URL(imageUrl);
                        Log.i("API Data2",imageUrl);
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        pop2.setImageBitmap(bmp);
                    }
                    else {
                        URL url = new URL(imageUrl);
                        Log.i("API Data3",imageUrl);
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        pop3.setImageBitmap(bmp);
                    }
                }*/
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

                Log.i("API Data",results.toString());
                pop1.setImageBitmap(bmp1);
                pop2.setImageBitmap(bmp2);
                pop3.setImageBitmap(bmp3);
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

