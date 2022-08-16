package com.xxx.xxx.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.xxx.xxx.bean.ArticleBean;
import com.xxx.xxx.databinding.ItemArticleBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ArticleAdapter extends BaseQuickAdapter<ArticleBean, BaseDataBindingHolder<ItemArticleBinding>> implements LoadMoreModule {
    public ArticleAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemArticleBinding> holder, ArticleBean item) {
        //注意 ItemBinding 改为自己item_layout的名字 ItemXxxBinding
        ItemArticleBinding binding =holder.getDataBinding();
        assert binding != null;
        binding.setViewModel(item);
        binding.executePendingBindings();
    }

    //局部刷新用的
    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemArticleBinding> holder, ArticleBean item, List<?> payloads) {
        super.convert(holder, item, payloads);
        if (payloads.size() > 0 && payloads.get(0) instanceof Integer) {
            //不为空，即调用notifyItemChanged(position,payloads)后执行的，可以在这里获取payloads中的数据进行局部刷新
            int type = (Integer) payloads.get(0);// 刷新哪个部分 标志位
        }
    }

    @NonNull
    @Override
    public BaseLoadMoreModule addLoadMoreModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseLoadMoreModule(baseQuickAdapter);
    }
}
