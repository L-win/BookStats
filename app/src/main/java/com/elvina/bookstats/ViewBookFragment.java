package com.elvina.bookstats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewBookFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_view_book, container, false);

        getActivity().setTitle("Book Details");


        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        String author = bundle.getString("author");
        String year = bundle.getString("year");
        int allPages = bundle.getInt("allpages");

        TextView titleS = root.findViewById(R.id.text_book_title);
        TextView authorS = root.findViewById(R.id.text_book_author);
        TextView yearS = root.findViewById(R.id.text_book_year);
        TextView allPagesS = root.findViewById(R.id.text_book_all_pages);
        titleS.setText(title);
        authorS.setText(author);
        yearS.setText(year);
        allPagesS.setText(String.valueOf(allPages));

        return root;
    }
}