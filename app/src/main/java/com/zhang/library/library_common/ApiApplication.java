package com.zhang.library.library_common;

import android.app.Application;

import com.zhang.library.utils.LogUtils;
import com.zhang.library.utils.context.ContextUtils;

/**
 * @author ZhangXiaoMing 2023-08-05 09:05 周六
 */
public class ApiApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        ContextUtils.set(this);
        LogUtils.init(true);
    }
}
