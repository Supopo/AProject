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
	
	<#if hasLoadMore>
    public void getDataList(int pageNum){
	
//        dataList.postValue(list);
//        if (list.size() == 0) {
//            //加载结束，没有更多数据了
//            loadStatus.postValue(0);
//        } else {
//            //当前页数据加载完成
//            loadStatus.postValue(1);
//        }
	
	}
	<#else>
	public void getDataList(){
	
	}
	</#if>
	
}
