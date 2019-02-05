package com.example.maxim.imageviewer.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.maxim.imageviewer.R;
import com.example.maxim.imageviewer.mvp.presenter.list.IDbPhotoListPresenter;
import com.example.maxim.imageviewer.mvp.view.item.DbPhotoItemView;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DbPhotoAdapter extends RecyclerView.Adapter<DbPhotoAdapter.MyViewHolderDb> {

    private IDbPhotoListPresenter presenter;

    public DbPhotoAdapter(IDbPhotoListPresenter presenter){
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolderDb onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.db_photo_item, viewGroup, false);
        return new MyViewHolderDb(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderDb myViewHolder, int position) {
        RxView.clicks(myViewHolder.itemView)
                .map(obj -> myViewHolder)
                .subscribe(presenter.getClickSubject());
        RxView.clicks(myViewHolder.favouriteButton)
                .map(obj -> myViewHolder)
                .subscribe(presenter.getFavouriteClickSubject());
        myViewHolder.pos = position;
        presenter.bindView(myViewHolder);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

    class MyViewHolderDb extends RecyclerView.ViewHolder implements DbPhotoItemView {

        private int pos;
        private boolean isFavourite;

        @BindView(R.id.db_item_image)
        ImageView imageView;
        @BindView(R.id.item_favourite_btn)
        ImageButton favouriteButton;
        @BindDrawable(R.drawable.ic_favorite_red_24dp)
        Drawable favourite;
        @BindDrawable(R.drawable.ic_favorite_border_red_24dp)
        Drawable notFavourite;

        MyViewHolderDb(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public int getPos() {
            return pos;
        }

        @Override
        public void setImage(String thumbnailPath) {
            try (InputStream inputStream = new FileInputStream(thumbnailPath)) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setIsFavourite(boolean b) {
            isFavourite = b;
            if (b){
                favouriteButton.setImageDrawable(favourite);
            } else {
                favouriteButton.setImageDrawable(notFavourite);
            }
        }

        @Override
        public boolean getIsFavourite() {
            return isFavourite;
        }
    }
}
