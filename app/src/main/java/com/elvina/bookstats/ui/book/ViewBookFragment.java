package com.elvina.bookstats.ui.book;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elvina.bookstats.R;

public class ViewBookFragment extends Fragment implements AddCurrentPageDialog.AddCurrentPageDialogListener {
    TextView allPagesS;
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

        Button buttonAddCurrentPage = root.findViewById(R.id.button_add_current_page);
        buttonAddCurrentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        return root;
    }

    public void openDialog(){
        AddCurrentPageDialog addCurrentPageDialog = new AddCurrentPageDialog();
//        addCurrentPageDialog.setListener(getActivity());
        addCurrentPageDialog.show(getChildFragmentManager(),"dialog");
    }

    @Override
    public void applyTexts(int currentPage) {
//        allPagesS.setText(String.valueOf(currentPage));
    }
}