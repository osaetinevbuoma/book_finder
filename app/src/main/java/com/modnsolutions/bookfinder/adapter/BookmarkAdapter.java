package com.modnsolutions.bookfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.modnsolutions.bookfinder.BookmarkFragment;
import com.modnsolutions.bookfinder.R;
import com.modnsolutions.bookfinder.db.entity.BookEntity;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<BookEntity> mBooks;
    private BookmarkFragment.BookFragmentListener mListener;

    public BookmarkAdapter(Context context, List<BookEntity> books,
                           BookmarkFragment.BookFragmentListener listener) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
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
        BookEntity book = mBooks.get(position);

        Glide.with(mContext)
                .load(book.getImageLink())
                .fitCenter()
                .into(holder.mBookImageView);

        holder.mTitleTextView.setText(book.getTitle());
        holder.mAuthorsTextView.setText(book.getAuthors());
    }

    @Override
    public int getItemCount() {
        if (mBooks == null) return 0;
        return mBooks.size();
    }

    /**
     * Add books.
     *
     * @param books
     */
    public void setBooks(List<BookEntity> books) {
        mBooks = books;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mBookImageView;
        private TextView mTitleTextView;
        private TextView mAuthorsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mBookImageView = itemView.findViewById(R.id.img_view_book);
            mTitleTextView = itemView.findViewById(R.id.textview_book_title);
            mAuthorsTextView = itemView.findViewById(R.id.textview_authors);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BookEntity book = mBooks.get(getAdapterPosition());
            mListener.onBookmarkClicked(book.getBookId());
        }
    }
}
