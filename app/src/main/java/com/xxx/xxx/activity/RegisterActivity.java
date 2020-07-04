package com.xxx.xxx.activity;

import android.os.Bundle;

import com.xxx.mvvmlib.mvvmhabit.base.BaseActivity;
import com.xxx.mvvmlib.mvvmhabit.utils.ToastUtils;
import com.xxx.xxx.BR;
import com.xxx.xxx.R;
import com.xxx.xxx.databinding.ActivityRegisterBinding;
import com.xxx.xxx.viewModel.RegisterModel;

public class RegisterActivity  extends BaseActivity<ActivityRegisterBinding, RegisterModel> {
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
        ToastUtils.showShort("ddd");
        rlLeft.setOnClickListener(view -> {
            startActivity(LoginActivity.class);

        });

        viewModel.uc.pSwitchEvent.observe(this,v->{
            viewModel.codeText.setValue(v);
        });

    }
}
