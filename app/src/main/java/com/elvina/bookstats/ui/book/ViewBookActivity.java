package com.elvina.bookstats.ui.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
            viewProgress, viewDateAdded, viewDateLastPage, viewPagesPerDay,
            viewPagesLeft, viewDaysLeft, viewReadingStatus;

    Intent intent;

    Book newBook;

    int pagesPerDay, pagesLeft, daysLeft;
    String bookDateAdded, bookDateLastPage, bookProgress;
    long daysSpent;

    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_book);

        setTitle("Book Details");

        intent = getIntent();

        // PREPARE VIEWS
        prepareViews();

        viewBookViewModel = new ViewModelProvider(this).get(ViewBookViewModel.class);
        viewBookViewModel.getSingleBook(intent.getIntExtra(EXTRA_ID, 1)).observe(this,
                new Observer<Book>() {
                    @Override
                    public void onChanged(Book book) {

                        // PREPARE DATES
                        formatDate(book);

                        // CALCULATE ALL STATS
                        calculateStats(book);

                        // SET VALUES TO VIEW
                        setValues(book);

                        // FOR ALERT DIALOG
                        setNewBook(book);

                    }
                });

        // WHEN NEW PAGE ADDED..
        Button buttonAddCurrentPage = findViewById(R.id.button_add_current_page);
        buttonAddCurrentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        Button buttonEditBook = findViewById(R.id.button_edit_book);
        buttonEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent(ViewBookActivity.this,
                        EditBookActivity.class);
                intentEdit.putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, 1));
                startActivityForResult(intentEdit,1);
            }
        });
    }

    public void openDialog() {
        AddCurrentPageDialog addCurrentPageDialog = new AddCurrentPageDialog();
        addCurrentPageDialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void applyTexts(int currentPage) {
        if (currentPage > this.newBook.getCurrentPage()) {

            // CALCULATE NEW PROGRESS
            double bookNewProgressCalc =
                    100.0 / (Double.valueOf(this.newBook.getAllPages()) / Double.valueOf(currentPage));
            String bookNewProgress = df.format(bookNewProgressCalc) + "%";

            // SET NEW VALUES
            this.viewCurrentPage.setText(String.valueOf(currentPage));
            this.viewProgress.setText(bookNewProgress);

            String dateLastPage = Calendar.getInstance().getTime().toString();

            // INSERT TO DATABASE
            Book book = new Book(this.newBook.getTitle(),
                    this.newBook.getAuthor(), this.newBook.getDateAdded(),
                    this.newBook.getYear(), this.newBook.getAllPages());
            book.setId(this.newBook.getId());
            book.setDateLastPage(dateLastPage);
            book.setCurrentPage(currentPage);
            viewBookViewModel.update(book);

            Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "wrong input", Toast.LENGTH_LONG).show();
        }
    }

    private void prepareViews() {
        viewTitle = findViewById(R.id.text_book_title);
        viewAuthor = findViewById(R.id.text_book_author);
        viewYear = findViewById(R.id.text_book_year);
        viewAllPages = findViewById(R.id.text_book_all_pages);
        viewProgress = findViewById(R.id.text_book_progress);
        viewCurrentPage = findViewById(R.id.text_book_curent_page);
        viewDateAdded = findViewById(R.id.text_book_date_added);
        viewDateLastPage = findViewById(R.id.text_book_date_last_page);
        viewPagesPerDay = findViewById(R.id.text_book_pages_per_day);
        viewPagesLeft = findViewById(R.id.text_book_pages_left);
        viewDaysLeft = findViewById(R.id.text_book_days_left);
        viewReadingStatus = findViewById(R.id.text_book_reading_status);
    }

    private void formatDate(Book book) {
        String a = book.getDateAdded();
        String b = book.getDateLastPage();
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date dateA = null;
        Date dateB = null;
        try {
            dateA = parser.parse(a);
            dateB = parser.parse(b);
        } catch (Exception e) {

        }
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        this.bookDateAdded = formatter.format(dateA);
        this.bookDateLastPage = formatter.format(dateB);
    }

    private void calculateStats(Book book) {

        // CALCULATE PAGES PER DAY
        this.daysSpent = new Long(0L);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date firstDate = sdf.parse(book.getDateAdded());
            Date secondDate = sdf.parse(book.getDateLastPage());
            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            this.daysSpent = diff;
        } catch (Exception e) {

        }

//        int cPage = book.getCurrentPage();

        this.pagesPerDay = 1;

        if (this.daysSpent == 0) {
            this.daysSpent = 1;
        }
        if (book.getCurrentPage() > 0) {
            this.pagesPerDay =
                    book.getCurrentPage() / (int) this.daysSpent;
            if (this.pagesPerDay < 1) {
                this.pagesPerDay = 1;
            }
        }

        // CALCULATE PROGRESS
        int bookCurrenPage = book.getCurrentPage();

        double bookProgressCalc =
                100.0 /
                        (Double.valueOf(book.getAllPages()) /
                                Double.valueOf(bookCurrenPage));
        df = new DecimalFormat("###.#");
        this.bookProgress = df.format(bookProgressCalc) + "%";

        // PAGES LEFT, DAYS LEFT
        this.pagesLeft = book.getAllPages() - book.getCurrentPage();
        this.daysLeft = this.pagesLeft / this.pagesPerDay;
        if (this.daysLeft == 0) {
            this.daysLeft = 1;
        }
    }

    private void setValues(Book book) {
        viewTitle.setText(book.getTitle());
        viewAuthor.setText(book.getAuthor());
        viewYear.setText(book.getYear());
        viewAllPages.setText(String.valueOf(book.getAllPages()));
        viewCurrentPage.setText(String.valueOf(book.getCurrentPage()));
        viewProgress.setText(this.bookProgress);
        viewDateAdded.setText(this.bookDateAdded);
        viewDateLastPage.setText(this.bookDateLastPage);
        viewPagesPerDay.setText(String.valueOf(this.pagesPerDay));
        viewPagesLeft.setText(String.valueOf(this.pagesLeft));
        viewDaysLeft.setText(String.valueOf(this.daysLeft));
        if (!book.getReadingStatus()) {
            viewReadingStatus.setText("Finished");
        } else {
            viewReadingStatus.setText("Reading");
        }
    }

    private void setNewBook(Book book) {
        this.newBook = book;
    }
}