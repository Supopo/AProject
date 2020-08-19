package com.xxx.xxx.viewModel;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;


import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.ToastUtils;

public class RegisterModel extends BaseViewModel {
    //绑定的手机号码
    public MutableLiveData<String> userPhone = new MutableLiveData<>();
    //绑定的手机验证码
    public MutableLiveData<String> code = new MutableLiveData<>();
    //绑定获取验证码
    public MutableLiveData<String> codeText = new MutableLiveData<>();
    //手机号清除按钮的显示隐藏绑定
    public MutableLiveData<Integer> clearBtnVisibility = new MutableLiveData<>();
    private long lastTime;


    public RegisterModel(@NonNull Application application) {
        super(application);
        codeText.setValue("获取验证码");
    }

    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChange = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (hasFocus) {
                clearBtnVisibility.setValue(View.VISIBLE);
            } else {
                clearBtnVisibility.setValue(View.INVISIBLE);
            }
        }
    });
    //清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearTel = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            userPhone.setValue("");
        }
    });
    //上一步
    public BindingCommand toLast = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //注册
    public BindingCommand toRegist = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("下一步");
        }
    });
    //获取验证码
    public BindingCommand getCode = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(userPhone.getValue())) {
                ToastUtils.showShort("请输手机号码");
                return;
            }
            if (lastTime > 0) {
                ToastUtils.showShort(lastTime + "后可重新获取");
                return;
            }
            Flowable.interval(1, 1, TimeUnit.SECONDS)
                    .take(60)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext((aLong) -> {
                        codeText.postValue("重新获取(" + (60 - aLong) + ")");
                        lastTime = 60 - aLong;
                    })
                    .doOnComplete(() -> {
                        lastTime = 0;
                        codeText.postValue("获取验证码");
                    })
                    .doOnError((throwable) ->
                            throwable.printStackTrace()
                    )
                    .subscribe();
        }
    });


}
