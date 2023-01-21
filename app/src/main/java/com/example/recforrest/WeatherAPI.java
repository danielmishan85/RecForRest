package com.example.recforrest;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    double temperature;

    public static WeatherAPI getWeatherDataForCity(String city) {

        String apiKey="f501c3b6c235bca43062ed483367a3d9";


        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        // Create a new OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Create a new request using the GET method
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Use an Executor to make the API call in the background
        Executor executor = Executors.newSingleThreadExecutor();
        Future<WeatherAPI> future = ((ExecutorService) executor).submit(new Callable<WeatherAPI>() {
            @Override
            public WeatherAPI call() throws Exception {
                try {
                    // Make the API call using the client and get the response
                    Response response = client.newCall(request).execute();

                    // Check the status code of the response to make sure the request was successful
                    if (response.code() == 200) {
                        // Get the weather data from the API response
                        String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody);

                        WeatherAPI information = new WeatherAPI();
                        information.icon = updateIcon(json.getJSONArray("weather").getJSONObject(0).getInt("id"));
                        information.temperature = json.getJSONObject("main").getDouble("temp")-273.27;


                        return information;
                    } else {
                        // An error occurred, return null
                        return null;
                    }
                } catch (IOException | JSONException e) {
                    // An error occurred, return null
                    return null;
                }
            }
        });

        try {
            // Wait for the background task to complete and return the result
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            // An error occurred, return null
            return null;
        }
    }



    public static String updateIcon(int state) {
        if (state >= 200 && state <= 232) {
            return "thunderstorm1";
        } else if (state >= 300 && state <= 321) {
            return "lightrain";
        } else if (state >= 500 && state <= 531) {
            return "thunderstorm1";
        } else if (state >= 600 && state <= 622) {
            return "snow1";
        } else if (state >= 701 && state <= 781) {
            return "fog";
        } else if (state == 800) {
            return "sunny";
        } else if (state == 801 || state == 802) {
            return "cloudy";
        } else if (state == 803 || state == 804) {
            return "overcast";
        } else if (state >= 900 && state <= 906) {
            return "thunderstorm2";
        } else {
            return "launcher";
        }
    }

}
