package com.xxx.xxx.apiserver;


import com.xxx.xxx.bean.Banner;
import com.xxx.xxx.bean.LoginBean;

import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.GET;


public interface ApiServer {
    @GET("banners")
    Observable<BaseResponse<List<Banner>>> getBanners();
}
