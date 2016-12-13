package com.dde.shakeonit;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class DealViewFragment extends Fragment {

    Intent mIntent;
    String mDealAccepted;
    String mDealCompleted;
    Participant mCurrentUser;
    String mAuthor;
    ArrayList<Participant> mParticipants;
    public SharedPreferences prefs;

    public DealViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("com.dde.shakeonit", Context.MODE_PRIVATE);
        mAuthor = prefs.getString("currentAccount", "");

        mIntent = getActivity().getIntent();
        String participantList = mIntent.getExtras().getString(MainActivity.DEAL_PARTICIPANTS_EXTRA);
        mDealAccepted = mIntent.getExtras().getString(MainActivity.DEAL_ACCEPTED_EXTRA);
        mDealCompleted = mIntent.getExtras().getString(MainActivity.DEAL_COMPLETED_EXTRA);
        Log.d("ACCEPTED AND COMPLETED", mDealAccepted + " " + mDealCompleted);
        JSONArray participants = null;
        try {
            participants = new JSONArray(participantList);
        } catch (JSONException e) {
            e.printStackTrace();
            System.exit(0);
        }
        for(int i = 0; i < participants.length(); i++) {
            String checkAuthor = "";
            try {
                mCurrentUser =  new Participant(participants.getJSONObject(i));
                checkAuthor = mCurrentUser.getUsername();
            } catch (JSONException e) {
                e.printStackTrace();
                System.exit(0);
            }
            if(checkAuthor.equals(mAuthor)){
                return;
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String[] accepted = {""};
        final String[] completed = {""};
        View fragmentLayout = inflater.inflate(R.layout.fragment_deal_view, container, false);

        TextView title = (TextView) fragmentLayout.findViewById(R.id.dealViewTitle);
        TextView text = (TextView) fragmentLayout.findViewById(R.id.dealViewText);
        TextView location = (TextView) fragmentLayout.findViewById(R.id.dealViewLocation);
        TextView terms = (TextView) fragmentLayout.findViewById(R.id.dealViewTerms);
        TextView participants = (TextView) fragmentLayout.findViewById(R.id.dealViewParticipants);

        title.setText(mIntent.getExtras().getString(MainActivity.DEAL_TITLE_EXTRA));
        text.setText(mIntent.getExtras().getString(MainActivity.DEAL_TEXT_EXTRA));
        location.setText(mIntent.getExtras().getString(MainActivity.DEAL_LOCATION_EXTRA));
        terms.setText(mIntent.getExtras().getString(MainActivity.DEAL_TERMS_EXTRA));
        participants.setText(handleJSONParticipants());

        final String dealId = mIntent.getExtras().getString(MainActivity.DEAL_ID_EXTRA);

        final LinearLayout acceptButtons = (LinearLayout) fragmentLayout.findViewById(R.id.accept_buttons);
        final LinearLayout completeButtons = (LinearLayout) fragmentLayout.findViewById(R.id.complete_buttons);


        Button acceptDealButton = (Button) fragmentLayout.findViewById(R.id.accept_deal_button);
        acceptDealButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        AcceptCompleteDeal accept = new AcceptCompleteDeal(getContext(), "accept", dealId);
                        accept.execute();
                        acceptButtons.setVisibility(View.INVISIBLE);
                        accepted[0] = "true";
                        getActivity().onBackPressed();
                    }
                }
        );
        Button rejectDealButton = (Button) fragmentLayout.findViewById(R.id.reject_deal_button);
        rejectDealButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        AcceptCompleteDeal reject = new AcceptCompleteDeal(getContext(), "reject", dealId);
                        reject.execute();
                        acceptButtons.setVisibility(View.INVISIBLE);
                        accepted[0] = "false";
                        getActivity().onBackPressed();
                    }
                }
        );
        Button completeDealButton = (Button) fragmentLayout.findViewById(R.id.complete_deal_button);
        completeDealButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        AcceptCompleteDeal complete = new AcceptCompleteDeal(getContext(), "complete", dealId);
                        complete.execute();
                        completeButtons.setVisibility(View.INVISIBLE);
                        completed[0] = "true";
                        getActivity().onBackPressed();
                    }
                }
        );
        Button incompleteDealButton = (Button) fragmentLayout.findViewById(R.id.incomplete_deal_button);
        incompleteDealButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        AcceptCompleteDeal incomplete = new AcceptCompleteDeal(getContext(), "incomplete", dealId);
                        incomplete.execute();
                        completeButtons.setVisibility(View.INVISIBLE);
                        completed[0] = "false";
                        getActivity().onBackPressed();
                    }
                }
        );

        String acceptance = mCurrentUser.getAccepted();
        if (acceptance.equals("accepted") || acceptance.equals("rejected") || accepted[0].equals("true") ||accepted[0].equals("false")) {
            acceptButtons.setVisibility(View.INVISIBLE);
        }

        String completeness = mCurrentUser.getCompleted();
        Log.d("NUll REFERENCE TEST", mDealCompleted + completeness);

        if(mDealAccepted.equals("accepted")) {
            if(!mDealCompleted.equals("complete") && !mDealCompleted.equals("incomplete")) {
                if(!mCurrentUser.getCompleted().equals("complete") && !mCurrentUser.getCompleted().equals("incomplete")) {
                    completeButtons.setVisibility(View.VISIBLE);
                }
            }
        }
        else{
            completeButtons.setVisibility(View.INVISIBLE);
        }


        // return the inflated view
        return fragmentLayout;
    }

    public Boolean checkAcceptance(){
        Iterator<Participant> it = mParticipants.listIterator();
        while(it.hasNext()){
            Participant current = it.next();
            if((current.getAccepted().equals("rejected") || current.getAccepted().equals("NA")) && !(current.getUsername().equals(mAuthor)));
                return false;
        }
        return true;
    }

    public String handleJSONParticipants(){
        String rawJSON = mIntent.getExtras().getString(MainActivity.DEAL_PARTICIPANTS_EXTRA);
        //Log.d("PARTICIPANTS LIST", rawJSON);
        JSONArray participantsJSON = null;
        try {
            participantsJSON = new JSONArray(rawJSON);
        }catch(JSONException e){
            e.printStackTrace();
            System.exit(0);
        }

        mParticipants = new ArrayList<>();
        int length = participantsJSON.length();
        for(int i = 0; i < length; i++){
            Participant participant = null;
            try{
                participant =  new Participant(participantsJSON.getJSONObject(i));
            }catch (JSONException e){
                e.printStackTrace();
                System.exit(0);
            }

            mParticipants.add(participant);
        }
        String stringParticipants ="You, ";
        Iterator<Participant> it = mParticipants.listIterator();
        while(it.hasNext()){
            Participant participant = it.next();
            if(!(participant.getUsername().equals(mAuthor))){
                stringParticipants += participant.getUsername() + ", ";
            }
        }
        Log.d("Participants LIST", stringParticipants);
        if(!stringParticipants.equals("")){
            stringParticipants = stringParticipants.substring(0, stringParticipants.length() - 2);
        }
        return stringParticipants;
    }

}
