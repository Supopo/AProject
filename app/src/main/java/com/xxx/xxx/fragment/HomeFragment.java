package com.xxx.xxx.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xxx.xxx.BR;
import com.xxx.xxx.R;
import com.xxx.xxx.activity.ShowPicsActivity;
import com.xxx.xxx.activity.WebActivity;
import com.xxx.xxx.adapter.BannerHolder;
import com.xxx.xxx.adapter.TagsAdapter;
import com.xxx.xxx.app.Constant;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.databinding.FragmentHomeBinding;
import com.xxx.xxx.databinding.ItemVpagerBinding;
import com.xxx.xxx.viewModel.HomeViewModel;
import com.xxx.xxx.widget.AutoLineLayoutManager;
import com.xxx.xxx.widget.CardTransformer;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.io.Serializable;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ToastUtils;

//注意ActivityBaseBinding换成自己fragment_layout对应的名字 FragmentXxxBinding
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {


    private MZHolderCreator<BannerHolder> mzHolderCreator;
    private List<BannerBean> banners;
    private TagsAdapter tagsAdapter;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    //页面数据初始化方法
    @Override
    public void initData() {
        //Banner创造器
        mzHolderCreator = new MZHolderCreator<BannerHolder>() {
            @Override
            public BannerHolder createViewHolder() {
                return new BannerHolder();
            }
        };
        //请求Banner
        viewModel.getBanners();

        viewModel.getTags();

        //设置展开、收缩TextView
        binding.tvText.setShowMaxEms(8);
        binding.tvText.setShowFontColor(getActivity().getResources().getColor(R.color.blue));
        binding.tvText.setDefaultText("一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十");
        binding.tvText.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "点击了", Toast.LENGTH_SHORT).show();
        });

        binding.tvText2.setShowMaxEms(8);
        binding.tvText2.setDefaultText("一二三四五六七");
        binding.tvText2.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));


        //标签流内容
        tagsAdapter = new TagsAdapter(R.layout.item_tags);

        AutoLineLayoutManager autoLineLayoutManager = new AutoLineLayoutManager();
        binding.rvTag.setLayoutManager(autoLineLayoutManager);
        binding.rvTag.setAdapter(tagsAdapter);


        //PopupWindow内容
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_bottom, null);
        PopupWindow popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //外部点击取消
        popupWindow.setOutsideTouchable(true);

        binding.civImage.setOnClickListener(lis -> {
//            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);


            Animation sss = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_enter);
            Animation sss2 = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_exit);
            binding.civImage.startAnimation(sss);
            sss.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.civImage.startAnimation(sss2);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        });


        //TabLayout内容
        String[] titles = {"标签1", "标签2", "标签3", "标签4"};
        for (String title : titles) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title));
        }

        TabLayout.Tab tabAt = binding.tabLayout.getTabAt(0);
        setSelectTab(tabAt);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSelectTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_layout);
                }
                TextView tvTab = tab.getCustomView().findViewById(android.R.id.text1);
                tvTab.setCompoundDrawables(null, null, null, null);
                tvTab.setTextAppearance(R.style.TabLayoutTextUnSelected);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //子线程往主线程发送消息2
        binding.btn1.setOnClickListener(lis -> {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Log.e("--", "点击事件是在主线程");
            } else {
                Log.e("--", "点击事件是在子线程");
            }

            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        Log.e("--", "子线程发消息");
                    }
                    mHandler.sendEmptyMessage(0);
                }
            };
            thread.start();
        });


        //主线程往子线程发送消息1
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                //初始化
//                Looper.prepare();
//                mHandler2 = new Handler() {
//                    @Override
//                    public void handleMessage(@NonNull Message msg) {
//                        super.handleMessage(msg);
//                        if (Looper.myLooper() != Looper.getMainLooper()) {
//                            Log.e("--", "子线程收消息");
//                        }
//                        //处理完成后手动关闭
//                        mHandler2.getLooper().quit();
//                    }
//                };
//                //开始轮循
//                Looper.loop();
//            }
//        };
//        thread.start();


        //主线程往子线程发送消息1 利用HandlerThread
        HandlerThread handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();

        mHandler2 = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    Log.e("--", "子线程收消息");
                }
            }
        };


        //主线程往子线程发送消息2
        binding.btn2.setOnClickListener(lis -> {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Log.e("--", "主线程发消息");
            }
            mHandler2.sendEmptyMessage(1);
        });

    }

    private Handler mHandler2;

    //子线程往主线程发送消息1
    //在主线程中创建Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (Looper.myLooper() == Looper.getMainLooper()) {
                Log.e("--", "主线程收消息");
            }

            ToastUtils.showLong(msg.toString());
        }
    };

    private void setSelectTab(TabLayout.Tab tab) {
        View customView = tab.getCustomView();
        if (customView == null) {
            tab.setCustomView(R.layout.tab_text_layout);
        }
        TextView tvTab = tab.getCustomView().findViewById(android.R.id.text1);

        Drawable drawable = getResources().getDrawable(R.drawable.tab_layout_line);
        //切记这一行一定要设置
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        tvTab.setCompoundDrawables(null, null, null, drawable);
        tvTab.setCompoundDrawablePadding(-16);
        tvTab.setTextAppearance(R.style.TabLayoutTextSelected);
    }

    private void initViewPager() {
        //设置切换动画
        binding.viewPager.setPageTransformer(true,
                new CardTransformer(QMUIDisplayHelper.dp2px(getActivity(), 20)));
        binding.viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                //不设置banners.size是为了无线循环滑动
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            //返回要显示的条目内容
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_vpager, null);
                ItemVpagerBinding vPagerBinding = DataBindingUtil.bind(view);

                //手动设置当前展示View的偏移量，使前后View偏差，从而达到层叠效果
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vPagerBinding.ivPager.getLayoutParams();
                params.setMarginEnd(QMUIDisplayHelper.dp2px(getActivity(), 15));
                vPagerBinding.ivPager.setLayoutParams(params);

                //为了防止角标越界需要判断一下
                if (position >= banners.size()) {
                    vPagerBinding.setViewModel(banners.get(position % banners.size()));
                } else {
                    vPagerBinding.setViewModel(banners.get(position));
                }


                vPagerBinding.ivPager.setOnClickListener(lis -> {
                    Intent intent = new Intent();
                    intent.putExtra("imgs", (Serializable) banners);
                    intent.setClass(getActivity(), ShowPicsActivity.class);
                    startActivity(intent);
                });

                container.addView(view);
                return view;
            }

            //销毁条目
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //object:刚才创建的对象，即要销毁的对象
                container.removeView((View) object);
            }
        });
        //设置预加载页数
        binding.viewPager.setOffscreenPageLimit(2);
    }


    //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
    @Override
    public void initViewObservable() {
        //轮播图数据回调
        viewModel.banners.observe(getActivity(), banners -> {
            if (banners != null) {
                this.banners = banners;
                //设置数据
                binding.banner.setPages(banners, mzHolderCreator);
                //开始轮播
                binding.banner.start();
                //设置层叠滑动效果的ViewPager
                initViewPager();
            }
        });
        binding.banner.setBannerPageClickListener(((view, i) -> {
            Intent intent = new Intent();
            intent.putExtra(Constant.PageTitle, banners.get(i).getTitle());
            intent.putExtra(Constant.PageUrl, banners.get(i).getUrl());
            intent.setClass(getActivity(), WebActivity.class);
            startActivity(intent);
        }));

        viewModel.tags.observe(getActivity(), tags -> {
            if (tags != null) {
                tagsAdapter.setList(tags);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        binding.banner.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.banner.start();//开始轮播
    }

}
