package com.xxx.xxx.activity;

import android.os.Bundle;
import android.view.View;

import com.xxx.xxx.BR;
import com.xxx.xxx.R;
import com.xxx.xxx.databinding.ActivityRegisterBinding;
import com.xxx.xxx.viewModel.RegisterModel;

import me.goldze.mvvmhabit.base.BaseActivity;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, RegisterModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_register;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
    }

    @Override
    public void initData() {
        super.initData();
        rlTitle.setVisibility(View.GONE);
    }
}
