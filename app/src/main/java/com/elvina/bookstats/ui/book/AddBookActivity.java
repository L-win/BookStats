package com.elvina.bookstats.ui.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.elvina.bookstats.R;
import com.elvina.bookstats.database.Book;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBookActivity extends AppCompatActivity {

        public static final String EXTRA_ID = "com.elvina.bookstats.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.bookstats.EXTRA_TITLE";
    public static final String EXTRA_AUTHOR = "com.example.bookstats.EXTRA_AUTHOR";
    public static final String EXTRA_YEAR = "com.example.bookstats.EXTRA_YEAR";
    public static final String EXTRA_DATE_ADDED = "com.example.bookstats.EXTRA_DATE_ADDED";
    public static final String EXTRA_ALL_PAGES = "com.example.bookstats.EXTRA_ALL_PAGES";


    BookViewModel bookViewModel;

    Calendar c = Calendar.getInstance();

    private EditText editTextTitle, editTextAuthor, editTextYear, editTextAllPages;
    private String title, author, year, currentDate;
    int allPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        setTitle("Add Book");

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // PREPARE INPUT FIELDS
        prepareViews();


    }

    private void prepareViews() {
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextAuthor = findViewById(R.id.edit_text_author);
        editTextYear = findViewById(R.id.edit_text_year);
        editTextAllPages = findViewById(R.id.edit_text_all_pages);
    }

    private void getValues() {
        title = editTextTitle.getText().toString();
        author = editTextAuthor.getText().toString();
        year = editTextYear.getText().toString();
        allPages = Integer.parseInt(editTextAllPages.getText().toString());
        currentDate = c.getTime().toString();

        if (title.trim().isEmpty() || editTextAllPages.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Required fields are empty.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void saveBook() {

        // READ USER INPUT
        getValues();

        Book book = new Book(title, author, currentDate, year, allPages);
        bookViewModel.insert(book);

//        Intent data = new Intent();
//        data.putExtra(EXTRA_TITLE, title);
//        data.putExtra(EXTRA_AUTHOR, author);
//        data.putExtra(EXTRA_YEAR, year);
//        data.putExtra(EXTRA_DATE_ADDED, currentDate);
//        data.putExtra(EXTRA_ALL_PAGES, Integer.parseInt(allPages));

        Toast.makeText(this, "Added Book.", Toast.LENGTH_SHORT).show();
        setResult(RESULT_CANCELED);
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