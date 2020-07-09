package com.xxx.xxx.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxx.xxx.apiserver.ApiServer;
import com.xxx.xxx.apiserver.UserRepository;
import com.xxx.xxx.bean.Banner;
import com.xxx.xxx.http.RetrofitClient;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.utils.RxUtils;

public class HomeViewModel extends BaseViewModel {
    //轮播图数据
    public MutableLiveData<List<Banner>> banners = new MutableLiveData<>();
    public MutableLiveData<String> bannersRes = new MutableLiveData<>();
    private UserRepository userRepository = UserRepository.getInstance();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }


    public void getDataList(int pageNum) {

        //        dataList.postValue(list);
        //        if (list.size() == 0) {
        //            //加载结束，没有更多数据了
        //            loadStatus.postValue(0);
        //        } else {
        //            //当前页数据加载完成
        //            loadStatus.postValue(1);
        //        }

    }

    public BindingCommand getBannersCommand = new BindingCommand(() -> {
        addSubscribe(
                RetrofitClient.getInstance().create(ApiServer.class)
                        .getBanners()
                        .compose(RxUtils.schedulersTransformer()) //线程调度
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showDialog();
                            }
                        })
                        .subscribe(new Consumer<List<Banner>>() {
                            @Override
                            public void accept(List<Banner> banners) throws Exception {

                            }
                        })
        );
        //简单粗暴的请求方式
        RetrofitClient.getInstance().create(ApiServer.class)
                .getBanners()
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider())) // 请求与View周期同步
                .compose(RxUtils.schedulersTransformer())  // 线程调度
                .compose(RxUtils.exceptionTransformer())   // 网络错误的异常转换
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog("正在请求");
                    }
                })
                .subscribe(new DisposableObserver<BaseResponse<List<Banner>>>() {
                    @Override
                    public void onNext(BaseResponse<List<Banner>> response) {
                        banners.postValue(response.getData());
                        bannersRes.postValue("数量" + response.getData().size());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                    }

                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    });
    public BindingCommand getBannersCommand2 = new BindingCommand(() -> {
        userRepository.getBanners(this);
    });


}
