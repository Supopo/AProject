package com.xxx.xxx.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.xxx.xxx.MainActivity;
import com.xxx.xxx.apiserver.UserRepository;
import com.xxx.xxx.bean.GirlBean;
import com.xxx.xxx.http.RetrofitClient;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;

public class GirlsViewModel extends BaseViewModel {
    public GirlsViewModel(@NonNull Application application) {
        super(application);
    }

    private UserRepository userRepository = UserRepository.getInstance();
    //加载更多的状态
    public MutableLiveData<Integer> loadStatus = new MutableLiveData<>();
    public MutableLiveData<List<GirlBean>> dataList = new MutableLiveData<>();

    //----------------------------------------------------------
    private MutableLiveData<Integer> girlsPage = new MutableLiveData<>();
    //这种写法单向监听数据仓库数据变化，viewModel被销毁后不会对其产生影响
    public LiveData<List<GirlBean>> girlsList = Transformations.switchMap(girlsPage, page ->
            userRepository.getGirls(page));
    //-----------------------------------------------------------------


    //---------------------------写法二-------------------------------
    public void getDataList(int page) {
        userRepository.getGirls(this, page, 10);
        // ----------写法一------
        //        girlsPage.setValue(page);
        // ---------------------

    }

}
