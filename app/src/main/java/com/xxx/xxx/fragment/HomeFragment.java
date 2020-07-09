package com.xxx.xxx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.xxx.xxx.R;
import com.xxx.xxx.BR;
import com.xxx.xxx.databinding.FragmentHomeBinding;
import com.xxx.xxx.viewModel.HomeViewModel;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ToastUtils;

//注意ActivityBaseBinding换成自己fragment_layout对应的名字 FragmentXxxBinding
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    //页面数据初始化方法
    @Override
    public void initData() {

    }


    //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
    @Override
    public void initViewObservable() {
        viewModel.banners.observe(getActivity(), banners -> {
            if (banners != null) {
                ToastUtils.showLong("返回轮播图" + banners.size() + "张");
            }
        });
    }

}
