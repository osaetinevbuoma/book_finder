package com.modnsolutions.bookfinder.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.modnsolutions.bookfinder.utils.NetworkUtils;

import org.json.JSONArray;

public class SearchResultLoader extends AsyncTaskLoader<JSONArray> {

    private String mQueryString;
    private int mStartIndex;

    public SearchResultLoader(@NonNull Context context, String queryString, int startIndex) {
        super(context);
        mQueryString = queryString;
        mStartIndex = startIndex;
    }

    @Nullable
    @Override
    public JSONArray loadInBackground() {
        return NetworkUtils.searchBook(mQueryString, mStartIndex);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
