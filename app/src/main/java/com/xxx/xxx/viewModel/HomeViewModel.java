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
    public MutableLiveData<Boolean> disDialog = new MutableLiveData<>();
    public MutableLiveData<List<BannerBean>> banners = new MutableLiveData<>();
    public MutableLiveData<List<NewsBean>> tags = new MutableLiveData<>();
    public String image1 = "http://gank.io/images/aebca647b3054757afd0e54d83e0628e";


    private UserRepository userRepository = UserRepository.getInstance();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void getBanners() {
        userRepository.getBanners(disDialog, banners);
    }

    public void getTags() {
        List<NewsBean> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new NewsBean("# 标签" + i * 199));
        }
        tags.postValue(list);
    }

}
