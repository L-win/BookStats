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

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewBookActivity extends AppCompatActivity implements AddCurrentPageDialog.AddCurrentPageDialogListener {

    public static final String EXTRA_ID = "com.elvina.bookstats.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.elvina.bookstats.EXTRA_TITLE";
    public static final String EXTRA_AUTHOR = "com.elvina.bookstats.EXTRA_AUTHOR";
    public static final String EXTRA_YEAR = "com.elvina.bookstats.EXTRA_YEAR";
    public static final String EXTRA_DATE_ADDED = "com.elvina.bookstats.EXTRA_DATE_ADDED";
    public static final String EXTRA_ALL_PAGES = "com.elvina.bookstats.EXTRA_ALL_PAGES";
    public static final String EXTRA_CURRENT_PAGE = "com.elvina.bookstats.EXTRA_CURRENT_PAGES";
    public static final String EXTRA_DATE_LAST_PAGE = "com.elvina.bookstats" +
            ".EXTRA_DATE_LAST_PAGE";

    ViewBookViewModel viewBookViewModel;

    TextView viewTitle, viewAuthor, viewYear, viewAllPages, viewCurrentPage,
            viewProgress, viewDateAdded,viewDateLastPage;

    Intent intent;

    int bookAllPages;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        setTitle("Book Details");

        viewBookViewModel = new ViewModelProvider(this).get(ViewBookViewModel.class);

        intent = getIntent();

        // PREPARE VIEWS
        viewTitle = findViewById(R.id.text_book_title);
        viewAuthor = findViewById(R.id.text_book_author);
        viewYear = findViewById(R.id.text_book_year);
        viewAllPages = findViewById(R.id.text_book_all_pages);
        viewProgress = findViewById(R.id.text_book_progress);
        viewCurrentPage = findViewById(R.id.text_book_curent_page);
        viewDateAdded = findViewById(R.id.text_book_date_added);
        viewDateLastPage = findViewById(R.id.text_book_date_last_page);

        // FORMAT DATE
        String a = intent.getStringExtra(EXTRA_DATE_ADDED);
        String b =intent.getStringExtra(EXTRA_DATE_LAST_PAGE);
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date dateA = null;
        Date dateB = null;
        try {
            dateA = parser.parse(a);
            dateB = parser.parse(b);
        } catch (Exception e) {

        }
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        String bookDateAdded = formatter.format(dateA);
        String bookDateLastPage = formatter.format(dateB);


        // CALCULATE PROGRESS
        bookAllPages = intent.getIntExtra(EXTRA_ALL_PAGES, 0);
        int bookCurrenPage = intent.getIntExtra(EXTRA_CURRENT_PAGE, 0);
        double bookProgressCalc =
                100.0 / (Double.valueOf(bookAllPages) / Double.valueOf(bookCurrenPage));
        df = new DecimalFormat("###.#");
        String bookProgress = df.format(bookProgressCalc) + "%";

        // SET VALUES TO VIEW
        viewTitle.setText(intent.getStringExtra(EXTRA_TITLE));
        viewAuthor.setText(intent.getStringExtra(EXTRA_AUTHOR));
        viewYear.setText(intent.getStringExtra(EXTRA_YEAR));
        viewAllPages.setText(String.valueOf(intent.getIntExtra(EXTRA_ALL_PAGES, 0)));
        viewCurrentPage.setText(String.valueOf(intent.getIntExtra(EXTRA_CURRENT_PAGE, 0)));
        viewProgress.setText(bookProgress);
        //
        viewDateAdded.setText(bookDateAdded);
        viewDateLastPage.setText(bookDateLastPage);
        Toast.makeText(this, intent.getStringExtra(EXTRA_DATE_ADDED), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, intent.getStringExtra(EXTRA_DATE_LAST_PAGE),
//                Toast.LENGTH_SHORT).show();


        // WHEN NEW PAGE ADDED..
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
        if (currentPage != intent.getIntExtra(EXTRA_CURRENT_PAGE, 1)) {

            // CALCULATE NEW PROGRESS
            double bookNewProgressCalc =
                    100.0 / (Double.valueOf(this.bookAllPages) / Double.valueOf(currentPage));
            String bookNewProgress = df.format(bookNewProgressCalc) + "%";

            // SET NEW VALUES
            this.viewCurrentPage.setText(String.valueOf(currentPage));
            this.viewProgress.setText(bookNewProgress);

            String dateLastPage = Calendar.getInstance().getTime().toString();

            // INSERT TO DATABASE
            Book book = new Book(
                    intent.getStringExtra(EXTRA_TITLE),
                    intent.getStringExtra(EXTRA_AUTHOR),
                    intent.getStringExtra(EXTRA_DATE_ADDED),
                    intent.getStringExtra(EXTRA_YEAR),
                    intent.getIntExtra(EXTRA_ALL_PAGES, 1)
            );
            book.setId(intent.getIntExtra(EXTRA_ID, 1));
            book.setDateLastPage(dateLastPage);
            book.setCurrentPage(currentPage);
            viewBookViewModel.update(book);

            Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();

        }
    }
}