package com.dde.shakeonit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener /*implements NewDealViewFragment.OnNewDealSubmittedListener*/ {

    public static final String DEAL_ID_EXTRA = "com.dde.shakeonit.Identifier";
    public static final String DEAL_TITLE_EXTRA = "com.dde.shakeonit.Title";
    public static final String DEAL_TEXT_EXTRA = "com.dde.shakeonit.Text";
    public static final String DEAL_LOCATION_EXTRA = "com.dde.shakeonit.Location";
    public static final String DEAL_PARTICIPANTS_EXTRA = "com.dde.shakeonit.Participants";
    public static final String DEAL_TERMS_EXTRA = "com.dde.shakeonit.Terms";
    public static final String DEAL_ACCEPTED_EXTRA = "com.dde.shakeonit.Accepted";
    public static final String DEAL_COMPLETED_EXTRA = "com.dde.shakeonit.Completed";
    public static final String FRAGMENT_TO_LOAD_EXTRA = "com.dde.shakeonit.Fragment_To_Load";


    public DrawerLayout mDrawerLayout;
    public RecyclerView mRecyclerView;                           // Declaring RecyclerView
    public RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    public RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager

    public enum FragmentToLoad{VIEW, NEW}

    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sets the title of main screen with list of deals
        setTitle(R.string.deal_list_name);

        //function containing all the hamburger menu code
        setupHamburgerMenu();

        //function containing swipe refresh layout code
        setupSwipeRefreshLayout();

        //onClick floating action button launch new deal activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startNewDeal = new Intent(view.getContext(), DealDetailActivity.class);
                startNewDeal.putExtra(MainActivity.FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLoad.NEW);
                startActivity(startNewDeal);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshAction();
    }

    @Override
    public void onRefresh() {
        //Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        refreshAction();

    }
    public void refreshAction(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivityListFragment frag = (MainActivityListFragment) getSupportFragmentManager().findFragmentById(R.id.mainActivityFrag);
                if(frag != null) {
                    try {
                        frag.addDealFromJSON();
                    } catch (ExecutionException | JSONException | InterruptedException e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
                if(frag != null)
                    frag.dealAdapter.notifyDataSetChanged();

            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_logout){
            logout();
            return true;
        }
        else if(id == android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    public void setupHamburgerMenu(){
        String NAME = "Eli Barton";
        String EMAIL = "email@email.com";
        int[] ICONS = {R.drawable.ic_check_circle_black_24dp,R.drawable.ic_check_circle_black_24dp};
        String[] TITLES = {"Associates", "PastDeals"};
        int PROFILE = R.color.primary;

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new DrawerMenuAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


    }

    //this function obtains a reference to the swipe refresh layout
    //then disables scrolling when not at the top of the list
    public void setupSwipeRefreshLayout(){
        ListFragment mMainActivityListFragment;

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_main);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mMainActivityListFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.mainActivityFrag);
        final ListView fragView = mMainActivityListFragment.getListView();

        fragView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(fragView != null && fragView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = fragView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = fragView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);
            }});
    }
}
