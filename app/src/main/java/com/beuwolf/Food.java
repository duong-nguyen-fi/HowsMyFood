package com.beuwolf.howsmyfood;

/**
 * Created by Beuwolf on 18-Nov-17.
 */

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Beuwolf on 07-Oct-17.
 */

public class Food implements Serializable {
    public String getName() {
        return name;
    }

    String name;

    public String getDate() {
        return date;
    }

    public Uri getPhotoURI() {
        return Uri.parse(photoStrURI);
    }

    private String date;
    private String photoStrURI;

    Food(String name, String date, String photoStrURI) {
        this.name = name;
        this.date = date;
        this.photoStrURI = photoStrURI;
    }


}