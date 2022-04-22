package com.xxx.xxx.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxx.xxx.apiserver.ApiServer;
import com.xxx.xxx.apiserver.CustomObserver;
import com.xxx.xxx.apiserver.DemoRepository;
import com.xxx.xxx.apiserver.UserRepository;
import com.xxx.xxx.bean.ArticleBean;
import com.xxx.xxx.bean.BaseListBean;
import com.xxx.xxx.http.RetrofitClient;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.utils.RxUtils;

public class ArticlesViewModel extends BaseViewModel<DemoRepository> {
    public ArticlesViewModel(@NonNull Application application) {
        super(application);
    }

    //网络请求仓库
    private UserRepository userRepository = UserRepository.getInstance();
    public MutableLiveData<Boolean> disDialog = new MutableLiveData<>();
    public MutableLiveData<BaseListBean<List<ArticleBean>>> dataList = new MutableLiveData<>();

    public void getDataList(int page) {
        //        userRepository.getArticles(disDialog, dataList, page, 10);
        RetrofitClient.execute(model.getArticles(page), new CustomObserver<BaseResponse<BaseListBean<List<ArticleBean>>>>() {

            @Override
            protected void dismissDialog() {
                disDialog.postValue(true);
            }

            @Override
            public void onSuccess(BaseResponse<BaseListBean<List<ArticleBean>>> data) {
                dataList.postValue(data.getData());
            }

            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                addSubscribe(d);
            }
        });
    }

    public void getDataList2(int page) {
        addSubscribe(
                model.getArticles(page)
                        .compose(RxUtils.bindToLifecycle(getLifecycleProvider())) // 请求与View周期同步
                        .compose(RxUtils.schedulersTransformer())  // 线程调度
                        .compose(RxUtils.exceptionTransformer())   // 网络错误的异常转换
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                //请求前的执行 主线程中
                            }
                        })
                        .subscribe(new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {

                            }
                        }));
    }

    public void getDataList3(int page) {
        model.getArticles(page)
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider())) // 请求与View周期同步
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
}
