package com.xxx.xxx.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xxx.xxx.utils.GetFilesUtils;

import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class TestViewModel extends BaseViewModel {


    public TestViewModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand toSet = new BindingCommand(()->{

    });

}
