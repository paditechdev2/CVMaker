package com.paditech.cvmarker.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import butterknife.ButterKnife;

/**
 * Created by USER on 13/6/2016.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this, getView());
        initView();
    }

    protected abstract void initView();
}
