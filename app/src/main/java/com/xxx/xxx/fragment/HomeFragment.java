package com.xxx.xxx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.xxx.xxx.BR;
import com.xxx.xxx.R;
import com.xxx.xxx.activity.WebActivity;
import com.xxx.xxx.adapter.BannerHolder;
import com.xxx.xxx.app.Constant;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.databinding.FragmentHomeBinding;
import com.xxx.xxx.viewModel.HomeViewModel;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseFragment;

//注意ActivityBaseBinding换成自己fragment_layout对应的名字 FragmentXxxBinding
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {


    private MZHolderCreator<BannerHolder> mzHolderCreator;
    private List<BannerBean> banners;

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
        //Banner创造器
        mzHolderCreator = new MZHolderCreator<BannerHolder>() {
            @Override
            public BannerHolder createViewHolder() {
                return new BannerHolder();
            }
        };
        //请求Banner
        viewModel.getBanners();
    }


    //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
    @Override
    public void initViewObservable() {
        //轮播图数据回调
        viewModel.banners.observe(getActivity(), banners -> {
            if (banners != null) {
                this.banners = banners;
                //设置数据
                binding.banner.setPages(banners, mzHolderCreator);
                //开始轮播
                binding.banner.start();
            }
        });
        binding.banner.setBannerPageClickListener(((view, i) -> {
            Intent intent = new Intent();
            intent.putExtra(Constant.PageTitle, banners.get(i).getTitle());
            intent.putExtra(Constant.PageUrl, banners.get(i).getUrl());
            intent.setClass(getActivity(), WebActivity.class);
            startActivity(intent);
        }));
    }


    @Override
    public void onPause() {
        super.onPause();
        binding.banner.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.banner.start();//开始轮播
    }

}
