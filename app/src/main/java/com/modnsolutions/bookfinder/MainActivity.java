package com.modnsolutions.bookfinder;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.modnsolutions.bookfinder.utils.Utilities;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView logoImageView = findViewById(R.id.img_view_logo);
        Glide.with(this)
                .load(getResources().getDrawable(R.drawable.logo))
                .centerCrop()
                .into(logoImageView);

        EditText queryEditText = findViewById(R.id.edit_text_query);
        queryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String queryString = v.getText().toString();

                    // Get connection status.
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.
                            CONNECTIVITY_SERVICE);

                    // Check availability of network connection and that a query string is entered.
                    if (Utilities.checkInternetConnectivity(connMgr) && queryString.length()
                            != 0) {
                        Intent intent = new Intent(getApplicationContext(), BookFinderActivity.class);
                        intent.putExtra(SearchResultsFragment.QUERY_STRING_EXTRA, queryString);
                        startActivity(intent);
                    } else {
                        if (queryString.length() == 0) {
                            Utilities.toastMessage(getApplicationContext(),
                                    getString(R.string.no_book_title));
                        } else {
                            Utilities.toastMessage(getApplicationContext(),
                                    getString(R.string.no_network));
                        }
                    }

                    return true;
                }
                return false;
            }
        });
    }
}
