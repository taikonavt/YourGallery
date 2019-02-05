package com.example.maxim.imageviewer.mvp.presenter.list;

import com.example.maxim.imageviewer.mvp.view.item.DbPhotoItemView;

import io.reactivex.subjects.PublishSubject;

public interface IDbPhotoListPresenter {

    PublishSubject<DbPhotoItemView> getClickSubject();

    void bindView(DbPhotoItemView itemView);

    int getItemCount();

    PublishSubject<DbPhotoItemView> getFavouriteClickSubject();
}
