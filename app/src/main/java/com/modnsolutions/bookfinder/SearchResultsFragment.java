package com.modnsolutions.bookfinder;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


/**
 * TODO: Reload more books once the user scrolls to bottom of screen.
 */
public class SearchResultsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<JSONArray> {

    public static final String FRAGMENT_TAG = SearchResultsFragment.class.getCanonicalName();
    public static final String QUERY_STRING_EXTRA =
            "com.modnsolutions.bookfinder.QUERY_STRING_EXTRA";
    public static final String QUERY_ID_EXTRA = "com.modnsolutions.bookfinder.QUERY_ID_EXTRA";
    public static final String QUERY_POSITION_EXTRA =
            "com.modnsolutions.bookfinder.QUERY_POSITION_EXTRA";
    private static final int LOADER_ID = 0;

    private RecyclerView mSearchResultsRV;
    private BookFinderAdapter mAdapter;
    private SearchResultsFragmentListener mListener;
    private String mQueryString;
    private int mStartIndex = 0;
    private LoaderManager mLoaderManager;
    private int mPosition;

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

        // Check if search query intent comes from search action bar or main activity screen.
        Intent intent = getActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQueryString = intent.getStringExtra(SearchManager.QUERY);
        } else {
            mQueryString = intent.getStringExtra(SearchResultsFragment.QUERY_STRING_EXTRA);
        }

        // Create query bundle for the load manager and restart the load manager.
        Bundle bundle = new Bundle();
        bundle.putString(QUERY_STRING_EXTRA, mQueryString);
        mLoaderManager.restartLoader(LOADER_ID, bundle, this);

        // Set up recycler view and adapter.
        mSearchResultsRV = root.findViewById(R.id.recycler_view_search_results);
        mAdapter = new BookFinderAdapter(getContext(), null, mQueryString, mListener);
        mSearchResultsRV.setAdapter(mAdapter);
        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(getContext()));

        // If the Up button was clicked, get the position of the book that was clicked
        if (intent.hasExtra(QUERY_POSITION_EXTRA)) {
            mPosition = intent.getIntExtra(QUERY_POSITION_EXTRA, 0);
        }

        return root;
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
            // Return the user to the position of the clicked book
            // Or put the user at the first book.
            mSearchResultsRV.scrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONArray> loader) {

    }

    public interface SearchResultsFragmentListener {
        // When an search result in the recyclerview is clicked.
        void onSearchResultClicked(String id, String query, int position);
    }
}
