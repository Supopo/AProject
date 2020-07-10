package com.xxx.xxx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.xxx.xxx.R;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.databinding.ItemBannerBinding;
import com.zhouwei.mzbanner.holder.MZViewHolder;

/**
 * //                       _ooOoo_
 * //                      o8888888o
 * //                      88" . "88
 * //                      (| -_- |)
 * //                       O\ = /O
 * //                   ____/`---'\____
 * //                 .   ' \\| |// `.
 * //                  / \\||| : |||// \
 * //                / _||||| -:- |||||- \
 * //                  | | \\\ - /// | |
 * //                | \_| ''\---/'' | |
 * //                 \ .-\__ `-` ___/-. /
 * //              ______`. .' /--.--\ `. . __
 * //           ."" '< `.___\_<|>_/___.' >'"".
 * //          | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //            \ \ `-. \_ __\ /__ _/ .-` / /
 * //    ======`-.____`-.___\_____/___.-`____.-'======
 * //                       `=---='
 * //
 * //    .............................................
 * //             佛祖保佑             永无BUG
 * =====================================================
 * 作    者：Supo
 * 日    期：2020/7/10
 * 描    述: 轮播图Holder
 * =====================================================
 */
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