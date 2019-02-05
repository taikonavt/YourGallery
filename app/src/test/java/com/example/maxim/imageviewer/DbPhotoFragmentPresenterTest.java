package com.example.maxim.imageviewer;

import com.example.maxim.imageviewer.common.Const;
import com.example.maxim.imageviewer.di.DaggerTestComponent;
import com.example.maxim.imageviewer.di.TestComponent;
import com.example.maxim.imageviewer.di.modules.TestRepositoryModule;
import com.example.maxim.imageviewer.mvp.model.entity.User;
import com.example.maxim.imageviewer.mvp.model.repository.Repository;
import com.example.maxim.imageviewer.mvp.presenter.DbPhotoFragmentPresenter;
import com.example.maxim.imageviewer.mvp.view.DbPhotoFragmentView;
import com.example.maxim.imageviewer.ui.fragment.DbPhotoFragment;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;
import timber.log.Timber;

public class DbPhotoFragmentPresenterTest {

    private DbPhotoFragmentPresenter presenter;
    private TestScheduler testScheduler;

    @Mock
    DbPhotoFragmentView fragmentView;

    @BeforeClass
    public static void setupClass(){
        Timber.plant(new Timber.DebugTree());
        Timber.d("setup class");
    }

    @AfterClass
    public static void tearDownClass(){
        Timber.d("tear down class");
    }

    @Before
    public void setup(){
        Timber.d("setup");
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        presenter = Mockito.spy(new DbPhotoFragmentPresenter(testScheduler, DbPhotoFragment.ALL));
    }

    @After
    public void tearDown(){
        Timber.d("tearDown");
    }

    @Test
    public void loadInfoSuccess(){
        Timber.d("loadInfoSuccess()");

        String login = "login";
        String token = "accessToken";
        User user = new User(login, token);

        boolean onlyFavourites = false;

        TestComponent component = DaggerTestComponent.builder()
                .testRepositoryModule(new TestRepositoryModule(){
                    @Override
                    public Repository provideRepository() {
                        Repository repository = super.provideRepository();
                        Mockito.when(repository.getUser()).thenReturn(Single.just(user));
                        Mockito.when(repository.getPhotoListFromDb(onlyFavourites))
                                .thenReturn(Flowable.just(new ArrayList<>()));
                        return repository;
                    }
                }).build();

        component.inject(presenter);

        presenter.attachView(fragmentView);
        Mockito.verify(presenter).attachView(fragmentView);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(presenter).loadInfo();
        Mockito.verify(fragmentView).showLoading();
        Mockito.verify(fragmentView).hideLoading();
        Mockito.verify(fragmentView).updateList();
    }

    @Test
    public void loadInfoGetUserFailed(){
        Timber.d("loadInfoFailed()");
        Throwable throwable = new RuntimeException("No such user in cache");

        TestComponent component = DaggerTestComponent.builder()
                .testRepositoryModule(new TestRepositoryModule(){
                    @Override
                    public Repository provideRepository() {
                        Repository repository = super.provideRepository();
                        Mockito.when(repository.getUser()).thenReturn(Single.error(throwable));
                        return repository;
                    }
                }).build();

        component.inject(presenter);
        presenter.attachView(fragmentView);
        Mockito.verify(presenter).attachView(fragmentView);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(presenter).loadInfo();
        Mockito.verify(fragmentView).showLoading();
        Mockito.verify(fragmentView).hideLoading();
        Mockito.verify(fragmentView).showError(throwable.getMessage());
    }
}
