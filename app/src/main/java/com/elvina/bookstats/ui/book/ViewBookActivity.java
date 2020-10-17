package com.elvina.bookstats.ui.book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elvina.bookstats.MainActivity;
import com.elvina.bookstats.R;
import com.elvina.bookstats.database.Book;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ViewBookActivity extends AppCompatActivity implements AddCurrentPageDialog.AddCurrentPageDialogListener {

    public static final String EXTRA_ID = "com.elvina.bookstats.EXTRA_ID";
//    public static final String EXTRA_TITLE = "com.elvina.bookstats.EXTRA_TITLE";
//    public static final String EXTRA_AUTHOR = "com.elvina.bookstats.EXTRA_AUTHOR";
//    public static final String EXTRA_YEAR = "com.elvina.bookstats.EXTRA_YEAR";
//    public static final String EXTRA_DATE_ADDED = "com.elvina.bookstats.EXTRA_DATE_ADDED";
//    public static final String EXTRA_ALL_PAGES = "com.elvina.bookstats.EXTRA_ALL_PAGES";
//    public static final String EXTRA_CURRENT_PAGE = "com.elvina.bookstats.EXTRA_CURRENT_PAGES";
//    public static final String EXTRA_DATE_LAST_PAGE = "com.elvina.bookstats" +
//            ".EXTRA_DATE_LAST_PAGE";

    BookViewModel bookViewModel;

    TextView viewTitle, viewAuthor, viewYear, viewAllPages, viewCurrentPage,
            viewProgress, viewDateAdded, viewDateLastPage, viewPagesPerDay,
            viewPagesLeft, viewDaysLeft, viewReadingStatus;

    ImageView viewCoverImage;

    Intent intent;

    Book newBook;

    int pagesPerDay, pagesLeft, daysLeft;
    String bookDateAdded, bookDateLastPage, bookProgress;
    long daysSpent;

    Button buttonAddCurrentPage;

    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_book);

        setTitle("Book Details");

        intent = getIntent();

        // PREPARE VIEWS
        prepareViews();

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        Book book = bookViewModel.getSingleBookMutable(intent.getIntExtra(EXTRA_ID, 0));

        // WHEN NEW PAGE ADDED..
        buttonAddCurrentPage = findViewById(R.id.button_add_current_page);
        buttonAddCurrentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        // PREPARE DATES
        formatDate(book);

        // CALCULATE ALL STATS
        calculateStats(book);

        // SET VALUES TO VIEW
        setValues(book);

        // FOR ALERT DIALOG
        setNewBook(book);

//        if(book.getReadingStatus() == false){
//            buttonAddCurrentPage.setVisibility(View.GONE);
//        }

    }

    public void openDialog() {
        AddCurrentPageDialog addCurrentPageDialog = new AddCurrentPageDialog();
        addCurrentPageDialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void applyTexts(int currentPage) {
        if (currentPage <= this.newBook.getAllPages()) {

            Book tempBook = newBook;
            tempBook.setCurrentPage(currentPage);
            tempBook.setDateLastPage(Calendar.getInstance().getTime().toString());

            formatDate(tempBook);
            calculateStats(tempBook);
            setValues(tempBook);

            bookViewModel.update(tempBook);

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
        viewCoverImage = findViewById(R.id.image_book_cover);
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

        double bookProgressCalc = 100.0 / (Double.valueOf(book.getAllPages()) / Double.valueOf(bookCurrenPage));
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

        File filePath = Environment.getExternalStorageDirectory();
        File dir = new File(filePath.getAbsolutePath() + "/BookStats/covers/");
        File file = new File(dir,
                book.getTitle().replace(" ", "_") + ".jpg");

        if (file.exists()) {
            Picasso.get().invalidate(file);
            Picasso.get()
                    .load(file)
                    .resize(400, 550)
                    .centerCrop()
                    .into(viewCoverImage);
        }

        if(book.getReadingStatus() == false){
            buttonAddCurrentPage.setVisibility(View.GONE);
        }else {
            buttonAddCurrentPage.setVisibility(View.VISIBLE);
        }

    }

    private void setNewBook(Book book) {
        this.newBook = book;
    }

    private void deleteBook() {

        new AlertDialog.Builder(this)
                .setMessage("Delete book?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent data = new Intent();
                        data.putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, 1));
                        System.out.println("TEST-0:" + intent.getIntExtra(EXTRA_ID, 1));
                        setResult(RESULT_OK, data);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create().show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_book_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_book:
                Intent intentEdit = new Intent(this,
                        EditBookActivity.class);
                intentEdit.putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, 1));
                startActivityForResult(intentEdit, 1);
                return true;
            case R.id.delete_book:
                deleteBook();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Book book = bookViewModel.getSingleBookMutable(intent.getIntExtra(EXTRA_ID, 0));
            setValues(book);
            setNewBook(book);
        }
    }
}