package com.modnsolutions.bookfinder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modnsolutions.bookfinder.adapter.BookmarkAdapter;
import com.modnsolutions.bookfinder.db.entity.BookEntity;
import com.modnsolutions.bookfinder.db.viewmodel.BookViewModel;

import java.util.List;

public class BookmarkFragment extends Fragment {

    public static final String FRAGMENT_TAG = BookmarkFragment.class.getCanonicalName();
    public static final String TAB_FRAGMENT_EXTRA = "com.modnsolutions.bookfinder.TAB_FRAGMENT_EXTRA";

    private BookFragmentListener mListener;
    private BookmarkAdapter mAdapter;
    private RecyclerView mBookmarkRV;
    private BookViewModel mBookViewModel;
    private TextView mNoFavoriteTextView;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_bookmark, container, false);

        mNoFavoriteTextView = rootView.findViewById(R.id.textview_no_favorite);
        mBookmarkRV = rootView.findViewById(R.id.recyclerview_bookmark);
        mAdapter = new BookmarkAdapter(getContext(), null, mListener);
        mBookmarkRV.setAdapter(mAdapter);
        mBookmarkRV.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration verticalItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        DividerItemDecoration horizontalItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL);
        mBookmarkRV.addItemDecoration(verticalItemDecoration);
        mBookmarkRV.addItemDecoration(horizontalItemDecoration);

        // Use view model to interact with data from DB.
        mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        mBookViewModel.findAllFavorite().observe(this, new Observer<List<BookEntity>>() {
            @Override
            public void onChanged(List<BookEntity> books) {
                mAdapter.setBooks(books);
                mNoFavoriteTextView.setVisibility(View.INVISIBLE);
            }
        });

        if (mAdapter.getItemCount() == 0) mNoFavoriteTextView.setVisibility(View.VISIBLE);
        else mNoFavoriteTextView.setVisibility(View.INVISIBLE);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookFragmentListener) {
            mListener = (BookFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BookFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface BookFragmentListener {
        // View book details
        void onBookmarkClicked(String id);
    }
}
