package com.zhang.library.common.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;
import com.zhang.library.common.vm.factory.ActivityVMFactory;
import com.zhang.library.utils.LogUtils;
import com.zhang.library.utils.context.ViewUtils;

/**
 * 基类Activity
 *
 * @author ZhangXiaoMing 2020-08-13 15:27 星期四
 */
public abstract class XMBaseActivity extends RxAppCompatActivity
        implements View.OnClickListener {

    protected final String TAG = getClass().getSimpleName();

    protected ViewModelProvider mViewModelProvider;


    protected XMBaseActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.info(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        setContentView(getContentLayoutId());

        onParseIntentData(getIntent());
        onInitLogicComponent();
        onInitView();
        onInitListener();
        onInitData();
    }

    @Override
    protected void onStart() {
        LogUtils.info(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        LogUtils.info(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtils.info(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtils.info(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtils.info(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.info(TAG, "onKeyDown()>>>keyCode = %d , event action = %d", keyCode, event.getAction());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LogUtils.info(TAG, "onActivityResult()>>>requestCode = %d , resultCode = %d", requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        LogUtils.info(TAG, "onConfigurationChanged()>>>new layout direction = %d", newConfig.orientation);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
    }


    /**
     * 设置沉浸式状态栏
     *
     * @param isDark 是否黑色字体
     */
    private void setStatusBarTransparent(boolean isDark) {
        ViewUtils.setStatusBarTransparent(this);
        ViewUtils.setStatusBarTextColor(this, isDark);
    }

    /**
     * 创建获取ViewModel对象
     *
     * @param modelClass ViewModel类对象
     * @param <T>        继承ViewModel的子类
     *
     * @return ViewModel对象
     */
    protected <T extends ViewModel> T createViewModel(Class<T> modelClass) {
        if (mViewModelProvider == null) {
            ActivityVMFactory factory = new ActivityVMFactory(this);
            mViewModelProvider = new ViewModelProvider(this, factory);
        }

        return mViewModelProvider.get(modelClass);
    }

    /**
     * 创建获取ViewModel对象
     *
     * @param modelClass ViewModel类对象
     * @param key        创建的key
     * @param <T>        继承ViewModel的子类
     *
     * @return ViewModel对象
     */
    protected <T extends ViewModel> T createViewModel(Class<T> modelClass, String key) {
        if (mViewModelProvider == null) {
            ActivityVMFactory factory = new ActivityVMFactory(this);
            mViewModelProvider = new ViewModelProvider(this, factory);
        }

        return mViewModelProvider.get(key, modelClass);
    }


    /**
     * 解析Intent数据
     *
     * @param intent intent传参
     */
    protected void onParseIntentData(Intent intent) {
    }

    /** 获取布局layout id */
    @LayoutRes
    protected abstract int getContentLayoutId();

    /** 初始化逻辑类对象 */
    protected abstract void onInitLogicComponent();

    /** 初始化布局 */
    protected abstract void onInitView();

    /** 初始化监听器 */
    protected abstract void onInitListener();

    /** 初始化数据 */
    protected abstract void onInitData();

}
