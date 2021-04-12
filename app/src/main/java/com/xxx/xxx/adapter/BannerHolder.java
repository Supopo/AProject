package com.xxx.xxx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.xxx.xxx.R;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.databinding.ItemBannerBinding;
import com.zhouwei.mzbanner.holder.MZViewHolder;

public class BannerHolder implements MZViewHolder<BannerBean> {
    private ItemBannerBinding bannerBinding;

    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null);
        bannerBinding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onBind(Context context, int position, BannerBean bannerBean) {
        bannerBinding.setViewModel(bannerBean);
        bannerBinding.executePendingBindings();
    }
}