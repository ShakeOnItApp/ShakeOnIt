package com.dde.shakeonit;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityListFragment extends ListFragment {


    public DealAdapter dealAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        try {
            addDealFromJSON();
        }catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        dealAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        launchDealDetailActivity(MainActivity.FragmentToLoad.VIEW, position);
    }

    public void launchDealDetailActivity(MainActivity.FragmentToLoad ftl, int position){

        //get deal at selected position in list
        Deal deal = (Deal) getListAdapter().getItem(position);

        //create new intent that launches DealDetailActivity
        Intent intent = new Intent(getActivity(), DealDetailActivity.class);
        //package deal information in intent
        intent.putExtra(MainActivity.DEAL_TITLE_EXTRA, deal.getTitle());
        intent.putExtra(MainActivity.DEAL_TEXT_EXTRA, deal.getDescription());
        intent.putExtra(MainActivity.DEAL_LOCATION_EXTRA, deal.getLocation());
        intent.putExtra(MainActivity.DEAL_PARTICIPANTS_EXTRA, deal.getParticipants());
        intent.putExtra(MainActivity.DEAL_TERMS_EXTRA, deal.getTerms());
        intent.putExtra(MainActivity.DEAL_ID_EXTRA,deal.getId());
        intent.putExtra(MainActivity.DEAL_ACCEPTED_EXTRA, deal.getAccepted());
        intent.putExtra(MainActivity.DEAL_COMPLETED_EXTRA, deal.getCompleted());


        switch(ftl){
            case VIEW:
                intent.putExtra(MainActivity.FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLoad.VIEW);
                break;
            case NEW:
                intent.putExtra(MainActivity.FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLoad.NEW);
                break;
        }

        //launch intent
        startActivity(intent);
    }

    public void addDealFromJSON() throws ExecutionException, InterruptedException, JSONException {
        JSONObject getResponse; //this is the result of the get request its an object containing an array of objects
        JSONArray jsonArray;    //this is the value of the above object
        JSONObject jsonObject;

        ArrayList<Deal> newDeals = new ArrayList<>();

        PopulateDeals populateDeals = new PopulateDeals(getContext());
        populateDeals.execute();
        String jsonDeals = populateDeals.get();

        getResponse = new JSONObject(jsonDeals);
        jsonArray = getResponse.getJSONArray("data");
        if(jsonArray != null) {
            int length = jsonArray.length();
            for(int i=0; i< length; i++) {
                jsonObject= jsonArray.getJSONObject(i);
                newDeals.add(new Deal(jsonObject));
            }
            if(dealAdapter == null) {
                dealAdapter = new DealAdapter(getContext(), newDeals);
                setListAdapter(dealAdapter);
            }
            else {
                dealAdapter.clear();
                dealAdapter.addAll(newDeals);

            }

        }
    }
}
