package com.xxx.xxx.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateAppManager {
    private String updateInfo;
    private String versionName;
    private double size;
    private int mustUpdate;
    private QMUIDialog mInstallDialog;
    private String downUrl;
    private boolean haveInstallPermission;
    private String authorities;// 清单文件中配置的FileProvider的authorities
    private Activity activity;

    public UpdateAppManager(Activity activity) {
        this.activity = activity;

    }

    //    获取本地软件版本号
    private int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            return 0;
        }

    }

    public void onActivityResult() {
        if (mInstallDialog != null && mInstallDialog.isShowing()) {
            mInstallDialog.dismiss();
        }
        //开启权限回来了
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                //没有权限
                installDialog();
                return;
            }
        }
        //有权限，开始下载应用程序
        downloadApk();
    }


    /**
     * 更新提示
     */
    private void updateDialog() {
        new QMUIDialog.MessageDialogBuilder(activity)
                .setTitle("发现新版本" + versionName)
                .setMessage(updateInfo)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {

                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();

                        //先根据下载路径去安装，如果文件为空会自动下载。
                        //安装应用的流程
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //先获取是否有安装未知来源应用的权限
                            haveInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
                            if (!haveInstallPermission) {
                                //没有权限
                                installDialog();
                                return;
                            }
                        }
                        //有权限，开始下载
                        downloadApk();
                    }
                })
                .show();
    }

    private void installDialog() {
        mInstallDialog = new QMUIDialog.MessageDialogBuilder(activity)
                .setTitle("安装提示")
                .setMessage("安装应用需要打开未知来源权限，请去设置中开启权限")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        startInstallPermissionSettingActivity();
                    }
                })
                .show();
    }

    /**
     * 从服务器端下载最新apk
     */
    private void downloadApk() {
        //显示下载进度
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setTitle(versionName + "版本更新 (" + size + "MB)");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.setCancelable(false);
        dialog.show();

        //访问网络下载apk
        new Thread(new DownloadApk(dialog)).start();
    }

    /**
     * 访问网络下载apk
     */
    private class DownloadApk implements Runnable {
        private ProgressDialog dialog;
        InputStream is;
        FileOutputStream fos;

        public DownloadApk(ProgressDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            String url = downUrl;
            Request request = new Request.Builder().get().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    //获取内容总长度
                    long contentLength = response.body().contentLength();
                    //设置最大值(百分比)
                    dialog.setMax(100);
                    //保存到sd卡
                    File apkFile = new File(Environment.getExternalStorageDirectory(), "jkzl" + versionName + ".apk");

                    fos = new FileOutputStream(apkFile);
                    //获得输入流
                    is = response.body().byteStream();
                    //定义缓冲区大小
                    byte[] bys = new byte[1024];
                    int progress = 0;
                    int len = -1;
                    while ((len = is.read(bys)) != -1) {
                        try {
                            Thread.sleep(1);
                            fos.write(bys, 0, len);
                            fos.flush();
                            progress += len;
                            //设置进度百分比
                            double n = (double) progress / contentLength;
                            dialog.setProgress((int) (100 * n));
                        } catch (InterruptedException e) {
                        }
                    }
                    //先去尝试安装
                    installApk();
                }
            } catch (IOException e) {
            } finally {
                //关闭io流
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    is = null;
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fos = null;
                }
            }
            dialog.dismiss();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        activity.startActivityForResult(intent, 10086);
    }

    /**
     * 下载完成,提示用户安装
     */
    private void installApk() {
        //先制定安装路径
        File apkFile = new File(Environment.getExternalStorageDirectory(), "jkzl" + versionName + ".apk");
        //文件不存在就去下载
        if (!apkFile.exists()) {
            downloadApk();
            return;
        }
        //调用系统安装程序
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            // 7.0以上要通过fileprovider获取安装包位置
            // UpdateConfig.FILE_PROVIDER_AUTH 即是在清单文件中配置的authorities
            apkUri = FileProvider.getUriForFile(activity, authorities, apkFile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            apkUri = Uri.fromFile(apkFile);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }

        activity.startActivity(intent);
    }
}
