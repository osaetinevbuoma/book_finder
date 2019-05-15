package com.modnsolutions.bookfinder;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.modnsolutions.bookfinder.adapter.PagerAdapter;

/**
 * TODO: Transitions.
 */
public class BookFinderActivity extends AppCompatActivity implements
        SearchResultsFragment.SearchResultsFragmentListener, BookmarkFragment.BookFragmentListener {

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_finder);

        if (findViewById(R.id.bookfinder_fragment_container) != null) mTwoPane = true;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_bookmark_saved));

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorWhite));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // If user was viewing Favorite, return to Favorite tab.
        Intent intent = getIntent();
        if (intent.hasExtra(BookmarkFragment.TAB_FRAGMENT_EXTRA))
            viewPager.setCurrentItem(intent.getIntExtra(BookmarkFragment.TAB_FRAGMENT_EXTRA,
                    -1));
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
    public void onSearchResultClicked(String id, String query, int position) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putString(SearchResultsFragment.QUERY_ID_EXTRA, id);

            BookDetailFragment bookDetailFragment = new BookDetailFragment();
            bookDetailFragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.bookfinder_fragment_container, bookDetailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(SearchResultsFragment.QUERY_STRING_EXTRA, query);
            intent.putExtra(SearchResultsFragment.QUERY_ID_EXTRA, id);
            intent.putExtra(SearchResultsFragment.QUERY_POSITION_EXTRA, position);
            startActivity(intent);
        }
    }

    @Override
    public void onBookmarkClicked(String id) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putString(SearchResultsFragment.QUERY_ID_EXTRA, id);
            bundle.putInt(BookmarkFragment.TAB_FRAGMENT_EXTRA, 1);

            BookDetailFragment bookDetailFragment = new BookDetailFragment();
            bookDetailFragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.bookfinder_fragment_container, bookDetailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(SearchResultsFragment.QUERY_ID_EXTRA, id);
            intent.putExtra(BookmarkFragment.TAB_FRAGMENT_EXTRA, 1);
            startActivity(intent);
        }
    }
}
