package com.xxx.xxx.apiserver;

import com.xxx.xxx.bean.ArticleBean;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.bean.BaseListBean;
import com.xxx.xxx.http.HttpDataSource;
import com.xxx.xxx.http.RetrofitClient;

import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * 网络请求仓库
 */
public class UserRepository implements HttpDataSource {
    private static final UserRepository instance = new UserRepository();

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return instance;
    }

    private final ApiServer userApi = RetrofitClient.getInstance().create(ApiServer.class);

    @Override
    public Observable<Object> login() {
        return null;
    }

    @Override
    public Observable<BaseResponse<List<BannerBean>>> getBanners() {
        return null;
    }

    @Override
    public Observable<BaseResponse<BaseListBean<List<ArticleBean>>>> getArticles(int page) {
        return userApi.getArticles(page);
    }

}
