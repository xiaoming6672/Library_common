package com.zhang.library.common.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhang.library.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 基类fragment
 *
 * @author ZhangXiaoMing 2020-11-02 11:08 星期一
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    private View mContentView;

    public BaseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.info(TAG, "onCreate(savedInstanceState)");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.info(TAG, "onCreateView()");

        mContentView = inflater.inflate(onCreateLayoutId(), container, false);
        initView();
        initData();
        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtils.info(TAG, "onViewCreated(view, savedInstanceState)");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        LogUtils.info(TAG, "onAttach()>>>activity = %s", activity.getClass().getName());
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtils.info(TAG, "onActivityCreated(savedInstanceState)");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        LogUtils.info(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtils.info(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.info(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtils.info(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtils.info(TAG, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtils.info(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtils.info(TAG, "onDetach()");
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.info(TAG, "onActivityResult()>>>requestCode = %d , resultCode = %d", requestCode, resultCode);
    }

    public final <T extends View> T findViewById(int resId) {
        if (mContentView == null) {
            return null;
        }

        return mContentView.findViewById(resId);
    }

    public final String getFragmentName() {
        return TAG;
    }

    /** Fragment创建时候设置的布局layout */
    protected abstract int onCreateLayoutId();

    /** 初始化控件 */
    protected abstract void initView();

    /** 初始化数据 */
    protected abstract void initData();
}
