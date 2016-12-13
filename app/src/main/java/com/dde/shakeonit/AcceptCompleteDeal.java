package com.dde.shakeonit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Eli on 11/26/2016.
 */

public class AcceptCompleteDeal extends AsyncTask<Void, Void, String> {

    private Context mContext;
    public SharedPreferences prefs;

    private String mEndpoint;
    private String mDealId;

    public AcceptCompleteDeal(Context context, String endpoint, String dealId){
        this.mContext = context;
        this.mEndpoint = endpoint;
        this.mDealId = dealId;
    }

    @Override
    public void onPreExecute(){
        super.onPreExecute();
        prefs = mContext.getSharedPreferences("com.dde.shakeonit", Context.MODE_PRIVATE);
    }

    @Override
    protected String doInBackground(Void... params) {
        URL url;
        HttpURLConnection httpURLConnection = null;

        String username = prefs.getString("currentAccount", "");

        String stuff = "";
        String requestUrl = "http://5b2a108a.ngrok.io/deals/" + mEndpoint +"?id=" + mDealId + "&username=" + username;
        //String requestUrl = "https://httpbin.org/get";
        Log.d("ACCEPT DEAL URL", requestUrl);
        try {
            url = new URL(requestUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setDoInput(true);
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            stuff = readStream(in);
            Log.d("HTTP Response", stuff);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        finally {
            httpURLConnection.disconnect();
        }
        return stuff;
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
