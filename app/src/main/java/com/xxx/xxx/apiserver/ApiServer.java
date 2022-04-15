package com.xxx.xxx.apiserver;


import com.xxx.xxx.bean.ArticleBean;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.bean.BaseListBean;

import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ApiServer {
    @GET("banner/json")
    Observable<BaseResponse<List<BannerBean>>> getBanners();

    @GET("article/list/{page}/json")
    Observable<BaseResponse<BaseListBean<List<ArticleBean>>>> getArticles(@Path("page") Integer page);
}
