package com.xxx.xxx.apiserver;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.xxx.xxx.MainActivity;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.bean.GirlBean;
import com.xxx.xxx.http.RetrofitClient;
import com.xxx.xxx.viewModel.GirlsViewModel;
import com.xxx.xxx.viewModel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
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

    public void getBanners(HomeViewModel viewModel) {
        userApi.getBanners()
                .compose(RxUtils.bindToLifecycle(viewModel.getLifecycleProvider())) // 请求与View周期同步
                .compose(RxUtils.schedulersTransformer())  // 线程调度
                .compose(RxUtils.exceptionTransformer())   // 网络错误的异常转换
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        viewModel.showDialog("正在请求");
                    }
                })
                .subscribe(new DisposableObserver<BaseResponse<List<BannerBean>>>() {
                    @Override
                    public void onNext(BaseResponse<List<BannerBean>> response) {
                        viewModel.banners.postValue(response.getData());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        viewModel.dismissDialog();
                    }

                    @Override
                    public void onComplete() {
                        //关闭对话框
                        viewModel.dismissDialog();
                    }
                });
    }


    public void getGirls(GirlsViewModel viewModel, int page, int count) {
        viewModel.showDialog();
        RetrofitClient.execute(userApi.getGirls(page, count), new CustomObserver<BaseResponse<List<GirlBean>>>() {

            @Override
            protected void dismissDialog() {
                viewModel.dismissDialog();
            }

            @Override
            public void onSuccess(BaseResponse<List<GirlBean>> data) {
                viewModel.dataList.postValue(data.getData());
                if (data.getData().size() > 0) {
                    viewModel.loadStatus.postValue(1);
                } else {
                    viewModel.loadStatus.postValue(0);
                }
            }

        });

        //        userApi.getGirls(page, count)
        //                .compose(RxUtils.bindToLifecycle(viewModel.getLifecycleProvider())) // 请求与View周期同步
        //                .compose(RxUtils.schedulersTransformer())  // 线程调度
        //                .compose(RxUtils.exceptionTransformer())   // 网络错误的异常转换
        //                .doOnSubscribe(new Consumer<Disposable>() {
        //                    @Override
        //                    public void accept(Disposable disposable) throws Exception {
        //                        viewModel.showDialog("正在捕获妹子");
        //                    }
        //                })
        //                .subscribe(new CustomObserver<BaseResponse<List<GirlBean>>>() {
        //
        //                    @Override
        //                    protected void dismissDialog() {
        //                        viewModel.dismissDialog();
        //                    }
        //
        //                    @Override
        //                    public void onSuccess(BaseResponse<List<GirlBean>> data) {
        //                        viewModel.dataList.postValue(data.getData());
        //                    }
        //
        //                });
    }

    public MutableLiveData<List<GirlBean>> getGirls(int page) {
        MutableLiveData<List<GirlBean>> girlsList = new MutableLiveData<>();
        Log.e("--", "1");
        userApi.getGirls(page, 10)
                .compose(RxUtils.schedulersTransformer())  // 线程调度
                .compose(RxUtils.exceptionTransformer())   // 网络错误的异常转换
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribe(new DisposableObserver<BaseResponse<List<GirlBean>>>() {
                    @Override
                    public void onNext(BaseResponse<List<GirlBean>> response) {
                        if (response.getData() != null) {
                            girlsList.setValue(response.getData());
                            Log.e("--", "2");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Log.e("--", "3");
        return girlsList;
    }


}
