package com.xxx.xxx.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxx.xxx.apiserver.UserRepository;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;

public class HomeViewModel extends BaseViewModel {
    //轮播图数据
    public MutableLiveData<List<BannerBean>> banners = new MutableLiveData<>();
    public MutableLiveData<List<NewsBean>> tags = new MutableLiveData<>();
    public MutableLiveData<String> image1 = new MutableLiveData<>();


    private UserRepository userRepository = UserRepository.getInstance();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }


    public void getDataList(int pageNum) {

        //        dataList.postValue(list);
        //        if (list.size() == 0) {
        //            //加载结束，没有更多数据了
        //            loadStatus.postValue(0);
        //        } else {
        //            //当前页数据加载完成
        //            loadStatus.postValue(1);
        //        }

    }

    public void getBanners() {
        userRepository.getBanners(this);
    }

    public void getTags() {
        List<NewsBean> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new NewsBean("# 标签" + i * 199));
        }
        tags.postValue(list);
    }

}
