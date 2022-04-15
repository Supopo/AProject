package com.xxx.xxx.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxx.xxx.apiserver.UserRepository;
import com.xxx.xxx.bean.ArticleBean;
import com.xxx.xxx.bean.BaseListBean;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;

public class ArticlesViewModel extends BaseViewModel {
    public ArticlesViewModel(@NonNull Application application) {
        super(application);
    }

    //网络请求仓库
    private UserRepository userRepository = UserRepository.getInstance();
    public MutableLiveData<Boolean> disDialog = new MutableLiveData<>();
    public MutableLiveData<BaseListBean<List<ArticleBean>>> dataList = new MutableLiveData<>();

    public void getDataList(int page) {
        //请参考getBanners
        userRepository.getArticles(disDialog, dataList, page, 10);
    }

}
