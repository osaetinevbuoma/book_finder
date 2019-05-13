package com.modnsolutions.bookfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SearchResultsActivity extends AppCompatActivity implements
        SearchResultsFragment.SearchResultsFragmentListener {

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        if (findViewById(R.id.fragment_book_details) != null) mTwoPane = true;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_books_results, new SearchResultsFragment())
                .addToBackStack(SearchResultsFragment.FRAGMENT_TAG)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_view_bookmark:
                startActivity(new Intent(this, BookmarkActivity.class));
                return true;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchResultClicked(String id, String queryString, int position) {
        if (mTwoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_book_details, new BookDetailFragment())
                    .addToBackStack(BookDetailFragment.FRAGMENT_TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(SearchResultsFragment.QUERY_STRING_EXTRA, queryString);
            intent.putExtra(SearchResultsFragment.QUERY_ID_EXTRA, id);
            intent.putExtra(SearchResultsFragment.QUERY_POSITION_EXTRA, position);
            startActivity(intent);
        }
    }
}
