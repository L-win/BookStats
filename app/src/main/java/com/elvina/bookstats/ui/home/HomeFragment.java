package com.elvina.bookstats.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elvina.bookstats.AddBookActivity;
import com.elvina.bookstats.R;
import com.elvina.bookstats.ViewBookFragment;
import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.database.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
//                Toast.makeText(getActivity(), "title1 "+books.get(0).getTitle(), Toast.LENGTH_LONG).show();
            }
        });

        // ON CLICK
        adapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Bundle bundle = new Bundle();
                String author = book.getAuthor();
                String title = book.getTitle();
                int year = book.getYear();
                bundle.putString("author", author);
                bundle.putString("title", title);
                bundle.putInt("year",year);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ViewBookFragment viewBookFragment = new ViewBookFragment();
                viewBookFragment.setArguments(bundle);
                ft.replace(R.id.fragment_container, viewBookFragment);
                ft.commit();
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
            int year = data.getIntExtra(AddBookActivity.EXTRA_YEAR, 1);
            String dateAdded = data.getStringExtra(AddBookActivity.EXTRA_DATE_ADDED);

            Book book = new Book(title, author, dateAdded, year);
            bookViewModel.insert(book);
//            Snackbar.make(mainLayout, "saved", Snackbar.LENGTH_LONG).show();
            Toast.makeText(getActivity(), "Added Book.", Toast.LENGTH_SHORT).show();

        }
    }
}