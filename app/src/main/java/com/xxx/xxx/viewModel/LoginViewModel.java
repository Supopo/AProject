package com.xxx.xxx.viewModel;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;

import com.xxx.xxx.MainActivity;
import com.xxx.xxx.activity.RegisterActivity;
import com.xxx.xxx.apiserver.DemoRepository;
import com.xxx.xxx.app.Constant;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;


public class LoginViewModel extends BaseViewModel<DemoRepository> {
    //用户名的绑定
    public MutableLiveData<String> userName = new MutableLiveData<>();
    //密码的绑定
    public MutableLiveData<String> password = new MutableLiveData<>();

    //用户名清除按钮的显示隐藏绑定
    public MutableLiveData<Integer> clearBtnVisibility = new MutableLiveData<>();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public LoginViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层
        userName.setValue(model.getUserName());
        password.setValue(model.getPassword());
    }

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }


    //清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            userName.setValue("");
        }
    });
    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchEvent.setValue(uc.pSwitchEvent.getValue() == null || !uc.pSwitchEvent.getValue());
        }
    });
    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (hasFocus) {
                clearBtnVisibility.setValue(View.VISIBLE);
            } else {
                clearBtnVisibility.setValue(View.INVISIBLE);
            }
        }
    });
    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login();
        }
    });
    //注册按钮的点击事件
    public BindingCommand registerOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(RegisterActivity.class);
        }
    });

    /**
     * 网络模拟一个登陆操作
     **/
    private void login() {
        if (TextUtils.isEmpty(userName.getValue())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.getValue())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        //RaJava模拟登录
        addSubscribe(model.login()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismissDialog();
                        SPUtils.getInstance().put(Constant.IS_LOGIN, true);
                        //保存账号密码
                        model.saveUserName(userName.getValue());
                        model.savePassword(password.getValue());
                        //进入DemoActivity页面
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
