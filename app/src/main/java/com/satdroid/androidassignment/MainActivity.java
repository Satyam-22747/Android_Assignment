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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

 public class MainActivity extends AppCompatActivity {

     OkHttpClient client;
     Gson gson;
     TextView title_Tv, description_Tv;
     ProgressBar  progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title_Tv=findViewById(R.id.title_generated_tv);
        description_Tv=findViewById(R.id.description_tv);
        progressBar=findViewById(R.id.pgbar);
        String url="https://run.mocky.io/v3/f4613593-a726-4908-84cf-08b5b96c4a57";
        client = new OkHttpClient();
        gson = new Gson();
        progressBar.setVisibility(View.VISIBLE);
        fetchJsonData();
    }
     private void fetchJsonData() {
         String url = "https://run.mocky.io/v3/f4613593-a726-4908-84cf-08b5b96c4a57";

         Request request = new Request.Builder()
                 .url(url)
                 .build();
         client.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 e.printStackTrace();
                 Toast.makeText(MainActivity.this, "Api response Fail", Toast.LENGTH_SHORT).show();
                 progressBar.setVisibility(View.GONE);

             }
             @Override
             public void onResponse(Call call, Response response) throws IOException {
                 if (response.isSuccessful() && response.body() != null) {
                     String responseBody = response.body().string();
                     ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);

                     String content = apiResponse.getChoices().get(0).getMessage().getContent();
                     JsonObject contentJson = JsonParser.parseString(content).getAsJsonObject();
                     String title = contentJson.get("title").getAsString();
                     String description = contentJson.get("description").getAsString();
                     runOnUiThread(() -> {
                         title_Tv.setText(title);
                         description_Tv.setText(description);
                         progressBar.setVisibility(View.GONE);
                     });
                 }
                 else {
                     Toast.makeText(MainActivity.this, "Response fail", Toast.LENGTH_SHORT).show();
                     progressBar.setVisibility(View.GONE);
                 }
             }
         });
     }
}