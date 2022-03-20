package com.xxx.xxx.app;


import com.xxx.xxx.apiserver.ApiServer;
import com.xxx.xxx.apiserver.DemoRepository;
import com.xxx.xxx.http.HttpDataSource;
import com.xxx.xxx.http.HttpDataSourceImpl;
import com.xxx.xxx.http.LocalDataSource;
import com.xxx.xxx.http.LocalDataSourceImpl;
import com.xxx.xxx.http.RetrofitClient;

/**
 * 注入全局的数据仓库，可以考虑使用Dagger2。（根据项目实际情况搭建，千万不要为了架构而架构）
 */
public class Injection {
    public static DemoRepository provideDemoRepository() {
        //网络API服务
        ApiServer apiService = RetrofitClient.getInstance().create(ApiServer.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return DemoRepository.getInstance(httpDataSource, localDataSource);
    }
}
