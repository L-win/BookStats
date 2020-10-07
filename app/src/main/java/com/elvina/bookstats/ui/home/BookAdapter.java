package com.elvina.bookstats.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.elvina.bookstats.R;
import com.elvina.bookstats.database.Book;

public class BookAdapter extends ListAdapter<Book, BookAdapter.BookHolder> {

    private OnItemClickListener listener;

    public BookAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Book> DIFF_CALLBACK = new DiffUtil.ItemCallback<Book>() {
        @Override
        public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getAuthor().equals(newItem.getAuthor()) &&
                    oldItem.getYear().equals(newItem.getYear());
        }
    };

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_adapter, parent, false);
        return new BookHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book currentNote = getItem(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewAuthor.setText(currentNote.getAuthor());
        holder.textViewYear.setText(currentNote.getYear());

//        if (currentNote.getPriority() == 0) {
//            holder.textViewPriority.setBackground(null);
//        }
    }

    public Book getBookAt(int position) {
        return getItem(position);
    }

    class BookHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewAuthor;
        private TextView textViewYear;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewAuthor = itemView.findViewById(R.id.text_view_author);
            textViewYear = itemView.findViewById(R.id.text_view_year);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

