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
import com.example.maxim.imageviewer.mvp.presenter.DbPhotoFragmentPresenter;
import com.example.maxim.imageviewer.mvp.view.DbPhotoFragmentView;
import com.example.maxim.imageviewer.ui.adapter.DbPhotoAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class DbPhotoFragment extends MvpAppCompatFragment
            implements DbPhotoFragmentView {

    private static String ARGS_KEY = "args";
    public static final boolean ALL = false;
    public static final boolean FAV = true;

    @InjectPresenter
    DbPhotoFragmentPresenter presenter;

    @BindView(R.id.photo_fragment_view)
    View view;
    @BindView(R.id.photo_fragment_recycler_view)
    RecyclerView recyclerView;

    DbPhotoAdapter adapter;

    public static DbPhotoFragment getInstance(boolean onlyFavourites){
        DbPhotoFragment fragment = new DbPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_KEY, onlyFavourites);
        fragment.setArguments(bundle);
        return fragment;
    }

    @ProvidePresenter
    public DbPhotoFragmentPresenter providePhotoFragmentPresenter() {
        DbPhotoFragmentPresenter presenter = new DbPhotoFragmentPresenter(
                AndroidSchedulers.mainThread(), getArguments().getBoolean(ARGS_KEY));
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

        adapter = new DbPhotoAdapter(presenter.getPhotoListPresenter());
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
