package com.example.siva.galleryflickr;

/**
 * Created by siva on 18/9/18.
 */

public class ImageListActivity {

    private String id;
    private String title;
    private String url_s;

    public ImageListActivity(String id, String title, String url_s) {
        this.id = id;
        this.title = title;
        this.url_s = url_s;
    }

    public String getmId() {
        return id;
    }

    public String getmTitle() {
        return title;
    }

    public String getmUrl_s() {return url_s;}
}
