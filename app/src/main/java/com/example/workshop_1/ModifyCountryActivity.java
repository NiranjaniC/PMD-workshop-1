package com.example.workshop_1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ModifyCountryActivity extends AppCompatActivity {

    EditText editCountry, editCurrency;

    Button btnUpdate, btnDelete;

    DBManager dbManager;

    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_country);

        editCountry = findViewById(R.id.editCountry);
        editCurrency = findViewById(R.id.editCurrency);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        dbManager = new DBManager(this);
        dbManager.open();

        id = getIntent().getLongExtra("id", 0);

        editCountry.setText(getIntent().getStringExtra("country"));
        editCurrency.setText(getIntent().getStringExtra("currency"));

        btnUpdate.setOnClickListener(v -> {

            String country = editCountry.getText().toString().trim();
            String currency = editCurrency.getText().toString().trim();

            if (country.isEmpty()) {
                editCountry.setError("Enter Country Name");
                return;
            }

            if (currency.isEmpty()) {
                editCurrency.setError("Enter Currency");
                return;
            }

            dbManager.update(id, country, currency);

            finish();

        });

        btnDelete.setOnClickListener(v -> {

            dbManager.delete(id);

            finish();

        });

    }
}