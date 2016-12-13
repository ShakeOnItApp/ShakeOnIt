package com.dde.shakeonit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eli on 11/26/2016.
 */

public class Participant {
    private String mUsername;
    private String mAccepted;
    private String mCompleted;

    public Participant(JSONObject object){
        if(!object.isNull("username"))
            this.mUsername = object.optString("username", "");
        if (!object.isNull("accepted"))
            this.mAccepted = object.optString("accepted", "");
        if (!object.isNull("completed"))
            this.mCompleted = object.optString("completed", "");
    }


    public String getUsername() {
        return mUsername;
    }

    public String getAccepted() {
        return mAccepted;
    }

    public String getCompleted() {
        return mCompleted;
    }
}
