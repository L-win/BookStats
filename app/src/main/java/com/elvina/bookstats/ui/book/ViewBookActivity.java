package com.elvina.bookstats.ui.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elvina.bookstats.R;
import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.database.BookViewModel;
import com.elvina.bookstats.database.ViewBookViewModel;

public class ViewBookActivity extends AppCompatActivity implements AddCurrentPageDialog.AddCurrentPageDialogListener {

    public static final String EXTRA_ID = "com.elvina.bookstats.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.elvina.bookstats.EXTRA_TITLE";
    public static final String EXTRA_AUTHOR = "com.elvina.bookstats.EXTRA_AUTHOR";
    public static final String EXTRA_YEAR = "com.elvina.bookstats.EXTRA_YEAR";
    public static final String EXTRA_DATE_ADDED = "com.elvina.bookstats.EXTRA_DATE_ADDED";
    public static final String EXTRA_ALL_PAGES = "com.elvina.bookstats.EXTRA_ALL_PAGES";
    public static final String EXTRA_CURRENT_PAGE = "com.elvina.bookstats.EXTRA_CURRENT_PAGES";

    Bundle bundle;
    TextView currentPageS;
    Intent intent;
    ViewBookViewModel viewBookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        setTitle("Book Details Actv");

        intent = getIntent();

        TextView titleS = findViewById(R.id.text_book_title);
        TextView authorS = findViewById(R.id.text_book_author);
        TextView yearS = findViewById(R.id.text_book_year);
        TextView allPagesS = findViewById(R.id.text_book_all_pages);
        currentPageS = findViewById(R.id.text_book_curent_page);
        titleS.setText(intent.getStringExtra(EXTRA_TITLE));
        authorS.setText(intent.getStringExtra(EXTRA_AUTHOR));
        yearS.setText(intent.getStringExtra(EXTRA_YEAR));
        allPagesS.setText(String.valueOf(intent.getIntExtra(EXTRA_ALL_PAGES, 0)));
        currentPageS.setText(String.valueOf(intent.getIntExtra(EXTRA_CURRENT_PAGE, 0)));

        viewBookViewModel = new ViewModelProvider(this).get(ViewBookViewModel.class);

        Button buttonAddCurrentPage = findViewById(R.id.button_add_current_page);
        buttonAddCurrentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });


    }

    public void openDialog() {
        AddCurrentPageDialog addCurrentPageDialog = new AddCurrentPageDialog();
        addCurrentPageDialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void applyTexts(int currentPage) {
        currentPageS.setText(String.valueOf(currentPage));
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        if (currentPage != intent.getIntExtra(EXTRA_CURRENT_PAGE, 1)) {
            Book book = new Book(
                    intent.getStringExtra(EXTRA_TITLE),
                    intent.getStringExtra(EXTRA_AUTHOR),
                    intent.getStringExtra(EXTRA_DATE_ADDED),
                    intent.getStringExtra(EXTRA_YEAR),
                    intent.getIntExtra(EXTRA_ALL_PAGES, 1)
            );
            book.setId(intent.getIntExtra(EXTRA_ID, 1));
            book.setCurrentPage(currentPage);
            viewBookViewModel.update(book);
        }
    }
}