package com.example.maxim.imageviewer;

import com.example.maxim.imageviewer.di.DaggerTestComponent;
import com.example.maxim.imageviewer.di.TestComponent;
import com.example.maxim.imageviewer.di.modules.ApiModule;
import com.example.maxim.imageviewer.di.modules.CacheModule;
import com.example.maxim.imageviewer.di.modules.ImageCacheModule;
import com.example.maxim.imageviewer.di.modules.ImageLoaderModule;
import com.example.maxim.imageviewer.mvp.model.entity.User;
import com.example.maxim.imageviewer.mvp.model.entity.room.RoomPhoto;
import com.example.maxim.imageviewer.mvp.model.image.ImageLoader;
import com.example.maxim.imageviewer.mvp.model.image.android.ImageLoaderGlide;
import com.example.maxim.imageviewer.mvp.model.repository.ICache;
import com.example.maxim.imageviewer.mvp.model.repository.IImageCache;
import com.example.maxim.imageviewer.mvp.model.repository.Repository;
import com.example.maxim.imageviewer.mvp.model.repository.RoomCache;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import timber.log.Timber;

import static junit.framework.Assert.assertEquals;

public class RepositoryInstrumentedTest {
    @Inject
    Repository repository;

    private static MockWebServer mockWebServer;

    @BeforeClass
    public static void setupClass() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        mockWebServer.shutdown();
    }

    @Before
    public void setup(){
        Timber.d("setup");
        TestComponent component = DaggerTestComponent
                .builder()
                .apiModule(new ApiModule(){
                    @Override
                    public String baseUrlProduction() {
                        return mockWebServer.url("/").toString();
                    }
                })
                .cacheModule(new CacheModule(){
                    @Override
                    public ICache provideCache() {
                        return Mockito.mock(RoomCache.class);
                    }
                })
                .imageLoaderModule(new ImageLoaderModule(){
                    @Override
                    public ImageLoader imageLoader(IImageCache imageCache) {
                        return Mockito.mock(ImageLoaderGlide.class);
                    }
                })
                .build();

        component.inject(this);
    }

    @Test
    public void getUser(){
        String login = "login";
        String token = "accessToken";

//        mockWebServer.enqueue(createUserResponse("someuser", "someavatar", "somerepos"));
//
        User user = new User(login, token);
        repository.saveUser(user);

        TestObserver<User> observer = new TestObserver<>();
        repository.getUser().subscribe(observer);

        observer.awaitTerminalEvent();

        observer.assertValueCount(1);
        assertEquals(observer.values().get(0).getLogin(), login);
        assertEquals(observer.values().get(0).getAccessToken(), token);
    }

    private MockResponse createUserResponse(String login, String avatarUrl, String reposUrl){
        String body = "{\"login\":\"" + login + "\", \"avatar_url\":\"" + avatarUrl + "\", \"repos_url\":\"" + reposUrl + "\"}";
        return new MockResponse().setBody(body);
    }

}
