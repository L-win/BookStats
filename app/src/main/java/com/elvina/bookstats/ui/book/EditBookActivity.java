package com.elvina.bookstats.ui.book;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.elvina.bookstats.R;
import com.elvina.bookstats.database.Book;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class EditBookActivity extends AppCompatActivity {

    BookViewModel bookViewModel;
//    EditBookViewModel editBookViewModel;

    EditText viewTitle, viewAuthor, viewYear, viewPages;
    SwitchCompat viewReadStatus;
    ImageView viewImage;

    String dateAdded, coverUri, dateLastPage;
    int bookId, currentPage;

    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        setTitle("Edit Book");

        Intent intent = getIntent();
        bookId = intent.getIntExtra(ViewBookActivity.EXTRA_ID, 1);

        prepareViews();
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        bookViewModel
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
                bookViewModel.update(newBook);
                Toast.makeText(
                        EditBookActivity.this,
                        "updated",
                        Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button changeBookCover = findViewById(R.id.button_book_cover_change);
        changeBookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void prepareViews() {
        viewTitle = findViewById(R.id.edit_text_title_edit);
        viewReadStatus = findViewById(R.id.switch_reading_status);
        viewAuthor = findViewById(R.id.edit_text_author_edit);
        viewYear = findViewById(R.id.edit_text_year_edit);
        viewPages = findViewById(R.id.edit_text_pages_edit);

        viewImage = findViewById(R.id.image_book_cover);
    }

    private void setValues(Book book) {
        viewTitle.setText(book.getTitle());
        viewAuthor.setText(book.getAuthor());
        viewYear.setText(book.getYear());
        viewPages.setText(String.valueOf(book.getAllPages()));
        viewReadStatus.setChecked(!book.getReadingStatus());
        dateAdded = book.getDateAdded();
        currentPage = book.getCurrentPage();
        this.coverUri = book.getCoverUri();
        this.dateLastPage = book.getDateLastPage();

        if (this.coverUri != null) {
            Uri imageUri = Uri.parse(this.coverUri);
            Picasso.get()
                    .load(imageUri)
                    .resize(400, 550)
                    .centerCrop()
                    .into(viewImage);
        }

    }

    private Book getValues() {
        String newTitle = viewTitle.getText().toString();
        String newAuthor = viewAuthor.getText().toString();
        String newYear = viewYear.getText().toString();
        String newPages = viewPages.getText().toString();
        boolean readingStatus = viewReadStatus.isChecked();

        Book newBook = new Book(
                newTitle,
                newAuthor,
                newYear,
                Integer.parseInt(newPages)
        );
        newBook.setId(this.bookId);
        newBook.setDateAdded(this.dateAdded);
        newBook.setCoverUri(this.coverUri);
        newBook.setReadingStatus(!readingStatus);
        newBook.setCurrentPage(this.currentPage);
        newBook.setDateLastPage(this.dateLastPage);
        return newBook;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Picasso.get()
                    .load(imageUri)
                    .resize(400, 550)
                    .centerCrop()
                    .into(viewImage);

            writeCoverToFile(createCover(imageUri));
        }
    }

    private Bitmap createCover(Uri uri) {
        Bitmap bookCover = null;
        try {
            bookCover = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (Exception e) {
            System.out.println("TEST-0: bitmap exception " + e);
        }
        return bookCover;
    }

    private void writeCoverToFile(Bitmap coverImage) {
        File filePath = Environment.getExternalStorageDirectory();
        File dir = new File(filePath.getAbsolutePath() + "/BookStats/covers/");
        dir.mkdir();
        File file = new File(dir, viewTitle.getText().toString().replace(" ", "_") + ".jpg");

        try {
            outputStream = new FileOutputStream(file);
        } catch (Exception e) {
            System.out.println("TEST-0: Output exception: " + e);
        }
        coverImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        try {
            outputStream.flush();
        } catch (Exception e) {

        }
        try {
            outputStream.close();
        } catch (Exception e) {

        }
    }

}