package com.dhj.myapp;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by zzh on 2016/11/10.
 */

public class MyAppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }
}
