package com.example.recforrest;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {

    static double temperature;
    static String fragmentId;
    public static class GetWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            fragmentId = params[1];

            String apiKey = "457824bee154cd1f95301371e98017db";
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

            try {
                URL apiEndpoint = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiEndpoint.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream responseBody = connection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    BufferedReader reader = new BufferedReader(responseBodyReader);
                    StringBuilder builder = new StringBuilder();
                    String line = reader.readLine();
                    while (line != null) {
                        builder.append(line);
                        line = reader.readLine();
                    }
                    return builder.toString();
                } else {
                    Log.d("TAG", "Error: " + responseCode);
                    return null;
                }
            } catch (IOException e) {
                Log.d("TAG", "Error: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Update the UI with the temperature value
            try {
                JSONObject json = new JSONObject(result);
                temperature = json.getJSONObject("main").getDouble("temp");
                if(fragmentId.equals("my posts"))
                {
                    MyPostInfoFragment.changeIconAccordingToTemp(temperature);
                }
                else if (fragmentId.equals("all posts"))
                {
                    PostInfoFragment.changeIconAccordingToTemp(temperature);
                }



            } catch (JSONException e) {
                Log.w("TAG", "Error: " + e.getMessage());
            }
        }
    }

}
