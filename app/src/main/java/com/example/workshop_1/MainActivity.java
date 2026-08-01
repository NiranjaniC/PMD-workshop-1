package com.example.workshop_1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DBManager dbManager;

    ListView listView;

    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);

        dbManager = new DBManager(this);
        dbManager.open();

        Cursor cursor = dbManager.fetch();

        String[] from = {
                DatabaseHelper.COL_COUNTRY,
                DatabaseHelper.COL_CURRENCY
        };

        int[] to = {
                R.id.txtCountry,
                R.id.txtCurrency
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_row,
                cursor,
                from,
                to,
                0
        );

        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddCountryActivity.class);

            startActivity(intent);

        });

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Cursor c = (Cursor) parent.getItemAtPosition(position);

            Intent modify = new Intent(
                    MainActivity.this,
                    ModifyCountryActivity.class);

            modify.putExtra(
                    "id",
                    c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));

            modify.putExtra(
                    "country",
                    c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_COUNTRY)));

            modify.putExtra(
                    "currency",
                    c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COL_CURRENCY)));

            startActivity(modify);

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor cursor = dbManager.fetch();

        String[] from = {
                DatabaseHelper.COL_COUNTRY,
                DatabaseHelper.COL_CURRENCY
        };

        int[] to = {
                R.id.txtCountry,
                R.id.txtCurrency
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_row,
                cursor,
                from,
                to,
                0
        );

        listView.setAdapter(adapter);
    }
}