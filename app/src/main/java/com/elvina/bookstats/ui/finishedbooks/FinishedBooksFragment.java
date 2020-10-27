package com.elvina.bookstats.ui.finishedbooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elvina.bookstats.R;
import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.ui.book.AddBookActivity;
import com.elvina.bookstats.ui.book.ViewBookActivity;
import com.elvina.bookstats.ui.home.BookAdapter;
import com.elvina.bookstats.ui.home.HomeViewModel;

import java.util.List;

public class FinishedBooksFragment extends Fragment {

    FinishedBooksViewModel finishedBooksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finished_books, container, false);

        // SET TOOLBAR
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle("Book Stats");

        // PROGRESS BAR
        final ProgressBar progressBar = root.findViewById(R.id.progress_bar);

        // TEXT IF LIST IS EMPTY
        final TextView textIfEmptyList = root.findViewById(R.id.text_empty_list);

        // RECYCLERVIEW
        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
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
                        progressBar.setVisibility(View.GONE);

                        if(books.isEmpty()){
                            textIfEmptyList.setVisibility(View.VISIBLE);
                        }else {
                            textIfEmptyList.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });

        adapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Intent intent = new Intent(getActivity(), ViewBookActivity.class);
                intent.putExtra(ViewBookActivity.EXTRA_ID, book.getId());

//                startActivity(intent);
                startActivityForResult(intent,1);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            int id = data.getIntExtra(AddBookActivity.EXTRA_ID, 0);

            Book book = new Book(null, null, null, 1);
            book.setId(id);
            finishedBooksViewModel.delete(book);
            Toast.makeText(getActivity(), "Deleted Book.", Toast.LENGTH_SHORT).show();

        }
    }
}