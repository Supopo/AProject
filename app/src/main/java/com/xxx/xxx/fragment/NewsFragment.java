package com.xxx.xxx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.xxx.xxx.R;
import com.xxx.xxx.adapter.NewsAdapter;
import com.xxx.xxx.databinding.FragmentNewsBinding;
import com.xxx.xxx.viewModel.NewsViewModel;

import me.goldze.mvvmhabit.base.BaseFragment;

//注意ActivityBaseBinding换成自己fragment_layout对应的名字 FragmentXxxBinding
public class NewsFragment extends BaseFragment<FragmentNewsBinding, NewsViewModel> {

    private NewsAdapter mAdapter;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_news;
    }

    @Override
    public int initVariableId() {
        return com.xxx.xxx.BR.viewModel;
    }

    //页面数据初始化方法
    @Override
    public void initData() {
        initAdapter();
        viewModel.getDataList();
    }


    //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
    @Override
    public void initViewObservable() {
        //下拉刷新
        binding.srlContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getDataList();
                //刷新完立即关闭刷新效果是因为本身请求就带有Dialog
                //如果此处不关闭的话应该在网络请求结束后关闭
                binding.srlContent.setRefreshing(false);
            }
        });

        //往adapter里面加载数据
        viewModel.dataList.observe(this, dataList -> {
            if (dataList != null) {
                mAdapter.setList(dataList);
                if (dataList.size() == 0) {
                    //创建适配器.空布局，没有数据时候默认展示的
                    mAdapter.setEmptyView(R.layout.list_empty);
                }
            }
        });


    }

    public void initAdapter() {
        mAdapter = new NewsAdapter(R.layout.item_news);

        binding.rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvContent.setAdapter(mAdapter);

        //Item点击事件
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

        });


        //	子View点击事件
        mAdapter.addChildClickViewIds(R.id.ll_item);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {

        });

    }
}
