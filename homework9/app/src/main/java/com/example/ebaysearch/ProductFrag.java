package com.example.ebaysearch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFrag extends android.app.Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment product.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFrag newInstance(String param1, String param2) {
        ProductFrag fragment = new ProductFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RequestQueue mQueue;
    JSONObject data;
    View view;
    private static final String TAG = "ProductFrag";

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
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        this.view = view;
        DetailActivity activity = (DetailActivity) getActivity();
        String url = activity.makeURL();
        Log.d(TAG, "onCreateView: URL is: " + url);
        getJson(url);
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

    public void setData(JSONObject data) throws JSONException {
        // set the gallery images
        LinearLayout galleryLayout = (LinearLayout) view.findViewById(R.id.galleryLayout);
        JSONArray pictureURLs = data.getJSONObject("Item").getJSONArray("PictureURL");
        for (int i = 0; i < pictureURLs.length(); i++) {
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setMinimumHeight(312);
            image.setAdjustViewBounds(true);
            image.setPadding(0, 0, 50, 50);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            image.setLayoutParams(layoutParams);
            Picasso.get().load(pictureURLs.getString(i)).into(image);
//            image.getLayoutParams().height = 312;
            galleryLayout.addView(image);
        }

        DetailActivity activity = (DetailActivity) getActivity();
        Card card = activity.getCard();

        // set the title, price and shipping information
        TextView productTitleView = view.findViewById(R.id.productTitleView);
        productTitleView.setText(card.getItemTitle());
        TextView priceView = view.findViewById(R.id.priceView);
        priceView.setText(card.getPrice());
        TextView shipCostView = view.findViewById(R.id.shipPriceView);
        shipCostView.setText("Ships for $" + card.getShippingCost());

        TextView subtitleView = view.findViewById(R.id.subtitleView);
        try {
            subtitleView.setText(data.getJSONObject("Item").getString("Subtitle"));
        }
        catch (JSONException e){
            LinearLayout subtitleLayout = view.findViewById(R.id.subtitleLayout);
            subtitleLayout.setVisibility(View.GONE);
        }
        TextView brandView = view.findViewById(R.id.brandView);
        brandView.setText(data.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList").getJSONObject(0).getJSONArray("Value").getString(0));

        JSONArray NameValueList = data.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList");
        int numSpecs = 0;
        int i = 0;
        String html = "";
        while (numSpecs < 5 && i < NameValueList.length()) {
            if (!NameValueList.getJSONObject(i).equals("Brand")) {
                html += "        <b><p style=\"padding: 0px;\">&#8226 "
                        + NameValueList.getJSONObject(i).getJSONArray("Value").getString(0) + "</p></b>\n";
                numSpecs++;
            }
            i++;
        }
        TextView specsView = view.findViewById(R.id.specsView);
        specsView.setText(Html.fromHtml(html));
    }
}