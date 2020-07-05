package com.xxx.xxx.viewModel;

import android.app.Application;
import android.text.TextUtils;


import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;


import com.xxx.mvvmlib.mvvmhabit.base.BaseViewModel;
import com.xxx.mvvmlib.mvvmhabit.binding.command.BindingAction;
import com.xxx.mvvmlib.mvvmhabit.binding.command.BindingCommand;
import com.xxx.mvvmlib.mvvmhabit.bus.event.SingleLiveEvent;
import com.xxx.mvvmlib.mvvmhabit.utils.RxUtils;
import com.xxx.mvvmlib.mvvmhabit.utils.ToastUtils;
import com.xxx.xxx.MainActivity;
import com.xxx.xxx.activity.RegisterActivity;
import com.xxx.xxx.apiserver.ApiServer;
import com.xxx.xxx.bean.LoginBean;
import com.xxx.xxx.http.RetrofitClient;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class LoginViewModel extends BaseViewModel {
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }


    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            login();
            startActivity(MainActivity.class);
            finish();

        }
    });

    //注册
    public BindingCommand toRegister = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("注册");
            startActivity(RegisterActivity.class);
        }
    });

    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand openEye = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchEvent.setValue(uc.pSwitchEvent.getValue() == null || !uc.pSwitchEvent.getValue());
        }
    });
    //忘记密码

    public BindingCommand forgetPass = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("忘记密码");
        }
    });


    /**
     * 网络模拟一个登陆操作
     **/
    public void login() {
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        //RaJava模拟登录
        addSubscribe(RetrofitClient.getInstance().create(ApiServer.class).getRecommendPoetry()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean o) throws Exception {
                        dismissDialog();
                        //保存账号密码
                        //跳到主页
                        startActivity(MainActivity.class);
                        //关闭页面
                        finish();
                    }
                }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
