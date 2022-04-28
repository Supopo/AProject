package com.xxx.xxx.http;

import com.xxx.xxx.bean.ArticleBean;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.bean.BaseListBean;

import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.Path;

/**
 * Created by Suuu on 2022/3/20.
 * 网络数据获取封装
 */
public interface HttpDataSource {
    //模拟登录
    Observable<Object> login();

    Observable<BaseResponse<List<BannerBean>>> getBanners();

    Observable<BaseResponse<BaseListBean<List<ArticleBean>>>> getArticles(int page);

}
