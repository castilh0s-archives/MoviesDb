package com.castilhos.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private RatingBar ratingBar;
    private Button buttonSave;
    private Button buttonRetrieveData;
    private TextView textViewResult;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.editTextTitle);
        ratingBar = findViewById(R.id.ratingBar);
        buttonSave = findViewById(R.id.buttonSave);
        buttonRetrieveData = findViewById(R.id.buttonRetrieveData);
        textViewResult = findViewById(R.id.textViewResult);

        database = openOrCreateDatabase(
                "database.db",
                MODE_PRIVATE,
                null
        );

        database.execSQL("CREATE TABLE IF NOT EXISTS movies(name VARCHAR(200), rating FLOAT(2,2))");
        getMovies();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                Float rating = ratingBar.getRating();

                database.execSQL("INSERT INTO movies(name, rating) VALUES ('" + title + "'," + rating + ")");

                String result = textViewResult.getText().toString();
                result += title + " - " + rating + "\n";

                textViewResult.setText(result);
            }
        });

        buttonRetrieveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovies();
            }
        });
    }

    private void getMovies() {
        Cursor cursor = database.rawQuery("SELECT * FROM movies", null);

        int indexName = cursor.getColumnIndex("name");
        int indexRating = cursor.getColumnIndex("rating");

        String result = "";
        while (cursor.moveToNext()) {
            result += cursor.getString(indexName) + " | " + cursor.getString(indexRating) + "\n";
        }

        textViewResult.setText(result);
    }
}
