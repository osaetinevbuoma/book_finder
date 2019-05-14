package com.modnsolutions.bookfinder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modnsolutions.bookfinder.adapter.BookmarkAdapter;

public class BookmarkFragment extends Fragment {

    public static final String FRAGMENT_TAG = BookmarkFragment.class.getCanonicalName();

    private BookFragmentListener mListener;
    private BookmarkAdapter mAdapter;
    private RecyclerView mBookmarkRV;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_bookmark, container, false);

        mBookmarkRV = rootView.findViewById(R.id.recyclerview_bookmark);
        mAdapter = new BookmarkAdapter(getContext(), null, mListener);
        mBookmarkRV.setAdapter(mAdapter);
        mBookmarkRV.setLayoutManager(new LinearLayoutManager(getContext()));

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
