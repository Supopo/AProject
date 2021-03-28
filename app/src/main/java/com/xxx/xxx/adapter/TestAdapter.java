package com.xxx.xxx.adapter;

import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xxx.xxx.bean.TestBean;
import com.xxx.xxx.databinding.ItemTestBinding;


public class TestAdapter extends BaseQuickAdapter<TestBean, BaseViewHolder> implements LoadMoreModule {
    public TestAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestBean item) {

        //注意 ItemBinding 改为自己item_layout的名字 ItemXxxBinding
        ItemTestBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setViewModel(item);
        binding.executePendingBindings();
    }

}
