package com.tackable.foobar.android.photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by stevenwoo on 5/9/14.
 */
public class FlickrFetchr {

    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String API_KEY = "0073e3ffe50f16aeece2b0e16ae03601";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";
    private static final String PARAM_EXTRAS = "extras";
    private static final String PARAM_FORMAT = "format";
    private static final String PARAM_JSON ="json";
    private static final String TAG = "FlickrFetchr";
    private static final String EXTRA_SMALL_URL = "url_s";
    private static final String PARAM_TEXT = "text";

    private static final String JSON_ID = "id";
    private static final String JSON_CAPTION = "title";
    private static final String JSON_URL = "url_s";

    public static final String PREF_SEARCH_QUERY = "searchQuery";

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


    public JSONArray extractPhotos(String inputString) {
        JSONArray arrayJSON = null;
        if( inputString != null ) {
            try {
                // need to remove jsonFlickrApi( from start
                // need to remove ) from end for some crappy reason flickr put that in there
                String start = "jsonFlickrApi(";
                String end = ")";
                if (inputString.length() > start.length() + end.length()) {
                    inputString = inputString.substring(start.length(), inputString.length() - end.length());
                }
                JSONObject object = (JSONObject) new JSONTokener(inputString).nextValue();

                String photoString = object.getString("photos");
                if (null != photoString) {
                    JSONObject jsonSubObject = (JSONObject) new JSONObject(photoString);
                    String photoStream = jsonSubObject.getString("photo");
                    if (photoStream != null) {
//                        Log.i(TAG,"-------------");
//                        Log.i(TAG, photoStream);
                        // now we have json array object...
                        arrayJSON = (JSONArray) new JSONTokener(photoStream).nextValue();
                    }
                }
            }
            catch(JSONException je){

            }
        }
        return arrayJSON;
    }

    public ArrayList<GalleryItem> downloadGalleryItems(String  url) {
        ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();
        String jsonString = null;

        try {
            jsonString = getUrl(url);


        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        JSONArray arrayPhotos = extractPhotos(jsonString);
        try {
            for (int i = 0; i < arrayPhotos.length(); ++i) {
                GalleryItem item = new GalleryItem();
                JSONObject jsonPhoto = arrayPhotos.getJSONObject(i);
                item.setId(jsonPhoto.getString(JSON_ID));
                item.setCaption(jsonPhoto.getString(JSON_CAPTION));
                item.setUrl(jsonPhoto.getString(JSON_URL));
                items.add(item);
            }
        }
        catch(JSONException je ){
            Log.i(TAG, "error parsing json flickr " + je);
        }
        return items;
    }

    public ArrayList<GalleryItem> fetchItems(){
        ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();
        String url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GET_RECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter(PARAM_FORMAT, PARAM_JSON)
                    .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                    .build().toString();
            Log.i(TAG, "requested URL is ------ " + url);
        return downloadGalleryItems(url);
//            Log.i(TAG, "received items: " + jsonString);
    }

    public ArrayList<GalleryItem> search(String query) {
        String url = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_SEARCH)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                .appendQueryParameter(PARAM_FORMAT, PARAM_JSON)
                .appendQueryParameter(PARAM_TEXT, query)
                .build().toString();
        return downloadGalleryItems(url);
    }
}
