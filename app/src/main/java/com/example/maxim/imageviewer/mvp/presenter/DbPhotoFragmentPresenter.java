package com.example.maxim.imageviewer.mvp.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.maxim.imageviewer.common.Directories;
import com.example.maxim.imageviewer.mvp.model.entity.Photo;
import com.example.maxim.imageviewer.mvp.model.entity.User;
import com.example.maxim.imageviewer.mvp.model.repository.Repository;
import com.example.maxim.imageviewer.mvp.presenter.list.IDbPhotoListPresenter;
import com.example.maxim.imageviewer.mvp.view.DbPhotoFragmentView;
import com.example.maxim.imageviewer.mvp.view.item.DbPhotoItemView;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

@InjectViewState
public class DbPhotoFragmentPresenter extends MvpPresenter<DbPhotoFragmentView> {

    IDbPhotoListPresenter photoListPresenter;
    private Scheduler mainThreadScheduler;
    private boolean onlyFavourites;
    private User user;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    Repository repository;

    public DbPhotoFragmentPresenter(Scheduler scheduler, boolean onlyFavourites){
        mainThreadScheduler = scheduler;
        this.onlyFavourites = onlyFavourites;
        photoListPresenter = new DbPhotoListPresenterImpl();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        repository.synchronizePhotos();
    }

    @Override
    public void attachView(DbPhotoFragmentView view) {
        super.attachView(view);
        loadInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @SuppressLint("CheckResult")
    public void loadInfo() {
        getViewState().showLoading();
        repository.getUser()
                .subscribe(user -> {
                    this.user = user;
                    compositeDisposable.add(
                            repository.getPhotoListFromDb(onlyFavourites)
                            .observeOn(mainThreadScheduler)
                            .subscribe(arrayList -> {
                                this.user.setPhotoListFromDb(arrayList);
                                getViewState().hideLoading();
                                getViewState().updateList();
                            }, throwable -> {
                                getViewState().hideLoading();
                                Timber.e(throwable, "Failed to download photo from Db");
                                getViewState().showError(throwable.getMessage());
                            }));
                }, throwable -> {
                    getViewState().hideLoading();
                    Timber.e(throwable, "Failed to get user from Db");
                    getViewState().showError(throwable.getMessage());
                });
    }

    public IDbPhotoListPresenter getPhotoListPresenter() {
        return photoListPresenter;
    }

    public class DbPhotoListPresenterImpl implements IDbPhotoListPresenter {

        PublishSubject<DbPhotoItemView> clickSubject = PublishSubject.create();
        PublishSubject<DbPhotoItemView> favouriteClickSubject = PublishSubject.create();

        DbPhotoListPresenterImpl(){
            Disposable disposable = favouriteClickSubject
                    .subscribe(itemView -> {
                        Photo photo = user.getPhotoListFromDb().get(itemView.getPos());
                        if (itemView.getIsFavourite()){
                            photo.setFavourite(false);
                            repository.updatePhoto(photo);
                        } else {
                            photo.setFavourite(true);
                            repository.updatePhoto(photo);
                        }
                    });
            compositeDisposable.add(disposable);
        }

        @Override
        public PublishSubject<DbPhotoItemView> getClickSubject() {
            return clickSubject;
        }

        @Override
        public PublishSubject<DbPhotoItemView> getFavouriteClickSubject() {
            return favouriteClickSubject;
        }

        @Override
        public void bindView(DbPhotoItemView itemView) {
            Photo photo = user.getPhotoListFromDb().get(itemView.getPos());
            String thumbnailPath = Directories.getThumbnailDir() + photo.getName();
            itemView.setImage(thumbnailPath);
            itemView.setIsFavourite(photo.isFavourite());
        }

        @Override
        public int getItemCount() {
            if (user == null) {
                return 0;
            } else if (user.getPhotoListFromDb() == null){
                return 0;
            } else {
                return user.getPhotoListFromDb().size();
            }
        }
    }
}
