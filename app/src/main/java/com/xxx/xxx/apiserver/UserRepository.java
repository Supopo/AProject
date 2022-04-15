package com.xxx.xxx.apiserver;

import androidx.lifecycle.MutableLiveData;

import com.xxx.xxx.bean.ArticleBean;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.bean.BaseListBean;
import com.xxx.xxx.http.RetrofitClient;

import java.util.List;

import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * 网络请求仓库
 */
public class UserRepository {
    private static final UserRepository instance = new UserRepository();

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return instance;
    }

    private ApiServer userApi = RetrofitClient.getInstance().create(ApiServer.class);

    public void getBanners(MutableLiveData<Boolean> disDialog, MutableLiveData<List<BannerBean>> dataList) {
        RetrofitClient.execute(userApi.getBanners(), new CustomObserver<BaseResponse<List<BannerBean>>>() {

            @Override
            protected void dismissDialog() {
                disDialog.postValue(true);
            }

            @Override
            public void onSuccess(BaseResponse<List<BannerBean>> data) {
                dataList.postValue(data.getData());
            }

        });
    }


    public void getArticles(MutableLiveData<Boolean> disDialog, MutableLiveData<BaseListBean<List<ArticleBean>>> dataList, int page, int count) {
        RetrofitClient.execute(userApi.getArticles(page), new CustomObserver<BaseResponse<BaseListBean<List<ArticleBean>>>>() {

            @Override
            protected void dismissDialog() {
                disDialog.postValue(true);
            }

            @Override
            public void onSuccess(BaseResponse<BaseListBean<List<ArticleBean>>> data) {
                dataList.postValue(data.getData());
            }

        });

    }

}
