package com.xxx.xxx.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxx.xxx.apiserver.CustomObserver;
import com.xxx.xxx.apiserver.DemoRepository;
import com.xxx.xxx.apiserver.UserRepository;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

public class HomeViewModel extends BaseViewModel<DemoRepository> {
    //轮播图数据
    public MutableLiveData<Boolean> disDialog = new MutableLiveData<>();
    public MutableLiveData<List<BannerBean>> banners = new MutableLiveData<>();
    public MutableLiveData<List<NewsBean>> tags = new MutableLiveData<>();
    public String image1 = "https://www.wanandroid.com/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png";


    private UserRepository userRepository = UserRepository.getInstance();

    //重载构造函数
    public HomeViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }

    public void getBanners() {
        //可以调用addSubscribe()添加Disposable，请求与View周期同步
        model.getBanners()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(this)//请求与ViewModel周期同步
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog("正在请求...");
                    }
                })
                .subscribe(new CustomObserver<BaseResponse<List<BannerBean>>>() {

                    @Override
                    protected void dismissDialog() {
                        disDialog.postValue(true);
                    }

                    @Override
                    public void onSuccess(BaseResponse<List<BannerBean>> data) {
                        banners.postValue(data.getData());
                    }
                });

    }

    public void getTags() {
        List<NewsBean> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new NewsBean("# 标签" + i * 199));
        }
        tags.postValue(list);
    }

}
