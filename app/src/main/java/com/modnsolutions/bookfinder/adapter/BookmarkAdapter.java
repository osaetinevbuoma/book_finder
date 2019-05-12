package com.modnsolutions.bookfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.modnsolutions.bookfinder.Book;
import com.modnsolutions.bookfinder.BookmarkFragment;
import com.modnsolutions.bookfinder.R;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Book> mBooks;
    private BookmarkFragment.BookFragmentListener mListener;

    public BookmarkAdapter(Context context, List<Book> books,
                           BookmarkFragment.BookFragmentListener listener) {
        mInflater = LayoutInflater.from(context);
        mBooks = books;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.bookmark_recyclerview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO: Bind appropiately
    }

    @Override
    public int getItemCount() {
        if (mBooks == null) return 0;
        return mBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onBookmarkClicked(null);
        }
    }
}
