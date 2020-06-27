package com.example.ebaysearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private RequestQueue mQueue;
    private ActionBar actionBar;
    public JSONObject data;
    private String cardID;
    private Card card;

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabItem productTab;
    TabItem sellerInfoTab;
    TabItem shippingTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabLayout);
        productTab = findViewById(R.id.productTab);
        sellerInfoTab = findViewById(R.id.sellerInfoTab);
        shippingTab = findViewById(R.id.shippingTab);
        viewPager = findViewById(R.id.viewPager);

        pagerAdapter = new PageAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        String cardID;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                cardID = null;
            } else {
                cardID = extras.getString("cardID");
            }
        } else {
            cardID = (String) savedInstanceState.getSerializable("cardID");
        }
        Log.d(TAG, "onCreate: the cardID received is: " + cardID);

        Intent i = getIntent();
        Card card = (Card) i.getSerializableExtra("card");
        this.card = card;
        this.cardID = cardID;
        actionBar.setTitle(card.getItemTitle());

        mQueue = Volley.newRequestQueue(this);
//        String url = makeURL();
//        getJson(url);
    }

    public Card getCard() {
        return card;
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        if (id == R.id.mybutton) {
            Intent browserIntent = null;
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(card.getViewItemURL()));
            startActivity(browserIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    //todo call it on server side
    public String makeURL() {
        String url = "";
        url += "http://10.0.2.2:3000/single-q?cardID=" + cardID;
//        url += "http://ebay-8.wl.r.appspot.com/single-q?cardID=" + cardID;
        return url;
    }

    public void getJson(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: got response: " + response);
                        ProgressBar progressBar = findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.GONE);
                        SellerInfoFrag sellerInfoFrag = (SellerInfoFrag) getFragmentManager().findFragmentById(R.id.SellerInfoFrag);
                        Log.d(TAG, "onResponse: sellerInfoFrag is" + sellerInfoFrag);
//                        sellerInfoFrag.setData(response);
//                        parseJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    public ArrayList<Card> parseJSON(JSONObject myJSON) {
        ArrayList<Card> cards = new ArrayList<Card>();
        Log.d(TAG, "onCreate: Started making tabs");
        Log.d(TAG, "parseJSON: JSON data: \n" + myJSON);
//        ListView mListView = findViewById(R.id.listView);
//        try {
//            JSONArray items = myJSON.getJSONObject("searchResult").getJSONArray("item");
//            for (int i = 0; i < items.length(); i++) {
//                String galleryURL = items.getJSONObject(i).getJSONArray("galleryURL").getString(0);
//                String itemTitle = items.getJSONObject(i).getJSONArray("title").getString(0);
//                String itemCondition = items.getJSONObject(i).getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0);
//                String isTopRated = items.getJSONObject(i).getJSONArray("topRatedListing").getString(0);
//                String shippingCost = items.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
//                String price = items.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("convertedCurrentPrice").getJSONObject(0).getString("__value__");
//                String itemID = items.getJSONObject(i).getJSONArray("itemId").getString(0);
//                Log.d(TAG, "parseJSON: itemID is " + itemID);
//                cards.add(new Card(galleryURL,itemTitle,itemCondition,isTopRated,shippingCost,price, itemID));
////                setCards(cards);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return cards;
    }

    public JSONObject getData() {
        return this.data;
    }

}