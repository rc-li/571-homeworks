package com.example.ebaysearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.optionBar);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCards();
            }
        });
    }

    public void openCards() {
        Intent intent = new Intent(this, Cards.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
//        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void searchClicked(View view) {
        valueCheck();

    }

    public boolean valueCheck() {
        boolean passValidation = true;

        EditText keyEdit = findViewById(R.id.keywordEdit);
        String keyword = keyEdit.getText().toString();
        TextView emptyKeyWarning = findViewById(R.id.emptyKeyWarning);
        if (keyword.equals("")) {
            emptyKeyWarning.setVisibility(View.VISIBLE);
            passValidation = false;
        }
        else {
            emptyKeyWarning.setVisibility(View.GONE);
        }

        EditText lowPriceEdit = findViewById(R.id.lowPrice);
        String lowPriceStr = lowPriceEdit.getText().toString();
        double lowPrice;
        try {
            lowPrice = Double.parseDouble(lowPriceStr);
        }
        catch (Exception e) {
            lowPrice = 0.0;
        }
        EditText highPriceEdit = findViewById(R.id.highPrice);
        String highPriceStr = highPriceEdit.getText().toString();
        double highPrice;
        try {
            highPrice = Double.parseDouble(highPriceStr);
        }
        catch (Exception e) {
            highPrice = 0.0;
        }
        Log.d(TAG, "searchClicked: " + "lowPrice is " + lowPrice + "1.23 and highPrice is " + highPrice);
        TextView priceWarning = findViewById(R.id.priceRangeWarning);
        if (highPrice < lowPrice) {
            priceWarning.setVisibility(View.VISIBLE);
            passValidation = false;
        }
        else {
            priceWarning.setVisibility(View.GONE);
        }

        if (!passValidation) {
            Toast.makeText(this, "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
        }

        return passValidation;
    }

    public void clearClicked(View view) {
        EditText keyword = findViewById(R.id.keywordEdit);
        keyword.setText("");
        EditText lowPrice = findViewById(R.id.lowPrice);
        lowPrice.setText("");
        EditText highPrice = findViewById(R.id.highPrice);
        highPrice.setText("");

        CheckBox isNew = findViewById(R.id.isNew);
        isNew.setChecked(false);
        CheckBox isUsed = findViewById(R.id.isUsed);
        isUsed.setChecked(false);
        CheckBox isUnspecified = findViewById(R.id.isUnspecified);
        isUnspecified.setChecked(false);

        Spinner spinner = findViewById(R.id.optionBar);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextView keyWordWarning = findViewById(R.id.emptyKeyWarning);
        keyWordWarning.setVisibility(View.GONE);
        TextView priceRangeWarning = findViewById(R.id.priceRangeWarning);
        priceRangeWarning.setVisibility(View.GONE);
    }
}