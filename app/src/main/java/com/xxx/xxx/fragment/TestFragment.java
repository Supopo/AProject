package com.xxx.xxx.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.xxx.xxx.MainActivity;
import com.xxx.xxx.R;
import com.xxx.xxx.BR;
import com.xxx.xxx.adapter.TestAdapter;
import com.xxx.xxx.bean.TestBean;
import com.xxx.xxx.databinding.FragmentTestBinding;
import com.xxx.xxx.viewModel.TestViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.databinding.ActivityBaseBinding;
import me.goldze.mvvmhabit.utils.ToastUtils;

import static android.app.Activity.RESULT_OK;

//注意ActivityBaseBinding换成自己fragment_layout对应的名字 FragmentXxxBinding
public class TestFragment extends BaseFragment<FragmentTestBinding, TestViewModel> {

    private String selectFile;
    private TestAdapter adapter;
    private String folderPath;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_test;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    //页面数据初始化方法
    @Override
    public void initData() {
        binding.btnGet.setOnClickListener(lis -> {
            toGet();
        });

        binding.rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TestAdapter(R.layout.item_test);
        binding.rvContent.setAdapter(adapter);

        binding.btnSave.setOnClickListener(lis -> {
            createExcel("测试文件");
        });

    }


    //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
    @Override
    public void initViewObservable() {

    }

    private void toGet() {
        new LFilePicker()
                //Fragment写法
                .withSupportFragment(this)
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                .withStartPath("/storage/emulated/0/")
                .withIsGreater(false)
                .withFileSize(500 * 1024)
                .withFileFilter(new String[]{".xls"})
                .withMaxNum(1)
                .start();
    }

    int REQUESTCODE_FROM_ACTIVITY = 1000;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                //If it is a file selection mode, you need to get the path collection of all the files selected
                //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                //文件路径
                List<String> list = data.getStringArrayListExtra("paths");
                Log.e("--", "filePath:" + list.toString());
                //If it is a folder selection mode, you need to get the folder path of your choice
                //文件夹路径
                folderPath = data.getStringExtra("path");
                Log.e("--", "folderPath:" + folderPath);

                if (list.size() > 0) {
                    selectFile = list.get(0);
                }
                new ExcelAsynTask().execute();
            }
        }
    }


    class ExcelAsynTask extends AsyncTask<Void, Void, List<TestBean>> {
        //运行子主线程，执行耗时操作，防止主线程阻塞，出现ANR
        @Override
        protected List<TestBean> doInBackground(Void... voids) {
            return readExcel(selectFile);
        }

        //运行于主线程，更新进度
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //运行于主线程，耗时操作结束后执行该方法
        @Override
        protected void onPostExecute(List<TestBean> list) {
            super.onPostExecute(list);
            adapter.setNewInstance(list);
        }
    }


    //读取Excel
    private List readExcel(String filePath) {
        List<TestBean> testBeans = new ArrayList<>();
        try {
            /**
             * 后续考虑问题,比如Excel里面的图片以及其他数据类型的读取
             **/
            InputStream is = new FileInputStream(filePath);
            Workbook book = Workbook
                    .getWorkbook(new File(filePath));
            book.getNumberOfSheets();
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();
            int Cols = sheet.getColumns();
            System.out.println("当前工作表的名字:" + sheet.getName());
            System.out.println("总行数:" + Rows);
            System.out.println("总列数:" + Cols);

            // 得到第一列第一行的单元格
            // Cell cell1 = sheet.getCell(0, 0);
            // String result = cell1.getContents();
            // 获得某个单元格的值
            // sheet.getCell(Col,Row).getContents()

            //            for (int i = 0; i < Cols; ++i) {
            //                for (int j = 0; j < Rows; ++j) {
            //                    System.out
            //                            .print((sheet.getCell(i, j)).getContents() + "\t");
            //                }
            //            System.out.print("\n");
            //            }


            // 因为知道测试表格有4列所以没有循环列数
            for (int i = 0; i < Rows; ++i) {
                TestBean testBean = new TestBean();

                // 判断有数据才加入
                if (!TextUtils.isEmpty(sheet.getCell(0, i).getContents())
                        && !TextUtils.isEmpty(sheet.getCell(1, i).getContents())
                        && !TextUtils.isEmpty(sheet.getCell(2, i).getContents())
                        && !TextUtils.isEmpty(sheet.getCell(3, i).getContents())
                ) {

                    testBean.setName((sheet.getCell(0, i)).getContents());
                    testBean.setStatus((sheet.getCell(1, i)).getContents());
                    testBean.setAcc((sheet.getCell(2, i)).getContents());
                    testBean.setPsd((sheet.getCell(3, i)).getContents());
                    testBeans.add(testBean);

                }


            }

            book.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return testBeans;

    }


    //保存到Excel
    public void createExcel(String fileName) {
        try {
            // 创建或打开Excel文件
            WritableWorkbook book = Workbook.createWorkbook(new File(
                    folderPath + "/" + fileName + ".xls"));

            // 生成名为“第一页”的工作表,参数0表示这是第一页
            WritableSheet sheet1 = book.createSheet("第一页", 0);
            WritableSheet sheet2 = book.createSheet("第三页", 2);

            // 在Label对象的构造函数中,元格位置是第一列第一行(0,0)以及单元格内容为test
            Label label = new Label(0, 0, "test");
            // 将定义好的单元格添加到工作表中
            sheet1.addCell(label);

            /*
             * 生成一个保存数字的单元格.必须使用Number的完整包路径,否则有语法歧义
             */
            jxl.write.Number number = new jxl.write.Number(1, 0, 555.12541);
            sheet2.addCell(number);

            // 写入数据并关闭文件
            book.write();
            book.close();

            ToastUtils.showLong("已保存！");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
