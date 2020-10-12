package com.elvina.bookstats.ui.book;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.elvina.bookstats.R;
import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.database.BookViewModel;

public class AddCurrentPageDialog extends AppCompatDialogFragment {

    private EditText editTextCurrentPage;
    private CheckBox checkboxBookStatus;
    private AddCurrentPageDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Enter current page.")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(editTextCurrentPage.getText().toString().trim().isEmpty()){
                            Toast.makeText(getActivity(), "field is empty",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            int currentPage = Integer.parseInt(editTextCurrentPage.getText().toString());
                            listener.applyTexts(currentPage);
                        }

                    }
                });

        editTextCurrentPage = view.findViewById(R.id.edit_text_current_page);
        checkboxBookStatus = view.findViewById(R.id.checkbox_book_status);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddCurrentPageDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement AddCurrentPageDialogListener");
        }
    }

    public interface AddCurrentPageDialogListener {
        void applyTexts(int currentPage);
    }
}
