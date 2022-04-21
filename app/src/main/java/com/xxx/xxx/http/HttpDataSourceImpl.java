package com.xxx.xxx.http;

import com.xxx.xxx.apiserver.ApiServer;
import com.xxx.xxx.bean.ArticleBean;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.bean.BaseListBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;

/**
 * 网络数据源
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private ApiServer apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(ApiServer apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(ApiServer apiService) {
        this.apiService = apiService;
    }


    @Override
    public Observable<Object> login() {
        return Observable.just(new Object()).delay(3, TimeUnit.SECONDS); //延迟3秒
    }

    @Override
    public Observable<BaseResponse<List<BannerBean>>> getBanners() {
        return apiService.getBanners();
    }

    @Override
    public Observable<BaseResponse<BaseListBean<List<ArticleBean>>>> getArticles(Integer page) {
        return apiService.getArticles(page);
    }
}
