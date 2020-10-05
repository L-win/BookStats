package com.elvina.bookstats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBookActivity extends AppCompatActivity {

//    public static final String EXTRA_ID = "com.example.myapplication.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.myapplication.EXTRA_TITLE";
    public static final String EXTRA_AUTHOR = "com.example.myapplication.EXTRA_AUTHOR";
    public static final String EXTRA_YEAR = "com.example.myapplication.EXTRA_YEAR";
    public static final String EXTRA_DATE_ADDED = "com.example.myapplication.EXTRA_DATE_ADDED";

    Calendar c = Calendar.getInstance();

    private EditText editTextTitle, editTextAuthor, editTextYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        // SETTINGS
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        boolean settingsDarkMode = preferences.getBoolean("check_box_dark_mode", true);
//        if (settingsDarkMode) {
//            setTheme(R.style.darktheme);
//        }

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextAuthor = findViewById(R.id.edit_text_author);
        editTextYear = findViewById(R.id.edit_text_year);
//        switchPin = findViewById(R.id.switch1);

//        mainLayout = findViewById(R.id.main_layout);

//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        setTitle("Add Book");


    }

    private void saveBook() {
        String title = editTextTitle.getText().toString();
        String author = editTextAuthor.getText().toString();
        String year = editTextYear.getText().toString();
        String currentDate = c.getTime().toString();

        if (title.trim().isEmpty() || author.trim().isEmpty()) {
            Toast.makeText(this, "Fields are empty.", Toast.LENGTH_SHORT).show();
//            Snackbar.make(mainLayout, "Fields are empty.", Snackbar.LENGTH_LONG).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_AUTHOR, author);
        data.putExtra(EXTRA_YEAR, year);
        data.putExtra(EXTRA_DATE_ADDED, currentDate);

//        int id = getIntent().getIntExtra(EXTRA_ID, -1);
//        if (id != -1) {
//            data.putExtra(EXTRA_ID, id);
//        }

        setResult(RESULT_OK, data);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_book_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_book:
                saveBook();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}