package com.modnsolutions.bookfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.modnsolutions.bookfinder.adapter.BookFinderAdapter;
import com.modnsolutions.bookfinder.loader.SearchResultLoader;

import org.json.JSONArray;

import java.util.LinkedList;
import java.util.List;


public class SearchResultsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<JSONArray> {

    public static final String FRAGMENT_TAG = SearchResultsFragment.class.getCanonicalName();
    public static final String QUERY_STRING_EXTRA =
            "com.modnsolutions.bookfinder.QUERY_STRING_EXTRA";
    private static final int LOADER_ID = 0;

    private RecyclerView mSearchResultsRV;
    private BookFinderAdapter mAdapter;
    private SearchResultsFragmentListener mListener;
    private String mQueryString;
    private int mStartIndex = 0;
    private LoaderManager mLoaderManager;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoaderManager = LoaderManager.getInstance(this);

        if (mLoaderManager.getLoader(LOADER_ID) != null)
            mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_results, container, false);

        Intent intent = getActivity().getIntent();
        mQueryString = intent.getStringExtra(QUERY_STRING_EXTRA);

        Bundle bundle = new Bundle();
        bundle.putString(QUERY_STRING_EXTRA, mQueryString);
        mLoaderManager.restartLoader(LOADER_ID, bundle, this);

        mSearchResultsRV = root.findViewById(R.id.recycler_view_search_results);
        mAdapter = new BookFinderAdapter(getContext(), null, mListener);
        mSearchResultsRV.setAdapter(mAdapter);
        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    private LinkedList<Book> initializeSampleData() {
        LinkedList<Book> books = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Book book = new Book(getResources().getString(R.string.book_title),
                    getResources().getString(R.string.book_description));

            books.add(book);
        }

        return books;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchResultsFragmentListener) {
            mListener = (SearchResultsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchResultsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @NonNull
    @Override
    public Loader<JSONArray> onCreateLoader(int id, @Nullable Bundle args) {
        return new SearchResultLoader(getContext(), mQueryString, mStartIndex);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONArray> loader, JSONArray data) {
        if (data != null) {
            mAdapter.setBooks(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONArray> loader) {

    }

    public interface SearchResultsFragmentListener {
        // When an search result in the recyclerview is clicked.
        void onSearchResultClicked(String id);
    }
}
