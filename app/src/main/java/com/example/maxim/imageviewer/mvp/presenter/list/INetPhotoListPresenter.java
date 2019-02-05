package com.example.maxim.imageviewer.mvp.presenter.list;

import com.example.maxim.imageviewer.mvp.view.item.DbPhotoItemView;
import com.example.maxim.imageviewer.mvp.view.item.NetPhotoItemView;

import io.reactivex.subjects.PublishSubject;

public interface INetPhotoListPresenter {

    PublishSubject<NetPhotoItemView> getClickSubject();

    void bindView(NetPhotoItemView itemView);

    int getItemCount();

    PublishSubject<NetPhotoItemView> getDownloadClickSubject();
}
