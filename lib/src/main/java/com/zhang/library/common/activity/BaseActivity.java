package com.zhang.library.common.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.zhang.library.utils.LogUtils;
import com.zhang.library.utils.context.ToastUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 基类Activity
 *
 * @author ZhangXiaoMing 2020-08-13 15:27 星期四
 */
public abstract class BaseActivity extends AppCompatActivity
        implements View.OnClickListener {

    protected final String TAG = getClass().getSimpleName();
    private ProgressDialog mProgressDialog;

    public BaseActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.info(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        if (isStatusBarTransparent()) {
            setStatusBarTransparent();
        }
        setContentView(onCreateLayoutId());

        setRequestedOrientation(getActivityOrientation());

        getIntentData();
        initView();
        initData();
    }

    protected boolean isStatusBarTransparent() {
        return false;
    }

    /** 获得Activity的方向 */
    protected int getActivityOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    protected void getIntentData() {
    }

    /** 设置沉浸式状态栏 */
    private void setStatusBarTransparent() {
        StatusBarUtil.setTransparent(getActivity());
    }

    /** 显示Toast提示 */
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    /** 显示Toast提示 */
    public void showToast(CharSequence text) {
        ToastUtils.show(text);
    }

    /**
     * 显示进度弹框
     *
     * @param msg 显示文字
     */
    public void showProgress(String msg) {
        showProgress(msg, ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     * 显示进度弹框
     *
     * @param msg           显示文字
     * @param progressStyle 进度框样式 参考{@link ProgressDialog#STYLE_HORIZONTAL}和{@link
     *                      ProgressDialog#STYLE_SPINNER}
     */
    public void showProgress(String msg, int progressStyle) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mProgressDialog.setProgressStyle(progressStyle);
        if (progressStyle == ProgressDialog.STYLE_HORIZONTAL)
            mProgressDialog.setMax(100);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    /**
     * 更新进度框进度
     *
     * @param progress 进度
     */
    public void updateProgress(int progress) {
        if (mProgressDialog == null || !mProgressDialog.isShowing())
            return;
        if (mProgressDialog.getMax() == 0)
            mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress);
    }

    /** 隐藏进度框 */
    public void dismissProgress() {
        if (mProgressDialog == null || !mProgressDialog.isShowing())
            return;
        mProgressDialog.dismiss();
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

    /** Activity创建时候设置的布局layout */
    protected abstract int onCreateLayoutId();

    /** 初始化控件 */
    protected abstract void initView();

    /** 初始化数据 */
    protected abstract void initData();

}
