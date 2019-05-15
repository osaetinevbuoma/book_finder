package com.modnsolutions.bookfinder;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modnsolutions.bookfinder.adapter.BookFinderAdapter;
import com.modnsolutions.bookfinder.loader.SearchResultLoader;
import com.modnsolutions.bookfinder.utils.NetworkUtils;
import com.modnsolutions.bookfinder.utils.Utilities;

import org.json.JSONObject;

import java.util.List;


public class SearchResultsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<JSONObject>> {

    public static final String FRAGMENT_TAG = SearchResultsFragment.class.getCanonicalName();
    public static final String QUERY_STRING_EXTRA =
            "com.modnsolutions.bookfinder.QUERY_STRING_EXTRA";
    public static final String QUERY_ID_EXTRA = "com.modnsolutions.bookfinder.QUERY_ID_EXTRA";
    public static final String QUERY_POSITION_EXTRA =
            "com.modnsolutions.bookfinder.QUERY_POSITION_EXTRA";
    public static final String QUERY_START_INDEX_EXTRA =
            "com.modnsolutions.bookfinder.QUERY_START_INDEX_POSITION";
    private static final int LOADER_ID = 0;

    private RecyclerView mSearchResultsRV;
    private ProgressBar mProgressBar;
    private TextView mNoResultTextView;
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

        // Don't show the progress bar yet.
        mProgressBar = root.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        // Reference to no result text view.
        mNoResultTextView = root.findViewById(R.id.textview_no_result);
        mNoResultTextView.setText("");
        mNoResultTextView.setVisibility(View.INVISIBLE);

        // Check Internet connection.
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Utilities.checkInternetConnectivity(connectivityManager)) {
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
            bundle.putInt(QUERY_START_INDEX_EXTRA, mStartIndex);
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

            // Add a scroll listener to listen to when the recycler view is scrolled to the end.
            mSearchResultsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    loadMoreBooks(recyclerView);
                }
            });
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            Utilities.toastMessage(getContext(), getString(R.string.no_network));
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
    public Loader<List<JSONObject>> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = args.getString(QUERY_STRING_EXTRA, mQueryString);
        int startIndex = args.getInt(QUERY_START_INDEX_EXTRA, 0);

        return new SearchResultLoader(getContext(), queryString, startIndex);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<JSONObject>> loader, List<JSONObject> data) {
        if (data != null) {
            // Remove progress bar if visible.
            if (mProgressBar.getVisibility() == View.VISIBLE)
                mProgressBar.setVisibility(View.INVISIBLE);

            mAdapter.setBooks(data);
            // Return the user to the position of the clicked book
            // Or put the user at the first book.
            mSearchResultsRV.scrollToPosition(mPosition);
        }

        if (data != null && data.size() == 0 && mAdapter.getItemCount() == 0) {
            mNoResultTextView.setText(getString(R.string.no_result_found));
            mNoResultTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<JSONObject>> loader) {

    }

    /**
     * Load more books from search query.
     * Get the last completely visible item position and check if the position is the same as
     * the last item in the adapter. If it is, fetch more books from Google Books API.
     *
     * TODO: Upgrade function to use the Paging API.
     *
     * @param recyclerView the recycler view.
     */
    private void loadMoreBooks(RecyclerView recyclerView) {
        int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findLastCompletelyVisibleItemPosition();
        if (lastPosition == mAdapter.getItemCount() - 1) {
            // Show progress bar.
            mProgressBar.setVisibility(View.VISIBLE);

            // Increment start index.
            mStartIndex += NetworkUtils.LOAD_INCREMENT;

            Bundle loadMoreBundle = new Bundle();
            loadMoreBundle.putString(QUERY_STRING_EXTRA, mQueryString);
            loadMoreBundle.putInt(QUERY_START_INDEX_EXTRA, mStartIndex);

            // Update position to last position plus one so that loader does not take user to
            // top of view.
            mPosition = lastPosition + 1;

            mLoaderManager.restartLoader(LOADER_ID, loadMoreBundle, this);
        }
    }

    public interface SearchResultsFragmentListener {
        // When an search result in the recyclerview is clicked.
        void onSearchResultClicked(String id, String query, int position);
    }
}
