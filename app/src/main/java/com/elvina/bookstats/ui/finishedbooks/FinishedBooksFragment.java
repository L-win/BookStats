package com.elvina.bookstats.ui.finishedbooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elvina.bookstats.R;
import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.ui.book.ViewBookActivity;
import com.elvina.bookstats.ui.home.BookAdapter;
import com.elvina.bookstats.ui.home.HomeViewModel;

import java.util.List;

public class FinishedBooksFragment extends Fragment {

    FinishedBooksViewModel finishedBooksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finished_books, container, false);

        // RECYCLERVIEW
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter((adapter));

        // VIEWMODEL
        finishedBooksViewModel = new ViewModelProvider(this).get(FinishedBooksViewModel.class);
        finishedBooksViewModel.getAllFinishedBooks().observe(
                getViewLifecycleOwner(),
                new Observer<List<Book>>() {
                    @Override
                    public void onChanged(List<Book> books) {
                        adapter.submitList(books);
                    }
                });

        adapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Intent intent = new Intent(getActivity(), ViewBookActivity.class);
                intent.putExtra(ViewBookActivity.EXTRA_ID, book.getId());

                startActivity(intent);
            }
        });

        return root;
    }
}