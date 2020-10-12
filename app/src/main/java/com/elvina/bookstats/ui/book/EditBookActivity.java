package com.elvina.bookstats.ui.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        Intent intent = getIntent();

        prepareViews();
//        editBookViewModel =
//                new ViewModelProvider(this).get(EditBookViewModel.class);
        viewBookViewModel = new ViewModelProvider(this).get(ViewBookViewModel.class);
        viewBookViewModel.getSingleBook(intent.getIntExtra(ViewBookActivity.EXTRA_ID, 1)).observe(this, new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                setValues(book);
            }
        });

//        viewReadStatus.isChecked();

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
        if(book.getReadingStatus() == 0){
            viewReadStatus.setChecked(true);
        }else if (book.getReadingStatus() == 1){
            viewReadStatus.setChecked(false);
        }
    }
}