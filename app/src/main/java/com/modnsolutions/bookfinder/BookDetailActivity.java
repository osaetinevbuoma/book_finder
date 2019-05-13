package com.modnsolutions.bookfinder;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout_book_detail, new BookDetailFragment())
                .commit();

        // Get search query for SearchView intent and send to BookFinderActivity for query.
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Intent searchIntent = new Intent(this, BookFinderActivity.class);
            searchIntent.putExtra(SearchResultsFragment.QUERY_STRING_EXTRA, intent
                    .getStringExtra(SearchManager.QUERY));
            startActivity(searchIntent);
        }
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
            /*case R.id.action_view_bookmark:
                startActivity(new Intent(this, BookmarkActivity.class));
                return true;*/

            case android.R.id.home:
                // When the Up button is pressed, the SearchResultsActivity should have the query
                // string and the current position of the book that was clicked. This will ensure
                // that the fragment does not query for books with "Null" string titles and that
                // the user is taken to the position of the book that was clicked.
                Intent intent = getIntent();
                Intent returnIntent = new Intent(this, BookFinderActivity.class);
                returnIntent.putExtra(SearchResultsFragment.QUERY_STRING_EXTRA,
                        intent.getStringExtra(SearchResultsFragment.QUERY_STRING_EXTRA));
                returnIntent.putExtra(SearchResultsFragment.QUERY_POSITION_EXTRA,
                        intent.getIntExtra(SearchResultsFragment.QUERY_POSITION_EXTRA, -1));
                startActivity(returnIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
