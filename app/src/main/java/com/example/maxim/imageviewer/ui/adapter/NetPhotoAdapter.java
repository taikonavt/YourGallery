package com.example.maxim.imageviewer.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.maxim.imageviewer.App;
import com.example.maxim.imageviewer.R;
import com.example.maxim.imageviewer.mvp.model.image.ImageLoader;
import com.example.maxim.imageviewer.mvp.presenter.list.INetPhotoListPresenter;
import com.example.maxim.imageviewer.mvp.view.item.NetPhotoItemView;
import com.jakewharton.rxbinding2.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetPhotoAdapter extends RecyclerView.Adapter<NetPhotoAdapter.MyViewHolderNet> {

    private INetPhotoListPresenter presenter;

    @Inject
    ImageLoader imageLoader;

    public NetPhotoAdapter(INetPhotoListPresenter presenter){
        this.presenter = presenter;
        App.getInstance().getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public MyViewHolderNet onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.net_photo_item, viewGroup, false);
        return new MyViewHolderNet(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderNet myViewHolder, int position) {
        RxView.clicks(myViewHolder.itemView)
                .map(obj -> myViewHolder)
                .subscribe(presenter.getClickSubject());
        RxView.clicks(myViewHolder.downloadButton)
                .map(obj -> myViewHolder)
                .subscribe(presenter.getDownloadClickSubject());
        myViewHolder.pos = position;
        presenter.bindView(myViewHolder);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

    class MyViewHolderNet extends RecyclerView.ViewHolder implements NetPhotoItemView {

        private int pos;

        @BindView(R.id.net_item_image)
        ImageView imageView;
        @BindView(R.id.net_item_download_btn)
        ImageButton downloadButton;

        MyViewHolderNet(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public int getPos() {
            return pos;
        }

        @Override
        public void setImage(String url) {
            imageLoader.loadInto(url, imageView);
        }
    }
}
