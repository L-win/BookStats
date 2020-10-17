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
    Button buttonAddCurrentPage;

    Book theBook;
    int theBookId;

    // FORMATTED AND CALCULATED
    String bookDateAdded, bookDateLastPage, bookProgress;
    int pagesPerDay, pagesLeft, daysLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        setTitle("Book Details");

        // PREPARE VIEWS
        prepareViews();

        // GET BOOK ID
        Intent intent = getIntent();
        theBookId = intent.getIntExtra(EXTRA_ID, 0);
//        theBook = (Book) intent.getSerializableExtra("theBook");
//        theBookId = theBook.getId();

//        System.out.println("TEST-1: " + theBook.getTitle());

        // GET DATABASE
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // GET BOOK
         theBook = bookViewModel.getSingleBookMutable(theBookId);

        // PREPARE DATES
        formatDate();

        // CALCULATE ALL STATS
        calculateStats();

        // ADD CURRENT PAGE BUTTON
        buttonAddCurrentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        // SET VALUES TO VIEW
        setValues();

    }

    public void openDialog() {
        AddCurrentPageDialog addCurrentPageDialog = new AddCurrentPageDialog();
        addCurrentPageDialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void applyTexts(int currentPage) {
        if (currentPage <= this.theBook.getAllPages()) {

            theBook.setCurrentPage(currentPage);
            theBook.setDateLastPage(Calendar.getInstance().getTime().toString());

            formatDate();
            calculateStats();
            setValues();

            bookViewModel.update(theBook);

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
        buttonAddCurrentPage = findViewById(R.id.button_add_current_page);
    }

    private void formatDate() {
        String a = theBook.getDateAdded();
        String b = theBook.getDateLastPage();
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

    private void calculateStats() {

        // DAYS SPENT
        long daysSpent;
        daysSpent = new Long(0L);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date firstDate = sdf.parse(theBook.getDateAdded());
            Date secondDate = sdf.parse(theBook.getDateLastPage());
            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            daysSpent = diff;
        } catch (Exception e) {

        }

        // PAGES PER DAY
        this.pagesPerDay = 1;

        if (daysSpent == 0) {
            daysSpent = 1;
        }
        if (theBook.getCurrentPage() > 0) {
            this.pagesPerDay = theBook.getCurrentPage() / (int) daysSpent;
            if (this.pagesPerDay < 1) {
                this.pagesPerDay = 1;
            }
        }

        // CALCULATE PROGRESS
        int bookCurrenPage = theBook.getCurrentPage();

        double bookProgressCalc = 100.0 / (Double.valueOf(theBook.getAllPages()) / Double.valueOf(bookCurrenPage));
        DecimalFormat df = new DecimalFormat("###.#");
        this.bookProgress = df.format(bookProgressCalc) + "%";

        // PAGES LEFT, DAYS LEFT
        this.pagesLeft = theBook.getAllPages() - theBook.getCurrentPage();
        this.daysLeft = this.pagesLeft / this.pagesPerDay;
        if (this.daysLeft == 0) {
            this.daysLeft = 1;
        }
    }

    private void setValues() {
        viewTitle.setText(theBook.getTitle());
        viewAuthor.setText(theBook.getAuthor());
        viewYear.setText(theBook.getYear());
        viewAllPages.setText(String.valueOf(theBook.getAllPages()));
        viewCurrentPage.setText(String.valueOf(theBook.getCurrentPage()));
        viewProgress.setText(this.bookProgress);
        viewDateAdded.setText(this.bookDateAdded);
        viewDateLastPage.setText(this.bookDateLastPage);
        viewPagesPerDay.setText(String.valueOf(this.pagesPerDay));
        viewPagesLeft.setText(String.valueOf(this.pagesLeft));
        viewDaysLeft.setText(String.valueOf(this.daysLeft));
        if (!theBook.getReadingStatus()) {
            viewReadingStatus.setText("Finished");
        } else {
            viewReadingStatus.setText("Reading");
        }

        File filePath = Environment.getExternalStorageDirectory();
        File dir = new File(filePath.getAbsolutePath() + "/BookStats/covers/");
        File file = new File(dir,
                theBook.getTitle().replace(" ", "_") + ".jpg");

        if (file.exists()) {
            Picasso.get().invalidate(file);
            Picasso.get()
                    .load(file)
                    .resize(400, 550)
                    .centerCrop()
                    .into(viewCoverImage);
        }

        if (!theBook.getReadingStatus()) {
            buttonAddCurrentPage.setVisibility(View.GONE);
        } else {
            buttonAddCurrentPage.setVisibility(View.VISIBLE);
        }

    }

    private void deleteBook() {

        new AlertDialog.Builder(this)
                .setMessage("Delete book?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent data = new Intent();
                        data.putExtra(EXTRA_ID, theBookId);
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
                intentEdit.putExtra(EXTRA_ID, theBookId);
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
        if (resultCode == Activity.RESULT_OK) {
            theBook = bookViewModel.getSingleBookMutable(theBookId);
            setValues();
        }
    }
}