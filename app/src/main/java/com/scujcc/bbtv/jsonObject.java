package com.scujcc.bbtv;

import java.io.Serializable;

public class jsonObject implements Serializable {
    private String title;
    private String quality;
    private String url;
    private String img;

    public String getImg () {
        return img;
    }

    public void setImg ( String img ) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

