package com.xxx.xxx.viewModel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxx.mvvmlib.mvvmhabit.base.BaseViewModel;
import com.xxx.mvvmlib.mvvmhabit.binding.command.BindingAction;
import com.xxx.mvvmlib.mvvmhabit.binding.command.BindingCommand;
import com.xxx.mvvmlib.mvvmhabit.bus.event.SingleLiveEvent;
import com.xxx.mvvmlib.mvvmhabit.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class RegisterModel  extends BaseViewModel {
    public MutableLiveData<String> userPhone = new MutableLiveData<>();
    public MutableLiveData<String>  code = new MutableLiveData<>();
    public MutableLiveData<String> codeText = new MutableLiveData<>();
    public UIChangeObservable uc  =new  UIChangeObservable();
    long lastTime = 0;
    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<String> pSwitchEvent = new SingleLiveEvent<>();
    }



     public RegisterModel(@NonNull Application application) {
        super(application);
         codeText.setValue("获取验证码");
    }
    //注册
    public BindingCommand regist = new BindingCommand(new BindingAction() {
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
            Disposable mDispos = Flowable.interval(1, 1, TimeUnit.SECONDS)
                    .take(60)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext((aLong) -> {
                       // uc.pSwitchEvent.setValue("重新获取(" + (60 - aLong) + ")");

                       codeText.postValue("重新获取(" + (60 - aLong) + ")");
                        lastTime = 60 - aLong;
                    })
                    .doOnComplete(() -> {
                        lastTime = 0;
                     //   uc.pSwitchEvent.setValue("获取验证码");
                        codeText.postValue("获取验证码");
                    })
                    .doOnError((throwable) ->
                            throwable.printStackTrace()
                    )
                    .subscribe();


        }
    });


}
