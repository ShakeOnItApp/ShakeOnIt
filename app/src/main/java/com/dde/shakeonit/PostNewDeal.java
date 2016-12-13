package com.dde.shakeonit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Eli on 11/18/2016.
 */

public class PostNewDeal extends AsyncTask<String, Void, Void> {

    private Context mContext;
    public SharedPreferences prefs;

    public PostNewDeal(Context context){
        this.mContext = context;
    }

    @Override
    public void onPreExecute(){
        super.onPreExecute();
        prefs = mContext.getSharedPreferences("com.dde.shakeonit", Context.MODE_PRIVATE);
    }

    @Override
    protected Void doInBackground(String... strings) {

        String author = prefs.getString("currentAccount", "");

        //this code handles parsing the user inputted participants into JSON
        List<String> items = Arrays.asList(strings[0].split("\\s*,\\s*"));

        //this code adds all the participants to an arraylist of JSON objects
        ArrayList<JSONObject> listOfObj = new ArrayList<>();
        Iterator<String> it = items.listIterator();
        while(it.hasNext()){
            JSONObject ob = new JSONObject();
            try {
                ob.put("username", it.next());
                ob.put("accepted", "null");
                ob.put("completed", "null");
            }catch(JSONException e){
                e.printStackTrace();
                System.exit(0);
            }
            listOfObj.add(ob);
        }
        //this code adds the author to the same list
        JSONObject creator = new JSONObject();
        try {
            creator.put("username", author);
            creator.put("accepted", "accepted");
            creator.put("completed", "null");
        }catch (JSONException e){
            e.printStackTrace();
            System.exit(0);
        }
        listOfObj.add(creator);

        //this code converts the array list into a JSONArray
        JSONArray participants = new JSONArray(listOfObj);
        //String jsonStringParticipants =  participants.toString();
        //Log.d("JSON PARTICIPANTS ARRAY", jsonStringParticipants);

        Map<String, String> mapToJSON = new HashMap<>();
        mapToJSON.put("author", author);
        mapToJSON.put("title", strings[1]);
        mapToJSON.put("description", strings[2]);
        mapToJSON.put("terms", strings[3]);
        mapToJSON.put("location", strings[4]);
        mapToJSON.put("accepted", "NA");
        mapToJSON.put("completed", "NA");

        JSONObject jsonContent = new JSONObject(mapToJSON);
        try {
            jsonContent.put("participants", participants);
        }catch(JSONException e){
            e.printStackTrace();
            System.exit(0);
        }

        String postContent = jsonContent.toString();
        int postContentLength = postContent.getBytes().length;

        URL url;
        HttpURLConnection httpURLConnection = null;

        String requestUrl = "http://5b2a108a.ngrok.io/deals";
        try {
            url = new URL(requestUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Content-Length", "" + postContentLength);

            OutputStream wr = new BufferedOutputStream(httpURLConnection.getOutputStream(), postContentLength);
            wr.write(postContent.getBytes("UTF-8"));
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            String stuff = readStream(in);
            Log.d("HTTP Response", stuff);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        finally {
            httpURLConnection.disconnect();
        }
        return null;
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
}
