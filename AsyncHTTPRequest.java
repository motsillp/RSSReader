package com.example.JSONTest;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 1391758 on 2017/04/20.
 * Retrieves the source of a website as a string
 */
public abstract class AsyncHTTPRequest extends AsyncTask<String, String,
        String> {
    String address;
    ContentValues parameters;
    public AsyncHTTPRequest(String address, ContentValues parameters) {
        this.address = address;
        this.parameters = parameters;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            //connection.setRequestProperty("Content-Type",
            //        "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            if (parameters.size() > 0) {
                Uri.Builder builder = new Uri.Builder();
                for (String s : parameters.keySet()) {
                    builder.appendQueryParameter(s,
                            parameters.getAsString(s));
                }
                String query = builder.build().getEncodedQuery();
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String response = "";
            String text = br.readLine();
            while (text != null)
            {
                response = response + br.readLine();
                text = br.readLine();
            }

            br.close();
            return response;
        } catch (Exception e) {
            Log.d("THIS WAY -->", "LOOK HERE");
            e.printStackTrace();
            return "failure";
        }
    }
    @Override
    protected abstract void onPostExecute(String output);
}