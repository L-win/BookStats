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

import org.w3c.dom.Text;

public class ViewBookFragment extends Fragment implements AddCurrentPageDialog.AddCurrentPageDialogListener {
    TextView allPagesS;
    Bundle bundle;
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

        bundle = getArguments();

        TextView titleS = root.findViewById(R.id.text_book_title);
        TextView authorS = root.findViewById(R.id.text_book_author);
        TextView yearS = root.findViewById(R.id.text_book_year);
        TextView allPagesS = root.findViewById(R.id.text_book_all_pages);
        TextView currentPageS = root.findViewById(R.id.text_book_curent_page);

        titleS.setText(bundle.getString("title"));
        authorS.setText(bundle.getString("author"));
        yearS.setText(bundle.getString("year"));
        allPagesS.setText(String.valueOf(bundle.getInt("allpages")));
        currentPageS.setText(String.valueOf(bundle.getInt("currentpage")));

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
//        Bundle bundle = getArguments();
        AddCurrentPageDialog addCurrentPageDialog = new AddCurrentPageDialog();
        addCurrentPageDialog.setArguments(bundle);
        addCurrentPageDialog.show(getChildFragmentManager(),"dialog");
    }

    @Override
    public void applyTexts(int currentPage) {
        allPagesS.setText(String.valueOf(currentPage));
    }
}