package com.xxx.xxx.apiserver;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xxx.xxx.bean.Banner;
import com.xxx.xxx.http.RetrofitClient;
import com.xxx.xxx.viewModel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.http.BaseResponse;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * //                       _ooOoo_
 * //                      o8888888o
 * //                      88" . "88
 * //                      (| -_- |)
 * //                       O\ = /O
 * //                   ____/`---'\____
 * //                 .   ' \\| |// `.
 * //                  / \\||| : |||// \
 * //                / _||||| -:- |||||- \
 * //                  | | \\\ - /// | |
 * //                | \_| ''\---/'' | |
 * //                 \ .-\__ `-` ___/-. /
 * //              ______`. .' /--.--\ `. . __
 * //           ."" '< `.___\_<|>_/___.' >'"".
 * //          | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //            \ \ `-. \_ __\ /__ _/ .-` / /
 * //    ======`-.____`-.___\_____/___.-`____.-'======
 * //                       `=---='
 * //
 * //    .............................................
 * //             佛祖保佑             永无BUG
 * =====================================================
 * 作    者：Supo
 * 日    期：2020/7/9
 * 描    述: 网络请求仓库
 * =====================================================
 */
public class UserRepository {
    private static final UserRepository instance = new UserRepository();

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return instance;
    }

    private ApiServer userApi = RetrofitClient.getInstance().create(ApiServer.class);

    public List<Banner> getBanners(BaseViewModel viewModel) {
        final List<Banner> banners = new ArrayList<>();


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
                .subscribe(new DisposableObserver<BaseResponse<List<Banner>>>() {
                    @Override
                    public void onNext(BaseResponse<List<Banner>> response) {
                        banners.addAll(response.getData());
                        ((HomeViewModel)viewModel).banners.postValue(banners);
                        ((HomeViewModel)viewModel).bannersRes.postValue("新数量" + response.getData().size());
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

        return banners;
    }
}
