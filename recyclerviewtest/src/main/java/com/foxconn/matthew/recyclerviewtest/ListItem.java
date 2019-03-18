package com.foxconn.matthew.recyclerviewtest;

/**
 * Created by Matthew on 2017/7/28.
 */

public class ListItem {
    private int imageId;
    private String name;

    public ListItem(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getNumber() {
        return name;
    }
}
