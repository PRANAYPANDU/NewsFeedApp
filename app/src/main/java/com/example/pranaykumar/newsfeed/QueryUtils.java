package com.example.pranaykumar.newsfeed;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRANAYKUMAR on 24-03-2017.
 */

public class QueryUtils {
    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils(){
    }
    public static List<News> fetchNews(String requestUrl)
    {
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        jsonResponse = makeHttpRequest(url);
        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<News>news = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return news;
    }

    private static List<News> extractFeatureFromJson(String jsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding News to
        List<News> news = new ArrayList<>();
        try {
            JSONObject baseJson = new JSONObject(jsonResponse);
            JSONObject response = baseJson.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNews = results.getJSONObject(i);
                String headings = currentNews.getString("webTitle");
                String date = currentNews.getString("webPublicationDate");
                String time = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");

                News newsFeed = new News(headings, date, time, url);
                news.add(newsFeed);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }

    private static String makeHttpRequest(URL url) {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i(LOG_TAG,"Connection opened");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.i(LOG_TAG,"Connected");

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.i(LOG_TAG,"got responseCOde"+urlConnection.getResponseCode());
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (line != null) {
                output.append(line);
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
}
