package com.tackable.foobar.android.photogallery;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by stevenwoo on 5/9/14.
 */
public class FlickrFetchr {

    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String API_KEY = "0073e3ffe50f16aeece2b0e16ae03601";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String PARAM_EXTRAS = "extras";
    private static final String TAG = "FlickrFetchr";
    private static final String EXTRA_SMALL_URL = "url_s";

    private static final String XML_PHOTO = "photo";

    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }

    }
    public String getUrl(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }
    public void fetchItems(){
        try {
            String url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GET_RECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                    .build().toString();
            Log.i(TAG, "requested URL is ------ " + url);
            String xmlString = getUrl(url);
            Log.i(TAG, "received items: " + xmlString);

        }
        catch (IOException ioe ){
            Log.e(TAG, "Failed to fetch items " + ioe);
        }
    }
}
