package com.modnsolutions.bookfinder.utils;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class NetworkUtils {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";
    private static final String START_INDEX = "startIndex";
    public static final int LOAD_INCREMENT = 10;

    /**
     * Search for books from Google Books API using the query string and start index (for paging).
     *
     * @param queryString the title of book to perform search
     * @param startIndex pagination
     * @return
     */
    public static List<JSONObject> searchBook(String queryString, int startIndex) {
        List<JSONObject> results = new LinkedList<>();

        try {
            Uri builtUri = Uri.parse(BASE_URL + "?").buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, String.valueOf(LOAD_INCREMENT))
                    .appendQueryParameter(START_INDEX, String.valueOf(startIndex))
                    .build();

            // Extract required fields and save in book JSONObject.
            JSONObject responseObject = httpService(builtUri);
            JSONArray responseArray = responseObject.getJSONArray("items");
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject object = responseArray.getJSONObject(i);

                JSONObject book = new JSONObject();
                book.put("id", object.getString("id"));
                book.put("title", object.getJSONObject("volumeInfo")
                        .getString("title"));
                book.put("publisher", object.getJSONObject("volumeInfo")
                        .getString("publisher"));
                book.put("publishedDate", object.getJSONObject("volumeInfo")
                        .getString("publishedDate"));
                book.put("imageLinks", object.getJSONObject("volumeInfo")
                        .getJSONObject("imageLinks"));
                book.put("authors", formatJSONArray(object.getJSONObject("volumeInfo")
                        .getJSONArray("authors")));

                results.add(book);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Search for details of book using the booking ID.
     * @param bookId
     * @return
     */
    public static JSONObject bookDetail(String bookId) {
        JSONObject book = new JSONObject();

        try {
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(bookId)
                    .build();

            // Extract required fields and save in book JSONObject.
            JSONObject responseObject = httpService(builtUri);

            book.put("id", responseObject.getString("id"));
            book.put("title", responseObject.getJSONObject("volumeInfo")
                    .getString("title"));
            book.put("publisher", responseObject.getJSONObject("volumeInfo")
                    .getString("publisher"));
            book.put("publishedDate", responseObject.getJSONObject("volumeInfo")
                    .getString("publishedDate"));
            book.put("imageLinks", responseObject.getJSONObject("volumeInfo")
                    .getJSONObject("imageLinks"));
            book.put("authors", formatJSONArray(responseObject.getJSONObject("volumeInfo")
                    .getJSONArray("authors")));
            book.put("pageCount", responseObject.getJSONObject("volumeInfo")
                    .getInt("pageCount"));
            book.put("categories", formatJSONArray(responseObject.getJSONObject("volumeInfo")
                    .getJSONArray("categories")));
            book.put("description", responseObject.getJSONObject("volumeInfo")
                    .getString("description"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return book;
    }

    /**
     * Make http requests to Google Book API.
     * @param uri
     * @return
     * @throws JSONException
     */
    private static JSONObject httpService(Uri uri) throws JSONException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Use a StringBuilder to hold the incoming response.
        StringBuilder builder = new StringBuilder();

        try {
            URL requestURL = new URL(uri.toString());

            // Open Connection
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get InputStream
            InputStream inputStream = urlConnection.getInputStream();

            // Create BufferedReader from the input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0) return null; // Builder was empty.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return new JSONObject(builder.toString());
    }

    /**
     * Format authors in JSONArray into a single continuous string with newline breaks.
     *
     * @param array JSONArray of authors.
     * @return String of authors.
     */
    private static String formatJSONArray(JSONArray array) throws JSONException {
        // Format authors into a continuous string value.
        StringBuilder arrayBuilder = new StringBuilder();
        for (int j = 0; j < array.length(); j++) {
            if (array.length() > 1 && j != array.length() - 1) {
                arrayBuilder.append(array.get(j));
                arrayBuilder.append("\n");
            }

            if (array.length() == 1 || j == array.length() - 1) {
                arrayBuilder.append(array.get(j));
            }
        }

        return arrayBuilder.toString();
    }
}
