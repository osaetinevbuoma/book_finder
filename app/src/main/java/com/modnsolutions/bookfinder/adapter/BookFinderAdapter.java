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
import com.modnsolutions.bookfinder.R;
import com.modnsolutions.bookfinder.SearchResultsFragment;
import com.modnsolutions.bookfinder.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookFinderAdapter extends RecyclerView.Adapter<BookFinderAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray mBooks;
    private Context mContext;
    private SearchResultsFragment.SearchResultsFragmentListener mListener;
    private String mQueryString;

    public BookFinderAdapter(Context context, JSONArray books, String queryString,
                             SearchResultsFragment.SearchResultsFragmentListener listener) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mBooks = books;
        mQueryString = queryString;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.search_results_recycler_view, parent,
                false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject book = mBooks.getJSONObject(position);

            Glide.with(mContext)
                    .load(Utilities.convertImageURL(book.getJSONObject("imageLinks")
                            .getString("thumbnail")))
                    .centerCrop()
                    .into(holder.mBookImageView);
            holder.mTitleTextView.setText(book.getString("title"));
            holder.mAuthorsTextView.setText(book.getString("authors"));
            holder.mPublisherTextView.setText(book.getString("publisher"));
            holder.mPublicationDateTextView.setText(book.getString("publishedDate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mBooks == null) return 0;
        return mBooks.length();
    }

    public void setBooks(JSONArray books) {
        mBooks = books;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mBookImageView;
        private TextView mTitleTextView;
        private TextView mAuthorsTextView;
        private TextView mPublisherTextView;
        private TextView mPublicationDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mBookImageView = itemView.findViewById(R.id.img_view_book);
            mTitleTextView = itemView.findViewById(R.id.textview_book_title);
            mAuthorsTextView = itemView.findViewById(R.id.textview_authors);
            mPublisherTextView = itemView.findViewById(R.id.textview_publisher);
            mPublicationDateTextView = itemView.findViewById(R.id.textview_publication_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                int position = getAdapterPosition();
                JSONObject book = (JSONObject) mBooks.get(position);
                mListener.onSearchResultClicked(book.getString("id"), mQueryString, position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
