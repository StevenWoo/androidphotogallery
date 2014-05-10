package com.tackable.foobar.android.photogallery;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by stevenwoo on 5/9/14.
 */
public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mUrl;

    void parseItems(ArrayList<GalleryItem> items ) throws IOException {

    }

    public String toString() {
        return mCaption;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
