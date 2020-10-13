package com.elvina.bookstats.ui.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elvina.bookstats.R;
import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.database.EditBookViewModel;
import com.elvina.bookstats.database.ViewBookViewModel;

public class EditBookActivity extends AppCompatActivity {

    EditBookViewModel editBookViewModel;
    ViewBookViewModel viewBookViewModel;

    EditText viewTitle, viewAuthor, viewYear, viewPages;
    SwitchCompat viewReadStatus;

    String dateAdded;
    int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        Intent intent = getIntent();
        bookId = intent.getIntExtra(ViewBookActivity.EXTRA_ID, 1);

        prepareViews();
        viewBookViewModel = new ViewModelProvider(this).get(ViewBookViewModel.class);
        viewBookViewModel
                .getSingleBook(bookId)
                .observe(this, new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                setValues(book);
            }
        });

        Button saveNewBook = findViewById(R.id.button_save_new_book);
        saveNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book newBook = getValues();
                viewBookViewModel.update(newBook);
                Toast.makeText(
                        EditBookActivity.this,
                        "updated",
                        Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

    }

    private void prepareViews() {
        viewTitle = findViewById(R.id.edit_text_title_edit);
        viewReadStatus = findViewById(R.id.switch_reading_status);
        viewAuthor = findViewById(R.id.edit_text_author_edit);
        viewYear = findViewById(R.id.edit_text_year_edit);
        viewPages = findViewById(R.id.edit_text_pages_edit);
    }

    private void setValues(Book book) {
        viewTitle.setText(book.getTitle());
        viewAuthor.setText(book.getAuthor());
        viewYear.setText(book.getYear());
        viewPages.setText(String.valueOf(book.getAllPages()));
        viewReadStatus.setChecked(!book.getReadingStatus());
        dateAdded = book.getDateAdded();
    }

    private Book getValues(){
        String newTitle = viewTitle.getText().toString();
        String newAuthor = viewAuthor.getText().toString();
        String newYear = viewYear.getText().toString();
        String newPages = viewPages.getText().toString();
        boolean readingStatus = viewReadStatus.isChecked();
        Book newBook = new Book(
                newTitle,
                newAuthor,
                dateAdded,
                newYear,
                Integer.parseInt(newPages)
                );
        newBook.setId(bookId);
        newBook.setReadingStatus(!readingStatus);
        return newBook;
    }
}