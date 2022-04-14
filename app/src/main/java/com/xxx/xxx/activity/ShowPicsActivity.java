package com.xxx.xxx.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xxx.xxx.R;
import com.xxx.xxx.bean.BannerBean;
import com.xxx.xxx.databinding.ActivityShowPicsBinding;
import com.xxx.xxx.databinding.ItemVpagerBinding;
import com.xxx.xxx.utils.DonwloadSaveImg;
import com.xxx.xxx.viewModel.ShowPicsViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.ToastUtils;


//注意ActivityBaseBinding换成自己activity_layout对应的名字 ActivityXxxBinding
public class ShowPicsActivity extends BaseActivity<ActivityShowPicsBinding, ShowPicsViewModel> {


    private List<BannerBean> list = new ArrayList<>();

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_show_pics;

    }

    @Override
    public int initVariableId() {
        return com.xxx.xxx.BR.viewModel;
    }


    //页面数据初始化方法
    @Override
    public void initData() {
        setStatusBarTransparent();
        rlTitle.setVisibility(View.GONE);

        //        list = (List<BannerBean>) getIntent().getSerializableExtra("imgs");
        //图片地址不是.jpg结尾有的手机下载不下来
        list.add(new BannerBean("https://ae01.alicdn.com/kf/U6de089ce45ff468a8f06c50e19ad7379N.jpg"));
        list.add(new BannerBean("https://ae01.alicdn.com/kf/Ue16c54cac6574a06a0c1afdad979b007W.jpg"));
        list.add(new BannerBean("https://ae01.alicdn.com/kf/Uec00959acd9c4d0aa900d5fb8ea481931.jpg"));

        if (list != null) {
            PagerAdapter pagerAdapter = new PagerAdapter() {

                @Override
                public int getCount() {
                    return list.size();
                }

                @Override
                public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                    return view == object;
                }

                @NonNull
                @Override
                public Object instantiateItem(@NonNull ViewGroup container, int position) {
                    View view = LayoutInflater.from(ShowPicsActivity.this).inflate(R.layout.item_vpager, null);
                    ItemVpagerBinding vPagerBinding = DataBindingUtil.bind(view);
                    vPagerBinding.setViewModel(list.get(position));


                    vPagerBinding.ivPager.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showSaveDialog(position);
                            return false;
                        }
                    });

                    container.addView(view);
                    return view;
                }

                @Override
                public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                    //object:刚才创建的对象，即要销毁的对象
                    container.removeView((View) object);
                }
            };
            binding.viewPager.setAdapter(pagerAdapter);
        }


    }


    //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
    @Override
    public void initViewObservable() {

    }


    private void showSaveDialog(int position) {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("保存图片")
                .setMessage("确定要保存到相册吗？")
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
                    Disposable disposable = permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                            .subscribe(granted -> {
                                if (granted) {
                                    //保存到相册
                                    DonwloadSaveImg.donwloadImg(ShowPicsActivity.this, list.get(position).getImagePath());
                                } else {
                                    ToastUtils.showShort("访问权限已拒绝");
                                }

                            });
                    dialog.dismiss();
                })
                .show();
    }

}
