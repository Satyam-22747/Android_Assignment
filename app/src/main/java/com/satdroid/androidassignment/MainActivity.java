 package com.satdroid.androidassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;


import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

 public class MainActivity extends AppCompatActivity {
     TextView title_Tv, description_Tv;
     ProgressBar  progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title_Tv=findViewById(R.id.title_generated_tv);
        description_Tv=findViewById(R.id.description_tv);
        progressBar=findViewById(R.id.pgbar);
        progressBar.setVisibility(View.VISIBLE);

        String url="https://run.mocky.io/v3/f4613593-a726-4908-84cf-08b5b96c4a57";

        new FetchDataTask().execute(url);
    }
     private class FetchDataTask extends AsyncTask<String, Void, String> {
         @Override
         protected String doInBackground(String... urls) {
             String urlString = urls[0];
             StringBuilder result = new StringBuilder();
             try {
                 URL url = new URL(urlString);
                 HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                 urlConnection.setRequestMethod("GET");

                 BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                 String line;

                 while ((line = reader.readLine()) != null) {
                     result.append(line);
                 }

                 reader.close();
                 urlConnection.disconnect();

             } catch (IOException e) {
                 e.printStackTrace();
             }

             return result.toString();
         }

         @Override
         protected void onPostExecute(String result) {
             try {
                 JSONObject jsonResponse = new JSONObject(result);
                 JSONArray choicesArray = jsonResponse.getJSONArray("choices");
                 JSONObject firstChoice = choicesArray.getJSONObject(0);
                 JSONObject messageObject = firstChoice.getJSONObject("message");
                 String content = messageObject.getString("content");

                 JSONObject contentJson = new JSONObject(content);
                 String title = contentJson.getString("title");
                 String description = contentJson.getString("description");

                 title_Tv.setText(title);
                 description_Tv.setText(description);
                 progressBar.setVisibility(View.GONE);
             } catch (JSONException e) {
                 e.printStackTrace();
                 progressBar.setVisibility(View.GONE);
             }
         }
     }
}