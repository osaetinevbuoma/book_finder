package com.modnsolutions.bookfinder;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.modnsolutions.bookfinder.utils.Utilities;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout_book_detail, new BookDetailFragment())
                .commit();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.
                CONNECTIVITY_SERVICE);
        if (Utilities.checkInternetConnectivity(connectivityManager)) {
            // Get search query for SearchView intent and send to BookFinderActivity for query.
            Intent intent = getIntent();
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                Intent searchIntent = new Intent(this, BookFinderActivity.class);
                searchIntent.putExtra(SearchResultsFragment.QUERY_STRING_EXTRA, intent
                        .getStringExtra(SearchManager.QUERY));
                startActivity(searchIntent);
            }
        } else Utilities.toastMessage(this, getString(R.string.no_network));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView.
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // When the Up button is pressed, the BookFinderActivity should have the query
                // string and the current position of the book that was clicked. This will ensure
                // that the fragment does not query for books with "Null" string titles and that
                // the user is taken to the position of the book that was clicked.
                Intent intent = getIntent();
                Intent returnIntent = new Intent(this, BookFinderActivity.class);
                returnIntent.putExtra(SearchResultsFragment.QUERY_STRING_EXTRA,
                        intent.getStringExtra(SearchResultsFragment.QUERY_STRING_EXTRA));
                returnIntent.putExtra(SearchResultsFragment.QUERY_POSITION_EXTRA,
                        intent.getIntExtra(SearchResultsFragment.QUERY_POSITION_EXTRA, -1));
                if (intent.hasExtra(BookmarkFragment.TAB_FRAGMENT_EXTRA)) {
                    returnIntent.putExtra(BookmarkFragment.TAB_FRAGMENT_EXTRA, intent.getIntExtra(
                            BookmarkFragment.TAB_FRAGMENT_EXTRA, -1));
                }
                startActivity(returnIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
