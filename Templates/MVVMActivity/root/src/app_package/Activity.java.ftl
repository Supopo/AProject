package ${packageName}.activity;

import ${packageName}.R;
import ${packageName}.BR;


//注意ActivityBaseBinding换成自己activity_layout对应的名字 ActivityXxxBinding
public class ${activityClass} extends BaseActivity<ActivityBaseBinding, ${viewModelClass}> {

<#if creatAdapter>
    private ${adapterClass} mAdapter;
	<#if hasLoadMore>
	private int pageNum = 1;
    private BaseLoadMoreModule mLoadMore;
	</#if>
</#if>

	@Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.${activityLayout};

    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


	//页面数据初始化方法
	@Override
    public void initData() {
		<#if creatAdapter>
			initAdapter();
		</#if>
    }


	//页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
	@Override
    public void initViewObservable() {
	<#if creatAdapter>
		<#if hasRefresh>
		//下拉刷新
		binding.srlContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
				<#if hasLoadMore>
                mLoadMore.setEnableLoadMore(false);
                pageNum = 1;
                viewModel.getDataList(pageNum);
				<#else>
				viewModel.getDataList();
				</#if>
				//刷新完立即关闭刷新效果是因为本身请求就带有Dialog
				//如果此处不关闭的话应该在网络请求结束后关闭
                binding.srlContent.setRefreshing(false);
            }
        });
		</#if>

		//往adapter里面加载数据
        viewModel.dataList.observe(this, dataList -> {
            if (dataList != null) {
			<#if hasLoadMore>
                if (pageNum == 1) {
                    mAdapter.setNewInstance(dataList);
                    if (dataList.size() == 0) {
                        //创建适配器.空布局，没有数据时候默认展示的
                       mAdapter.setEmptyView(R.layout.list_empty);
                    }
                } else {
                    mAdapter.addData(dataList);
                }
			<#else>	
				 mAdapter.setNewInstance(dataList);
                    if (dataList.size() == 0) {
                        //创建适配器.空布局，没有数据时候默认展示的
                       mAdapter.setEmptyView(R.layout.list_empty);
                    }
			</#if>
            }
        });

		<#if hasLoadMore>
        //根据下一页的数据来判断是加载完成了还是加载结束
        viewModel.loadStatus.observe(this, status -> {
            if (status == 0) {
                mLoadMore.loadMoreEnd(true);
            } else if (status == 1) {
                mLoadMore.loadMoreComplete();
            }
        });
		</#if>	
	</#if>	

    }
	
	<#if creatAdapter>
	public void initAdapter(){
	    mAdapter = new ${adapterClass}(R.layout.${itemLayout});
		<#if hasLoadMore>
        mLoadMore = mAdapter.getLoadMoreModule();//创建适配器.上拉加载
        mLoadMore.setEnableLoadMore(true);//打开上拉加载
        mLoadMore.setAutoLoadMore(true);//自动加载
        mLoadMore.setPreLoadNumber(1);//设置滑动到倒数第几个条目时自动加载，默认为1
        mLoadMore.setEnableLoadMoreIfNotFullPage(true);//当数据不满一页时继续自动加载
        //mLoadMore.setLoadMoreView(new BaseLoadMoreView)//设置自定义加载布局
        mLoadMore.setOnLoadMoreListener(new OnLoadMoreListener() {//设置加载更多监听事件
            @Override
            public void onLoadMore() {
                pageNum++;
                viewModel.getDataList(pageNum);
            }
        });
		</#if>
		
	    binding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        binding.rvContent.setAdapter(mAdapter);
		
		//Item点击事件
		mAdapter.setOnItemClickListener((adapter, view, position) -> {
          
        });
		
		
		//	子View点击事件
        mAdapter.addChildClickViewIds(R.id.ll_item);
		mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
		
		});
	
	}
	</#if>

}
