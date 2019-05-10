package com.modnsolutions.bookfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
                    Toast.makeText(getApplicationContext(), "Search query sent",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
}
