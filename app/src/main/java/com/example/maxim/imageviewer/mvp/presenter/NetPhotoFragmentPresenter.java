package com.example.maxim.imageviewer.mvp.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.maxim.imageviewer.App;
import com.example.maxim.imageviewer.NetworkStatus;
import com.example.maxim.imageviewer.R;
import com.example.maxim.imageviewer.common.Const;
import com.example.maxim.imageviewer.mvp.model.entity.Envelop;
import com.example.maxim.imageviewer.mvp.model.entity.User;
import com.example.maxim.imageviewer.mvp.model.entity.instagram.instamedia.Data;
import com.example.maxim.imageviewer.mvp.model.repository.Repository;
import com.example.maxim.imageviewer.mvp.presenter.list.INetPhotoListPresenter;
import com.example.maxim.imageviewer.mvp.view.NetPhotoFragmentView;
import com.example.maxim.imageviewer.mvp.view.item.NetPhotoItemView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

@InjectViewState
public class NetPhotoFragmentPresenter extends MvpPresenter<NetPhotoFragmentView> {

    INetPhotoListPresenter photoListPresenter;
    private Scheduler mainThreadScheduler;
    private User user;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    Repository repository;

    public NetPhotoFragmentPresenter(Scheduler scheduler) {
        mainThreadScheduler = scheduler;
        photoListPresenter = new PhotoListPresenterImpl();
    }

    @Override
    public void attachView(NetPhotoFragmentView view) {
        super.attachView(view);
        loadInfo();
    }

    @SuppressLint("CheckResult")
    private void loadInfo() {
        getViewState().showLoading();
        repository.getUser()
                .observeOn(mainThreadScheduler)
                .subscribe(user -> {
                    this.user = user;
                    compositeDisposable.add(
                            repository.getDataListFromNet(user)
                                    .observeOn(mainThreadScheduler)
                                    .subscribe(envelop -> {
                                        switch (envelop.getType()){
                                            case LIST: {
                                                List list = (List) envelop.getData();
                                                this.user.setDataListFromNet(list);
                                                getViewState().hideLoading();
                                                getViewState().updateList();
                                                break;
                                            }
                                            case MESSAGE: {
                                                getViewState().hideLoading();
                                                getViewState().showError((String) envelop.getData());
                                            }
                                        }
                                    }, throwable -> {
                                        getViewState().hideLoading();
                                        Timber.e(throwable, App.getInstance().getString(R.string.error_photo_from_net));
                                        getViewState().showError(throwable.getMessage());
                                    }));
                }, throwable -> {
                    getViewState().hideLoading();
                    Timber.e(throwable, App.getInstance().getString(R.string.error_user_from_db));
                    getViewState().showError(throwable.getMessage());
                });
    }

    public INetPhotoListPresenter getPhotoListPresenter() {
        return photoListPresenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public class PhotoListPresenterImpl implements INetPhotoListPresenter {

        PublishSubject<NetPhotoItemView> clickSubject = PublishSubject.create();
        PublishSubject<NetPhotoItemView> downloadClickSubject = PublishSubject.create();

        PhotoListPresenterImpl(){
            Disposable disposable = downloadClickSubject
                    .subscribe(itemView -> {
                        Data data = user.getDataListFromNet().get(itemView.getPos());
                        repository.savePhotoFromNet(data);
                    });
            compositeDisposable.add(disposable);
        }

        @Override
        public PublishSubject<NetPhotoItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public PublishSubject<NetPhotoItemView> getDownloadClickSubject() {
            return downloadClickSubject;
        }

        @Override
        public void bindView(NetPhotoItemView itemView) {
            Data data = user.getDataListFromNet().get(itemView.getPos());
            itemView.setImage(data.getImages().getLowResolution().getUrl());
        }

        @Override
        public int getItemCount() {
            if (user == null) {
                return 0;
            } else if (user.getDataListFromNet() == null){
                return 0;
            } else {
                return user.getDataListFromNet().size();
            }
        }
    }
}
