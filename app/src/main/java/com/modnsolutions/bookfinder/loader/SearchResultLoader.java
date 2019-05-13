package com.modnsolutions.bookfinder.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.modnsolutions.bookfinder.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class SearchResultLoader extends AsyncTaskLoader<List<JSONObject>> {

    private String mQueryString;
    private int mStartIndex;

    public SearchResultLoader(@NonNull Context context, String queryString, int startIndex) {
        super(context);
        mQueryString = queryString;
        mStartIndex = startIndex;
    }

    @Nullable
    @Override
    public List<JSONObject> loadInBackground() {
        return NetworkUtils.searchBook(mQueryString, mStartIndex);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
