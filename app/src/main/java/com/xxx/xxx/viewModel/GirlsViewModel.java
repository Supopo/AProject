package com.xxx.xxx.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.xxx.xxx.apiserver.UserRepository;
import com.xxx.xxx.bean.GirlBean;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;

public class GirlsViewModel extends BaseViewModel {
    public GirlsViewModel(@NonNull Application application) {
        super(application);
    }

    //网络请求仓库
    private UserRepository userRepository = UserRepository.getInstance();
    public MutableLiveData<Boolean> disDialog = new MutableLiveData<>();
    public MutableLiveData<List<GirlBean>> dataList = new MutableLiveData<>();

    public void getDataList(int page) {
        userRepository.getGirls(disDialog,dataList, page, 10);
    }

}
