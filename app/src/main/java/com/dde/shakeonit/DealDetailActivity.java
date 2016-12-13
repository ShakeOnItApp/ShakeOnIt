package com.dde.shakeonit;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DealDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_detail);

        createAndAddFragment();
    }

    private void createAndAddFragment(){

        Intent intent = getIntent();
        MainActivity.FragmentToLoad fragmentToLoad =
                (MainActivity.FragmentToLoad) intent.getSerializableExtra(MainActivity.FRAGMENT_TO_LOAD_EXTRA);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch(fragmentToLoad) {

            case VIEW:
                DealViewFragment dealViewFragment = new DealViewFragment();
                setTitle(R.string.viewDealDetailFragmentTitle);
                fragmentTransaction.add(R.id.deal_container, dealViewFragment, "DEAL_VIEW_FRAGMENT");
                break;

            case NEW:
                NewDealViewFragment newDealViewFragment = new NewDealViewFragment();
                setTitle("New Deal");
                fragmentTransaction.add(R.id.deal_container, newDealViewFragment, "NEW_DEAL_FRAGMENT");
                break;

        }

        fragmentTransaction.commit();

    }
}
