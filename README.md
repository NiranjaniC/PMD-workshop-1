# Android SQLite CRUD Application
## Workshop 1 – Country and Currency Database

# AIM

To develop an Android application that stores **Country Names** and their respective **Currencies** using **SQLite Database**, and perform CRUD (Create, Read, Update, Delete) operations through a ListView interface.

# SOFTWARE REQUIREMENTS

## Hardware Requirements
- Processor : Intel i3 or above
- RAM : 4 GB or above
- Hard Disk : 2 GB Free Space

## Software Requirements
- Windows 10/11
- Android Studio
- Java
- Android SDK
- SQLite Database
- Android Emulator (Pixel API 31 or above)

# OBJECTIVE

The application allows the user to

- Add a new country
- Store the country in SQLite Database
- Display all countries in ListView
- Modify country details
- Delete country records

# ALGORITHM / PROCEDURE

### Step 1
Create a new Android Studio project.

### Step 2
Create SQLite Database Helper class.

### Step 3
Create DBManager class for CRUD operations.

### Step 4
Design XML layouts for

- MainActivity
- AddCountryActivity
- ModifyCountryActivity
- List Row

### Step 5
Display all country records in ListView.

### Step 6
Click **Add Country** button to insert new records.

### Step 7
Click a ListView item to edit or delete the selected record.

### Step 8
Store all records permanently using SQLite Database.

### Step 9
Run the application on Emulator.

# PROJECT STRUCTURE

```
workshop-1

│
├── MainActivity.java
├── AddCountryActivity.java
├── ModifyCountryActivity.java
├── DatabaseHelper.java
├── DBManager.java
│
├── activity_main.xml
├── activity_add_country.xml
├── activity_modify_country.xml
├── list_row.xml
│
└── AndroidManifest.xml
```

# PROGRAM CODE

## 1. DatabaseHelper.java
```
package com.example.workshop_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information
    public static final String DATABASE_NAME = "CountryDB";
    public static final int DATABASE_VERSION = 1;

    // Table information
    public static final String TABLE_NAME = "Country";

    // Column names
    public static final String COL_ID = "_id";
    public static final String COL_COUNTRY = "country";
    public static final String COL_CURRENCY = "currency";

    // SQL query to create the table
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_COUNTRY + " TEXT NOT NULL, " +
                    COL_CURRENCY + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }
}
```

## 2. DBManager.java
```
package com.example.workshop_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context context) {
        this.context = context;
    }

    // Open Database
    public DBManager open() {

        dbHelper = new DatabaseHelper(context);

        database = dbHelper.getWritableDatabase();

        return this;
    }

    // Close Database
    public void close() {

        dbHelper.close();

    }

    // Insert Data
    public void insert(String country, String currency) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_COUNTRY, country);

        values.put(DatabaseHelper.COL_CURRENCY, currency);

        database.insert(DatabaseHelper.TABLE_NAME, null, values);

    }

    // Read Data
    public Cursor fetch() {

        String[] columns = {
                DatabaseHelper.COL_ID,
                DatabaseHelper.COL_COUNTRY,
                DatabaseHelper.COL_CURRENCY
        };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    // Update Data
    public int update(long id, String country, String currency) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_COUNTRY, country);

        values.put(DatabaseHelper.COL_CURRENCY, currency);

        return database.update(
                DatabaseHelper.TABLE_NAME,
                values,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    // Delete Data
    public void delete(long id) {

        database.delete(
                DatabaseHelper.TABLE_NAME,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );

    }

}
```
## 3. MainActivity.java
```
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
```
## 4. AddCountryActivity.java
```
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
```
## 5. ModifyCountryActivity.java
```
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
```
## 6. AndroidManifest.xml
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Workshop1"
        tools:targetApi="31">
        <activity
            android:name=".ModifyCountryActivity"
            android:exported="false"
            android:windowSoftInputMode="adjustResize" />
        <activity
            android:name=".AddCountryActivity"
            android:exported="false"
            android:windowSoftInputMode="adjustResize" />
        <activity android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```
## 7. activity_main.xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/txtTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Country Database"
        android:textSize="24sp"
        android:textStyle="bold"
        android:gravity="center"
        android:padding="10dp"/>

    <Button
        android:id="@+id/btnAdd"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Add Country"/>

    <ListView
        android:id="@+id/listView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>

</LinearLayout>
```
## 8. activity_add_country.xml
```
<?xml version="1.0" encoding="utf-8"?>

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="20dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Add Country"
        android:textStyle="bold"
        android:textSize="22sp"
        android:gravity="center"
        android:layout_marginBottom="20dp"/>

    <EditText
        android:id="@+id/editCountry"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter Country Name"/>

    <EditText
        android:id="@+id/editCurrency"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter Currency"
        android:layout_marginTop="15dp"/>

    <Button
        android:id="@+id/btnSave"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Save"
        android:layout_marginTop="25dp"/>

</LinearLayout>
```
## 9. activity_modify_country.xml
```
<?xml version="1.0" encoding="utf-8"?>

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="20dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Modify Country"
        android:textSize="22sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="20dp"/>

    <EditText
        android:id="@+id/editCountry"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Country"/>

    <EditText
        android:id="@+id/editCurrency"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Currency"
        android:layout_marginTop="15dp"/>

    <Button
        android:id="@+id/btnUpdate"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Update"
        android:layout_marginTop="25dp"/>

    <Button
        android:id="@+id/btnDelete"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Delete"
        android:layout_marginTop="10dp"/>

</LinearLayout>
```
## 10. list_row.xml
```
<?xml version="1.0" encoding="utf-8"?>

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="12dp">

    <TextView
        android:id="@+id/txtCountry"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="20sp"
        android:textStyle="bold"
        android:text="Country"/>

    <TextView
        android:id="@+id/txtCurrency"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        android:text="Currency"/>

</LinearLayout>
```
# OUTPUT

<img width="1919" height="1020" alt="Screenshot 2026-08-01 085229" src="https://github.com/user-attachments/assets/817de82e-a7b0-41c9-a3fb-a336e5544d49" />
<img width="1919" height="1018" alt="Screenshot 2026-08-01 085249" src="https://github.com/user-attachments/assets/cf02a1c4-0ecf-401e-b9a7-5b08d76687ba" />
<img width="1919" height="1019" alt="Screenshot 2026-08-01 085422" src="https://github.com/user-attachments/assets/1c01aba0-d692-4152-bd1b-5b569020594d" />
<img width="1918" height="1016" alt="Screenshot 2026-08-01 085515" src="https://github.com/user-attachments/assets/23f4710f-f902-418c-888c-281d5fee37cf" />
<img width="1918" height="1021" alt="Screenshot 2026-08-01 085547" src="https://github.com/user-attachments/assets/0714ae97-13bb-46ea-9239-fa83852e09ea" />
<img width="1919" height="1011" alt="Screenshot 2026-08-01 085604" src="https://github.com/user-attachments/assets/254b7c33-32dd-4b44-8322-e93dfff42e65" />

# RESULT

The Android application was developed successfully using **SQLite Database** to perform **CRUD (Create, Read, Update, Delete)** operations. The application allows users to add, view, update, and delete country and currency records through a simple and user-friendly interface. All records are stored permanently in the SQLite database and displayed dynamically in the ListView.




