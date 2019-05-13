package com.modnsolutions.bookfinder.utils;

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

}
