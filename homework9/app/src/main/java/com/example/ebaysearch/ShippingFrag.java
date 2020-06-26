package com.example.ebaysearch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShippingFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShippingFrag extends android.app.Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShippingFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment shipping.
     */
    // TODO: Rename and change types and number of parameters
    public static ShippingFrag newInstance(String param1, String param2) {
        ShippingFrag fragment = new ShippingFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RequestQueue mQueue;
    JSONObject data;
    View view;
    String item;
    private static final String TAG = "ShippingFrag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mQueue = Volley.newRequestQueue(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);
        this.view = view;
        DetailActivity activity = (DetailActivity) getActivity();
//        String url = activity.makeURL();
//        getJson(url);
        Card card = activity.getCard();
        this.item = card.getItem();
        JSONObject item = null;
        try {
            item = new JSONObject(this.item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        try {
            setData(item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void getJson(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: got response: " + response);
                        ProgressBar progressBar = view.findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.GONE);
                        try {
                            setData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    public void setData(JSONObject item) throws JSONException {
        TextView shipInfoStr = view.findViewById(R.id.shipInfoStr);
        String isOneDayAvailable = item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("oneDayShippingAvailable").getString(0);
        if (isOneDayAvailable.equals("true")) {
            isOneDayAvailable = "Yes";
        }
        else {
            isOneDayAvailable = "No";
        }
        String isExpeditedShippingAvailable = item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("expeditedShipping").getString(0);
        if (isExpeditedShippingAvailable.equals("true")) {
            isExpeditedShippingAvailable = "Yes";
        }
        else {
            isExpeditedShippingAvailable = "No";
        }
        shipInfoStr.setText(Html.fromHtml(
            " <ul style=\"margin: 0px;\">\n" +
                    "        <b><p style=\"text-indent: 10px;\">&#8226 Handling Time: "
                    + item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("handlingTime").getString(0) + "</p></b><br>\n" +
                    "        <b><p style=\"text-indent: 10px;\">&#8226 One Day Shipping Available: "
                    + isOneDayAvailable + "</p></b><br>\n" +
                    "        <b><p style=\"text-indent: 10px;\">&#8226 Shipping Type: " +
                    item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingType").getString(0) + "</p></b><br>\n" +
                    "        <b><p style=\"text-indent: 10px;\">&#8226 Shipping From: " +
                    item.getJSONArray("country").getString(0) + "</p></b><br>\n" +
                    "        <b><p style=\"text-indent: 10px;\">&#8226 Ship To Locations: " +
                    item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shipToLocations").getString(0) + "</p></b><br>\n" +
                    "        <b><p style=\"text-indent: 10px;\">&#8226 Expedited Shipping: " +
                    isExpeditedShippingAvailable + "</p></b><br><br>\n" +
//                    "    </ul>\n" +
                    "    <hr>"
            , Html.FROM_HTML_MODE_COMPACT));
    }
}