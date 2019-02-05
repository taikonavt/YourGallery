package com.example.maxim.imageviewer.mvp.model.image.android;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.maxim.imageviewer.App;
import com.example.maxim.imageviewer.NetworkStatus;
import com.example.maxim.imageviewer.R;
import com.example.maxim.imageviewer.common.Directories;
import com.example.maxim.imageviewer.mvp.model.entity.Photo;
import com.example.maxim.imageviewer.mvp.model.entity.instagram.instamedia.Data;
import com.example.maxim.imageviewer.mvp.model.image.ImageLoader;
import com.example.maxim.imageviewer.mvp.model.repository.IImageCache;
import com.example.maxim.imageviewer.mvp.model.repository.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ImageLoaderGlide implements ImageLoader {

    private IImageCache cache;

    public ImageLoaderGlide(IImageCache cache) {
        this.cache = cache;
    }

    @Override
    public void loadInto(@Nullable String url, ImageView container) {
        if (NetworkStatus.isOnline()) {
            GlideApp.with(container.getContext())
                    .asBitmap()
                    .load(url)
                    .placeholder(App.getInstance().getResources().getDrawable(R.drawable.ic_image_placeholder_48dp))
                    .listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Timber.e(e, "Image load failed");
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    cache.saveImage(url, resource);
                    return false;
                }
            })
                    .into(container);
        } else {
            cache.getFile(url)
                    .subscribe(new SingleObserver<File>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(File file) {
                            if (file != null) {
                                GlideApp.with(container.getContext())
                                        .load(cache.getFile(url))
                                        .into(container);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }

    @Override
    public void download(Repository repository, Data data) {
        if (NetworkStatus.isOnline()){
            GlideApp.with(App.getInstance())
                    .asBitmap()
                    .load(data.getImages().getStandardResolution().getUrl())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            Timber.e("Image load failed");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            String path = Directories.getDirPath() + data.getId() + ".jpg";
                            try (OutputStream outputStream = new FileOutputStream(new File(path))) {
                                if(resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)){
                                    Uri uri = Uri.fromFile(new File(path));
                                    String name = data.getId();
                                    Photo photo = new Photo(uri.toString(), false, name);
                                    repository.savePhotoToRepo(photo);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    })
                    .submit();
        }
    }

}
