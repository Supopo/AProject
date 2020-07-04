package com.xxx.xxx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.xxx.mvvmlib.mvvmhabit.base.BaseFragment;
import com.xxx.xxx.BR;
import com.xxx.xxx.R;



public class BFragment extends BaseFragment {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_b;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
