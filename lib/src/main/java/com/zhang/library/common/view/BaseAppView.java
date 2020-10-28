package com.zhang.library.common.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.zhang.library.utils.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 基类View
 *
 * @author ZhangXiaoMing 2020-08-13 15:27 星期四
 */
public abstract class BaseAppView extends FrameLayout
        implements View.OnClickListener {

    protected final String TAG = getClass().getSimpleName();

    public BaseAppView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BaseAppView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseAppView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        LayoutInflater.from(context).inflate(onCreateLayoutId(), this);

        initView();
        initData();
    }

    @Override
    protected void onAttachedToWindow() {
        LogUtils.info(TAG, "onAttachedToWindow()");
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        LogUtils.info(TAG, "onDetachedFromWindow()");
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v) {
    }

    public Activity getActivity() {
        Context context = getContext();
        if (context instanceof Activity) {
            return ((Activity) context);
        }
        return null;
    }

    /** 获取View的布局 */
    protected abstract int onCreateLayoutId();

    /** 初始化控件 */
    protected abstract void initView();

    /** 初始化数据 */
    protected abstract void initData();
}
