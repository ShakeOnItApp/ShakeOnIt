package com.dde.shakeonit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by Eli on 11/21/2016.
 */

public class PopulateDeals extends AsyncTask<Void, Void, String> {

    private Context mContext;
    public SharedPreferences prefs;

    public PopulateDeals(Context context){
        this.mContext = context;
    }

    @Override
    public void onPreExecute(){
        super.onPreExecute();
        prefs = mContext.getSharedPreferences("com.dde.shakeonit", Context.MODE_PRIVATE);
    }

    @Override
    protected String doInBackground(Void... nothing) {
        URL url;
        HttpURLConnection httpURLConnection = null;

        String username = prefs.getString("currentAccount", "");

        String stuff = "";
        String requestUrl = "http://5b2a108a.ngrok.io/deals/participants/" + username;
        //String requestUrl = "https://httpbin.org/get";
        try {
            url = new URL(requestUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
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
    @NonNull
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