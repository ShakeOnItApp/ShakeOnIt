package com.dde.shakeonit;


import android.util.Log;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by Eli on 11/10/2016.
 */

public class Deal {
    private String author, title, description, location;
    private String terms, participants, dealId;
    private String accepted, completed;
    private long dateCreatedMilli;



    public Deal(JSONObject json){
        if(!json.isNull("author"))
            this.author = json.optString("author", "");
        if (!json.isNull("title"))
            this.title = json.optString("title", "");
        if (!json.isNull("description"))
            this.description = json.optString("description", "");
        if (!json.isNull("location"))
            this.location = json.optString("location", "");
        if (!json.isNull("terms"))
            this.terms = json.optString("terms", "");
        if (!json.isNull("participants"))
            this.participants = json.optString("participants", "");
        if (!json.isNull("_id")) {
            this.dealId = json.optString("_id", "");
        }
        if (!json.isNull("accepted")){
            this.accepted = json.optString("accepted", "");
        }
        if(!json.isNull("completed")){
            this.completed = json.optString("completed","");
        }
        if (!json.isNull("timeCreated")) {
            String JSONdate = json.optString("timeCreated", null);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date dateCreated = null;
            try {
                dateCreated = dateFormat.parse(JSONdate);
            } catch (ParseException e) {
                e.printStackTrace();
                System.exit(0);
            }
            this.dateCreatedMilli = dateCreated.getTime();
            //this.dateCreatedMilli = System.currentTimeMillis();
        }
    }

    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public String getAuthor() {return author;}
    public String getLocation() {return location;}
    public long getDate(){ return dateCreatedMilli; }
    public String getId(){ return dealId; }

    public String getParticipants() {return participants; }
    public String getTerms() {return terms; }

    @Override
    public String toString() {
        return "ID: " + dealId + " Title: " + title + " Description: " + description + " Date: ";
    }

    public String getAccepted() {
        return accepted;
    }

    public String getCompleted() {
        return completed;
    }
}
