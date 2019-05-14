package com.modnsolutions.bookfinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class BookmarkActivity extends AppCompatActivity implements
        BookmarkFragment.BookFragmentListener {

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        if (findViewById(R.id.fragment_book_details) != null) mTwoPane = true;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_bookmark, new BookmarkFragment())
                .commit();
    }

    @Override
    public void onBookmarkClicked(String id) {
        if (mTwoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_book_details, new BookDetailFragment())
                    .addToBackStack(BookmarkFragment.FRAGMENT_TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else {
            startActivity(new Intent(this, BookDetailActivity.class));
        }
    }
}
