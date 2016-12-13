package com.dde.shakeonit;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.lang.String;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eli on 11/22/2016.
 */


public class VerifyUser extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        Log.d("TEST TEST TEST", "VerifyUser START");
        Map<String, String> mapToJSON = new HashMap<>();
        mapToJSON.put("username", strings[0]);
        mapToJSON.put("password", strings[1]);

        JSONObject jsonContent = new JSONObject(mapToJSON);
        String unameAndPwordJSON = jsonContent.toString();
        int postContentLength = unameAndPwordJSON.getBytes().length;

        String response = httpPost(postContentLength, unameAndPwordJSON);

        return response;
    }
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
    private String httpPost(int unameAndPasswordLength, String postContent){
        URL url;
        HttpURLConnection httpURLConnection = null;
        String httpResponse = "";

        String requestUrl = "http://5b2a108a.ngrok.io/users/verifyuser";
        try {
            url = new URL(requestUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Content-Length", "" + unameAndPasswordLength);

            OutputStream wr = new BufferedOutputStream(httpURLConnection.getOutputStream(), unameAndPasswordLength);
            wr.write(postContent.getBytes("UTF-8"));
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            httpResponse = readStream(in);
            Log.d("HTTP Response", httpResponse);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        finally {
            httpURLConnection.disconnect();
        }

        JSONObject response = null;
        try {
            response = new JSONObject(httpResponse);
        }catch(JSONException e){
            e.printStackTrace();
            System.exit(0);
        }
        String info = response.optString("info");

        return info;
    }
}
