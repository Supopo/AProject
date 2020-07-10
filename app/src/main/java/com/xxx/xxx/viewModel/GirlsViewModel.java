package com.xxx.xxx.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxx.xxx.apiserver.UserRepository;
import com.xxx.xxx.bean.GirlBean;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;

public class GirlsViewModel extends BaseViewModel {
    public MutableLiveData<List<GirlBean>> dataList = new MutableLiveData<>();
    //加载更多的状态
    public MutableLiveData<Integer> loadStatus = new MutableLiveData<>();
    private UserRepository userRepository = UserRepository.getInstance();


    public GirlsViewModel(@NonNull Application application) {
        super(application);
    }

    public void getDataList(int page) {
        userRepository.getGirls(this, page, 10);
    }

}
