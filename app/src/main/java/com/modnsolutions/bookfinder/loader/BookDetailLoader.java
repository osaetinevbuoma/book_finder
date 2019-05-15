package com.modnsolutions.bookfinder.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.modnsolutions.bookfinder.utils.NetworkUtils;

import org.json.JSONObject;

public class BookDetailLoader extends AsyncTaskLoader<JSONObject> {
    private String bookId;

    public BookDetailLoader(@NonNull Context context, String bookId) {
        super(context);
        this.bookId = bookId;
    }

    @Nullable
    @Override
    public JSONObject loadInBackground() {
        return NetworkUtils.bookDetail(bookId);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
