package com.robotechvalley.dhopaelo.domain.view;

import java.io.Serializable;

public class OrderViewItemModel implements Serializable {
    private int image;
    private String title;

    public OrderViewItemModel(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
