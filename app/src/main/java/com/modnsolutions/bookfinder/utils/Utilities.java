package com.modnsolutions.bookfinder.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utilities {

    /**
     * Google Books API does not allow insecure image loading. Convert URL string to https.
     *
     * @param imageURL original image URL
     * @return converted image URL.
     */
    public static String convertImageURL(String imageURL) {
        return "https" + imageURL.substring(4);
    }

    /**
     * Check network connection.
     *
     * @param connectivityManager
     * @return
     */
    public static boolean checkInternetConnectivity(ConnectivityManager connectivityManager) {
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Display toast message.
     * @param context
     * @param message
     */
    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
