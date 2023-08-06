package com.zhang.library.library_common;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.zhang.library.common.activity.XMBaseActivity;

public class MainActivity extends XMBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /** 获取布局layout id */
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitView() {
    }

    /** 初始化逻辑类对象 */
    @Override
    protected void onInitLogicComponent() {
    }

    @Override
    protected void onInitData() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        TestFragment fragment = new TestFragment();

        ft.add(R.id.fl_container, fragment, fragment.getFragmentName());

        ft.commitAllowingStateLoss();
    }

    /** 初始化监听器 */
    @Override
    protected void onInitListener() {
    }

}