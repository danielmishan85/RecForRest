package com.example.recforrest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherAPI {

    String icon;
    static double temperature;
    static String fragmentId;
    public static class GetWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            fragmentId = params[1];
            Log.w("TAG", "city: " + city);

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

//
//    public static String updateIcon(int state) {
//        if (state >= 200 && state <= 232) {
//            return "thunderstorm1";
//        } else if (state >= 300 && state <= 321) {
//            return "lightrain";
//        } else if (state >= 500 && state <= 531) {
//            return "thunderstorm1";
//        } else if (state >= 600 && state <= 622) {
//            return "snow1";
//        } else if (state >= 701 && state <= 781) {
//            return "fog";
//        } else if (state == 800) {
//            return "sunny";
//        } else if (state == 801 || state == 802) {
//            return "cloudy";
//        } else if (state == 803 || state == 804) {
//            return "overcast";
//        } else if (state >= 900 && state <= 906) {
//            return "thunderstorm2";
//        } else {
//            return "launcher";
//        }
//    }

}
