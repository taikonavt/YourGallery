package com.example.maxim.imageviewer.mvp.model.entity.instagram;

import com.example.maxim.imageviewer.mvp.model.entity.instagram.instamedia.Data;
import com.example.maxim.imageviewer.mvp.model.entity.instagram.instamedia.Meta;
import com.example.maxim.imageviewer.mvp.model.entity.instagram.instamedia.Pagination;

public class InstaMedia {
    private Data[] data;
    private Meta meta;
    private Pagination pagination;

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
