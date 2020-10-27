package com.elvina.bookstats.ui.book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.elvina.bookstats.R;
import com.elvina.bookstats.database.Book;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ViewBookActivity extends AppCompatActivity implements AddCurrentPageDialog.AddCurrentPageDialogListener {

    public static final String EXTRA_ID = "com.elvina.bookstats.EXTRA_ID";

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

        // GET DATABASE
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // GET BOOK
        theBook = bookViewModel.getSingleBookMutable(theBookId);

    }

    @Override
    protected void onStart() {
        super.onStart();
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
//        viewCoverImage = findViewById(R.id.image_book_cover);
        buttonAddCurrentPage = findViewById(R.id.button_add_current_page);
    }

    private void formatDate() {

        String[] input = {theBook.getDateAdded(), theBook.getDateLastPage()};

        String[] result = new String[2];
        try {
            result = new FormatDateAsynctask().execute(input).get();
        } catch (Exception e) {

        }

        this.bookDateAdded = result[0];
        this.bookDateLastPage = result[1];

    }

    private void calculateStats() {

        Integer[] result = new Integer[4];
        try {
            result = new CalculateStatsAsyncTask(theBook).execute().get();
        } catch (Exception e) {

        }

        this.pagesPerDay = result[0];
        this.pagesLeft = result[1];
        this.daysLeft = result[2];
        this.bookProgress = String.valueOf(result[3])+"%";

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

//        SET COVER
//        File filePath = Environment.getExternalStorageDirectory();
//        File dir = new File(filePath.getAbsolutePath() + "/BookStats/covers/");
//        File file = new File(dir,
//                theBook.getTitle().replace(" ", "_") + ".jpg");
//
//        if (file.exists()) {
//            Picasso.get().invalidate(file);
//            Picasso.get()
//                    .load(file)
//                    .resize(400, 550)
//                    .centerCrop()
//                    .into(viewCoverImage);
//        }

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

    private static class FormatDateAsynctask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String[] input) {
            String[] result = new String[2];

            SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            Date dateA = null;
            Date dateB = null;
            try {
                dateA = parser.parse(input[0]);
                dateB = parser.parse(input[1]);
            } catch (Exception e) {

            }

            SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
            result[0] = formatter.format(dateA);
            result[1] = formatter.format(dateB);

            return result;
        }
    }

    private static class CalculateStatsAsyncTask extends AsyncTask<Void, Void, Integer[]> {

        Book book;

        CalculateStatsAsyncTask(Book book) {
            this.book = book;
        }

        @Override
        protected Integer[] doInBackground(Void... voids) {
            Integer[] result = new Integer[4];
            int pagesPerDay = 1;
            int daysLeft, pagesLeft;

            // DAYS SPENT
            long daysSpent;
            daysSpent = new Long(0L);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date firstDate = sdf.parse(book.getDateAdded());
                Date secondDate = sdf.parse(book.getDateLastPage());
                long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                daysSpent = diff;
            } catch (Exception e) {
            }

            // PAGES PER DAY

            if (daysSpent == 0) {
                daysSpent = 1;
            }
            if (book.getCurrentPage() > 0) {
                pagesPerDay = book.getCurrentPage() / (int) daysSpent;
                if (pagesPerDay < 1) {
                    pagesPerDay = 1;
                }
            }

            // CALCULATE PROGRESS
            int bookCurrenPage = book.getCurrentPage();
            double bookProgressCalc = 100.0 / (Double.valueOf(book.getAllPages()) / Double.valueOf(bookCurrenPage));

            // PAGES LEFT, DAYS LEFT
            pagesLeft = book.getAllPages() - book.getCurrentPage();
            daysLeft = pagesLeft / pagesPerDay;
            if (daysLeft == 0) {
                daysLeft = 1;
            }

            result[0] = pagesPerDay;
            result[1] = pagesLeft;
            result[2] = daysLeft;
            result[3] = (int) bookProgressCalc;

            return result;
        }
    }

}