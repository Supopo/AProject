package com.xxx.xxx.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import com.xxx.xxx.BR;
import com.xxx.xxx.MainActivity;
import com.xxx.xxx.R;
import com.xxx.xxx.app.Constant;
import com.xxx.xxx.databinding.ActivitySplashBinding;
import com.xxx.xxx.viewModel.SplashViewModel;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.SPUtils;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> {

    private Handler handler;
    private Runnable runnable;
    private Timer timer = new Timer();
    private int recLen = 3;//跳过倒计时提示3秒
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen--;
                    binding.tvJump.setText("跳过 " + recLen);
                    if (recLen < 0) {
                        timer.cancel();
                        binding.tvJump.setVisibility(View.GONE);//倒计时到0隐藏字体
                    }
                }
            });
        }
    };
    private boolean isLogin;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_splash;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        super.initParam();

        //这段代码很重要，如果安装之后直接打开应用而非桌面打开，任务栈里面会重复存在A-B-C-A-C，在C页面关闭时候会重新弹出A
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }


    }

    //页面数据初始化方法
    @Override
    public void initData() {

        //设置全屏，隐藏状态栏
        //如果有刘海先要在手机的应用设置显示刘海区域内容
        //如果有的手机开启全屏之后顶部有彩色条，那是因为手机的全屏设置没有设置该app
        setFullScreen();
        //状态栏透明的全屏效果
        setStatusBarTransparent();


        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(params);
        }

        isLogin = SPUtils.getInstance().getBoolean(Constant.IS_LOGIN);

        rlTitle.setVisibility(View.GONE);
        binding.tvJump.setText("跳过 " + recLen);
        timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                toNext();
            }
        }, recLen * 1000);
    }

    private void toNext() {
        startActivity(isLogin ? MainActivity.class : LoginActivity.class);
        finish();
    }

    //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
    @Override
    public void initViewObservable() {
        binding.tvJump.setOnClickListener(listener -> {
            toNext();
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
        });
    }
}
