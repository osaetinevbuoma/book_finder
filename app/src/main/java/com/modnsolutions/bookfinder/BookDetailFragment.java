package com.modnsolutions.bookfinder;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.bumptech.glide.Glide;
import com.modnsolutions.bookfinder.db.entity.BookEntity;
import com.modnsolutions.bookfinder.db.viewmodel.BookViewModel;
import com.modnsolutions.bookfinder.loader.BookDetailLoader;
import com.modnsolutions.bookfinder.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

public class BookDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<JSONObject> {

    private static final int LOADER_ID = 1;
    private LoaderManager mLoaderManager;
    private ImageView mBookImageView;
    private TextView mTitleTextView;
    private TextView mAuthorsTextView;
    private TextView mPublisherTextView;
    private TextView mPublicationDateTextView;
    private TextView mPagesTextView;
    private TextView mCategoriesTextView;
    private TextView mDescriptionTextView;
    private ProgressBar mProgressBar;
    private LinearLayout mLinearLayout;
    private BookViewModel mBookViewModel;
    private BookEntity mBook;
    private JSONObject mJSONBook;

    public static final String FRAGMENT_TAG = BookDetailFragment.class.getCanonicalName();
    public static final String BOOK_ID_EXTRA = "com.modnsolutions.bookfinder.BOOK_ID_EXTRA";


    public BookDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mLoaderManager = LoaderManager.getInstance(this);
        if (mLoaderManager.getLoader(LOADER_ID) != null)
            mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_book_detail, container, false);

        mLinearLayout = root.findViewById(R.id.ll);
        mBookImageView = root.findViewById(R.id.img_view_book);
        mTitleTextView = root.findViewById(R.id.textview_book_title);
        mAuthorsTextView = root.findViewById(R.id.textview_authors);
        mPublisherTextView = root.findViewById(R.id.textview_publisher);
        mPublicationDateTextView = root.findViewById(R.id.textview_publication_date);
        mPagesTextView = root.findViewById(R.id.textview_pages);
        mCategoriesTextView = root.findViewById(R.id.textview_categories);
        mDescriptionTextView = root.findViewById(R.id.textview_book_description);
        mProgressBar = root.findViewById(R.id.progress_bar);

        mLinearLayout.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        Intent intent = new Intent();
        Bundle bundle = getArguments();
        if (bundle != null) {
            intent.putExtra(SearchResultsFragment.QUERY_ID_EXTRA, bundle.getString(
                    SearchResultsFragment.QUERY_ID_EXTRA));
        } else intent = getActivity().getIntent();

        if (intent != null) {
            // Find book in favorite. If it exist, display it, if not get book details from
            // Google Books API.
            mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
            mBook = mBookViewModel.findFavorite(intent.getStringExtra(
                    SearchResultsFragment.QUERY_ID_EXTRA));
            if (mBook != null) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mLinearLayout.setVisibility(View.VISIBLE);

                Glide.with(this)
                        .load(mBook.getImageLink())
                        .fitCenter()
                        .into(mBookImageView);
                mBookImageView.setContentDescription(mBook.getTitle());
                mTitleTextView.setText(mBook.getTitle());
                mAuthorsTextView.setText(mBook.getAuthors());
                mPublisherTextView.setText(mBook.getPublisher());
                mPublicationDateTextView.setText(mBook.getPublishedDate());
                mPagesTextView.setText(String.valueOf(mBook.getPageCount()));
                mCategoriesTextView.setText(mBook.getCategories());
                mDescriptionTextView.setText(HtmlCompat.fromHtml(mBook.getDescription(),0));
            } else {
                Bundle loaderBundle = new Bundle();
                loaderBundle.putString(BOOK_ID_EXTRA, intent.getStringExtra(SearchResultsFragment
                        .QUERY_ID_EXTRA));
                mLoaderManager.restartLoader(LOADER_ID, loaderBundle, this);
            }
        }

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bookmark, menu);

        // Get bookmark icon
        MenuItem bookmarkIcon = menu.getItem(1);
        if (mBook != null) {
            bookmarkIcon.setIcon(R.drawable.ic_bookmark_saved);
        } else {
            bookmarkIcon.setIcon(R.drawable.ic_bookmark_unsaved);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bookmark:
                // If book exist in favorite, remove it and change icon. Set mBook to null.
                // Else add to favorite and change icon.
                if (mBook != null) {
                    mBookViewModel.remove(mBook);
                    item.setIcon(R.drawable.ic_bookmark_unsaved);
                    Utilities.toastMessage(getContext(), mBook.getTitle()
                            + " has been removed from your favorites.");
                    mBook = null;

                } else {
                    if (mJSONBook != null) {
                        try {
                            BookEntity bookEntity = new BookEntity(mJSONBook.getString("id"),
                                    mJSONBook.getString("title"),
                                    mJSONBook.getString("publisher"),
                                    mJSONBook.getString("publishedDate"),
                                    Utilities.convertImageURL(mJSONBook.getJSONObject("imageLinks")
                                            .getString("thumbnail")),
                                    mJSONBook.getString("authors"),
                                    mJSONBook.getInt("pageCount"),
                                    mJSONBook.getString("categories"),
                                    mJSONBook.getString("description"));

                            mBookViewModel.insert(bookEntity);
                            item.setIcon(R.drawable.ic_bookmark_saved);
                            Utilities.toastMessage(getContext(),
                                    mJSONBook.getString("title")
                                            + " has been added to your favorites.");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        String bookId = args.getString(BOOK_ID_EXTRA, null);
        return new BookDetailLoader(getContext(), bookId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        if (data != null) {
            mJSONBook = data;
            try {
                mProgressBar.setVisibility(View.INVISIBLE);
                mLinearLayout.setVisibility(View.VISIBLE);

                Glide.with(this)
                        .load(Utilities.convertImageURL(data.getJSONObject("imageLinks")
                                .getString("thumbnail")))
                        .fitCenter()
                        .into(mBookImageView);
                mBookImageView.setContentDescription(data.getString("title"));
                mTitleTextView.setText(data.getString("title"));
                mAuthorsTextView.setText(data.getString("authors"));
                mPublisherTextView.setText(data.getString("publisher"));
                mPublicationDateTextView.setText(data.getString("publishedDate"));
                mPagesTextView.setText(String.valueOf(data.getInt("pageCount")));
                mCategoriesTextView.setText(data.getString("categories"));
                mDescriptionTextView.setText(HtmlCompat.fromHtml(data.getString("description"),
                        0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}
