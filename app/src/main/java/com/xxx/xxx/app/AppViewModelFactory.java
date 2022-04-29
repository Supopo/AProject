package com.xxx.xxx.app;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.xxx.xxx.apiserver.DemoRepository;
import com.xxx.xxx.viewModel.ArticlesViewModel;
import com.xxx.xxx.viewModel.HomeViewModel;
import com.xxx.xxx.viewModel.LoginViewModel;


/**
 * 利用工厂模式创建ViewModel
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final DemoRepository mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized ( AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideDemoRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppViewModelFactory(Application application, DemoRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
       if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
           return (T) new HomeViewModel(mApplication, mRepository);
       }else if (modelClass.isAssignableFrom(ArticlesViewModel.class)) {
           return (T) new ArticlesViewModel(mApplication, mRepository);
       }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
