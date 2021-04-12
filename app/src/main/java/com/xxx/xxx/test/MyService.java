package com.xxx.xxx.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

/**
 * AIDL测试服务
 */
public class MyService extends Service {

    Test.Stub mStub = new Test.Stub() {
        @Override
        public void dosomething() throws RemoteException {
            System.out.println("doSomething");
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) mStub;
    }


    //将AIDL包复制到另外一个app，然后在另外一个app的操作位置绑定服务即可


    //    private ServiceConnection connection = new ServiceConnection() {
    //        @Override
    //        public void onServiceConnected(ComponentName name, IBinder service) {
    //            test = Test.Stub.asInterface(service);
    //            System.out.println("绑定了");
    //            if (test != null) {
    //                try {
    //                    test.dosomething();
    //                } catch (RemoteException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        }
    //
    //        @Override
    //        public void onServiceDisconnected(ComponentName name) {
    //            test = null;
    //        }
    //    };


    // AIDL测试
    //    Intent intent = new Intent();
    //        intent.setAction("com.xxx.xxx.test.MyService");
    //        intent.setPackage("com.xxx.xxx");
    //    bindService(intent, connection, Context.BIND_AUTO_CREATE);//绑定
    // --------------
}
