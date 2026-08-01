package com.example.workshop_1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddCountryActivity extends AppCompatActivity {

    EditText editCountry, editCurrency;
    Button btnSave;

    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_country);

        editCountry = findViewById(R.id.editCountry);
        editCurrency = findViewById(R.id.editCurrency);

        btnSave = findViewById(R.id.btnSave);

        dbManager = new DBManager(this);
        dbManager.open();

        btnSave.setOnClickListener(v -> {

            String country = editCountry.getText().toString().trim();
            String currency = editCurrency.getText().toString().trim();

            if(country.isEmpty()){
                editCountry.setError("Enter Country Name");
                return;
            }

            if(currency.isEmpty()){
                editCurrency.setError("Enter Currency");
                return;
            }

            dbManager.insert(country, currency);

            finish();

        });

    }
}