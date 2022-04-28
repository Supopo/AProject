package com.xxx.xxx.apiserver;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xxx.xxx.bean.ArticleBean;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.bean.BaseListBean;
import com.xxx.xxx.bean.GirlBean;
import com.xxx.xxx.http.RetrofitClient;
import com.xxx.xxx.viewModel.ArticlesViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.utils.RxUtils;

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

    public void getArticles2(MutableLiveData<Boolean> disDialog, MutableLiveData<BaseListBean<List<ArticleBean>>> dataList, int page, int count) {
        userApi.getArticles(page)
                //                        .compose(RxUtils.bindToLifecycle(viewModel.getLifecycleProvider())) // 请求与View周期同步
                .compose(RxUtils.schedulersTransformer())  // 线程调度
                .compose(RxUtils.exceptionTransformer())   // 网络错误的异常转换
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //请求前的执行 主线程中
                    }
                })
                .subscribe(new CustomObserver<BaseResponse<List<ArticleBean>>>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        disposable = d;
                    }

                    @Override
                    protected void dismissDialog() {

                    }

                    @Override
                    public void onSuccess(BaseResponse<List<ArticleBean>> data) {
                    }

                });
    }

    public void getArticles(MutableLiveData<Boolean> disDialog, MutableLiveData<BaseListBean<List<ArticleBean>>> dataList, int page, int count) {
        RetrofitClient.execute(userApi.getArticles(page), new CustomObserver<BaseResponse<BaseListBean<List<ArticleBean>>>>() {

            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                disposable = d;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                disposable.dispose();
            }

            @Override
            protected void dismissDialog() {
                disDialog.postValue(true);
                disposable.dispose();
            }

            @Override
            public void onSuccess(BaseResponse<BaseListBean<List<ArticleBean>>> data) {
                dataList.postValue(data.getData());
            }

        });

    }
}
