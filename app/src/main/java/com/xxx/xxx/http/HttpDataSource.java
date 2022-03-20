package com.xxx.xxx.http;

import com.xxx.xxx.bean.BannerBean;

import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * Created by Suuu on 2022/3/20.
 * 网络数据获取封装
 */
public interface HttpDataSource {
    //模拟登录
    Observable<Object> login();

    Observable<BaseResponse<List<BannerBean>>> getBanners();

}
