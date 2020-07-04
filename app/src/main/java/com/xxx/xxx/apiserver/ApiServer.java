package com.xxx.xxx.apiserver;



import com.xxx.xxx.bean.LoginBean;

import io.reactivex.Observable;
import retrofit2.http.GET;


public interface ApiServer {
    @GET("user/1")
    Observable<LoginBean> getRecommendPoetry();
}
