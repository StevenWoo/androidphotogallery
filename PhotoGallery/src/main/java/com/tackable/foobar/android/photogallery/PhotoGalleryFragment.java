package com.tackable.foobar.android.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.io.IOException;

/**
 * Created by stevenwoo on 5/9/14.
 */
public class PhotoGalleryFragment extends Fragment {
    GridView mGridView;

    private static final String TAG = "PhotoGalleryFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mGridView = (GridView) v.findViewById(R.id.gridView);


        return v;
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
//            try {
//                String result = new FlickrFetchr().getUrl("http://www.flickr.com/swoo");
                new FlickrFetchr().fetchItems();
                //Log.i(TAG, "Fetched contents of url " + result);
//            }
 //           catch (IOException ioe) {
 //               Log.e(TAG, "failed to fetch URL", ioe);
  //          }
            return null;
        }
    }

}
