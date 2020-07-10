package com.xxx.xxx.apiserver;


import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.bean.GirlBean;

import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ApiServer {
    @GET("banners")
    Observable<BaseResponse<List<BannerBean>>> getBanners();

    @GET("data/category/Girl/type/Girl/page/{page}/count/{count}")
    Observable<BaseResponse<List<GirlBean>>> getGirls(@Path("page") Integer page, @Path("count") Integer count);
}
