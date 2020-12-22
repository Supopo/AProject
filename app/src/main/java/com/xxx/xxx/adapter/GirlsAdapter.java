package com.xxx.xxx.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xxx.xxx.bean.GirlBean;
import com.xxx.xxx.databinding.ItemGirlsBinding;


import java.util.List;


public class GirlsAdapter extends BaseQuickAdapter<GirlBean, BaseViewHolder> implements LoadMoreModule {
    public GirlsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GirlBean item) {

        //注意 ItemBinding 改为自己item_layout的名字 ItemXxxBinding
        ItemGirlsBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setViewModel(item);
        binding.executePendingBindings();

        //随机图片高度
        int[] arr = {600, 750, 550, 650};
        //        int index = (int) (Math.random() * arr.length);
        int index = helper.getLayoutPosition() % 4;

        int height = arr[index];

        //设定了imageView的高度之后 加载时候就不会闪烁了
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.ivGirls.getLayoutParams();
        params.height = height;
        binding.ivGirls.setLayoutParams(params);




    }

    //局部刷新用的
    @Override
    protected void convert(BaseViewHolder helper, GirlBean item, List<?> payloads) {
        super.convert(helper, item, payloads);
        if (payloads.size() > 0 && payloads.get(0) instanceof Integer) {
            //不为空，即调用notifyItemChanged(position,payloads)后执行的，可以在这里获取payloads中的数据进行局部刷新
            int type = (Integer) payloads.get(0);// 刷新哪个部分 标志位

        }
    }
}
