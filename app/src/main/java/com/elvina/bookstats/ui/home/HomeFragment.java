package com.elvina.bookstats.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elvina.bookstats.ui.book.AddBookActivity;
import com.elvina.bookstats.R;
import com.elvina.bookstats.ui.book.ViewBookActivity;
import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.database.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private BookViewModel bookViewModel;

    private FloatingActionButton buttonAddBook;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        buttonAddBook = root.findViewById(R.id.button_add_book);
        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBookActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // RECYCLERVIEW
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter((adapter));

        // VIEWMODEL
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        bookViewModel.getAllBooks().observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                adapter.submitList(books);
            }
        });

        // ON CLICK
        adapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Intent intent = new Intent(getActivity(), ViewBookActivity.class);
                intent.putExtra(ViewBookActivity.EXTRA_ID, book.getId());
                intent.putExtra(ViewBookActivity.EXTRA_TITLE, book.getTitle());
                intent.putExtra(ViewBookActivity.EXTRA_AUTHOR, book.getAuthor());
                intent.putExtra(ViewBookActivity.EXTRA_YEAR, book.getYear());
                intent.putExtra(ViewBookActivity.EXTRA_DATE_ADDED, book.getDateAdded());
                intent.putExtra(ViewBookActivity.EXTRA_ALL_PAGES, book.getAllPages());
                intent.putExtra(ViewBookActivity.EXTRA_CURRENT_PAGE, book.getCurrentPage());
                intent.putExtra(ViewBookActivity.EXTRA_DATE_LAST_PAGE,
                        book.getDateLastPage());
//                Toast.makeText(getActivity(), "int: "+book.getAllPages(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String title = data.getStringExtra(AddBookActivity.EXTRA_TITLE);
            String author = data.getStringExtra(AddBookActivity.EXTRA_AUTHOR);
            String year = data.getStringExtra(AddBookActivity.EXTRA_YEAR);
            String dateAdded = data.getStringExtra(AddBookActivity.EXTRA_DATE_ADDED);
            int allPages = data.getIntExtra(AddBookActivity.EXTRA_ALL_PAGES, 1);

            Book book = new Book(title, author, dateAdded, year, allPages);
            bookViewModel.insert(book);
            Toast.makeText(getActivity(), "Added Book.", Toast.LENGTH_SHORT).show();

        }
    }
}