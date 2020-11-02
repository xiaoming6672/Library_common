package com.zhang.library.library_common;

import android.os.Bundle;

import com.zhang.library.common.activity.BaseActivity;
import com.zhang.library.utils.LogUtils;

import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.setDebug(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int onCreateLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        TestFragment fragment = new TestFragment();

        ft.add(R.id.fl_container, fragment, fragment.getFragmentName());

        ft.commitAllowingStateLoss();
    }
}