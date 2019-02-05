package com.example.maxim.imageviewer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.maxim.imageviewer.App;
import com.example.maxim.imageviewer.R;
import com.example.maxim.imageviewer.common.Directories;
import com.example.maxim.imageviewer.mvp.presenter.NetPhotoFragmentPresenter;
import com.example.maxim.imageviewer.mvp.view.NetPhotoFragmentView;
import com.example.maxim.imageviewer.ui.adapter.NetPhotoAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class NetPhotoFragment extends MvpAppCompatFragment
        implements NetPhotoFragmentView {

    private static String ARGS_KEY = "args";

    @InjectPresenter
    NetPhotoFragmentPresenter presenter;

    @BindView(R.id.photo_fragment_view)
    View view;
    @BindView(R.id.photo_fragment_recycler_view)
    RecyclerView recyclerView;

    NetPhotoAdapter adapter;

    public static NetPhotoFragment getInstance(String args){
        NetPhotoFragment fragment = new NetPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_KEY, args);
        fragment.setArguments(bundle);
        return fragment;
    }

    @ProvidePresenter
    public NetPhotoFragmentPresenter providePhotoFragmentPresenter() {
        NetPhotoFragmentPresenter presenter = new NetPhotoFragmentPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_fragment, null);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gm = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gm);

        adapter = new NetPhotoAdapter(presenter.getPhotoListPresenter());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void updateList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
