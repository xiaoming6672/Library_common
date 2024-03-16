package com.zhang.library.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.zhang.library.utils.LogUtils;

/**
 * 基类View
 *
 * @author ZhangXiaoMing 2020-08-13 15:27 星期四
 */
public abstract class BaseAppView extends FrameLayout
        implements View.OnClickListener {


    protected final String TAG = getClass().getSimpleName();


    public BaseAppView(@NonNull Context context) {
        this(context, null);
    }

    public BaseAppView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseAppView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    protected void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(context).inflate(onCreateLayoutId(), this);

        initAttribute(context, attrs);

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


    public FragmentActivity getActivity() {
        Context context = getContext();
        if (context instanceof FragmentActivity) {
            return ((FragmentActivity) context);
        }
        return null;
    }


    /**
     * 初始化属性
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    protected void initAttribute(@NonNull Context context, @Nullable AttributeSet attrs) {
    }


    /** 获取View的布局 */
    @LayoutRes
    protected abstract int onCreateLayoutId();

    /** 初始化控件 */
    protected abstract void initView();

    /** 初始化数据 */
    protected abstract void initData();

}
