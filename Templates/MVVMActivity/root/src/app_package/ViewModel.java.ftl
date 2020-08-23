package ${packageName}.viewModel;

   <#if useAndroidX>
        import androidx.annotation.NonNull;
    </#if>
public class ${viewModelClass} extends BaseViewModel {

	<#if creatAdapter>
    public MutableLiveData<List<${beanClass}>> dataList = new MutableLiveData<>();
	<#if hasLoadMore>
    public MutableLiveData<Integer> loadStatus = new MutableLiveData<>();
    </#if>
	</#if>
   
	public  ${viewModelClass}(@NonNull Application application) {
        super(application);
    }
	
	<#if creatAdapter>
	<#if hasLoadMore>
    public void getDataList(int pageNum){

  addSubscribe(
                RetrofitClient.getInstance().create(ApiServer.class)
                        .getXxxList(pageNum, 10)
                        .compose(RxUtils.schedulersTransformer()) //线程调度
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showDialog();
                            }
                        })
                        .subscribe(new Consumer<BaseResponse<${listBeanClass}>>() {
                            @Override
                            public void accept(BaseResponse<${listBeanClass}> bean) throws Exception {
                                dismissDialog();
                                if (bean.isOk()) {
                                    if (bean.getResult().list != null) {
                                        dataList.postValue(bean.getResult().list);
                                        if (bean.getResult().list.size() == 0) {
                                            //加载结束，没有更多数据了
                                            loadStatus.postValue(0);
                                        } else {
                                            //加载完成
                                            loadStatus.postValue(1);
                                        }
                                    }
                                } else {
                                    if (pageNum > 1) {
                                        //加载结束，没有更多数据了
                                        loadStatus.postValue(0);
                                    } else {
                                        ToastUtils.showLong(bean.getMessage());
                                    }
                                }
                            }
                        })
        );

	}
	<#else>
	public void getDataList(){
	
	}
	</#if>
	  </#if>
	
}
