package com.example.maxim.imageviewer.mvp.model.entity.instagram.instamedia;

public class Images {
    private Image thumbnail;
    private Image low_resolution;
    private Image standard_resolution;

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Image getLowResolution() {
        return low_resolution;
    }

    public void setLowResolution(Image low_resolution) {
        this.low_resolution = low_resolution;
    }

    public Image getStandardResolution() {
        return standard_resolution;
    }

    public void setStandardResolution(Image standard_resolution) {
        this.standard_resolution = standard_resolution;
    }
}
