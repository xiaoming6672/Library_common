package com.zhang.library.library_common;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelCreator;

import com.zhang.library.common.activity.BaseRxActivity;
import com.zhang.library.utils.LogUtils;

public class MainActivity extends BaseRxActivity {

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
        ViewModelCreator<TestViewModel> creator = ViewModelCreator.newBuilder(this, this, TestViewModel.class);
        TestViewModel model = creator.create();
        LogUtils.debug("ZHANG", "onInitLogicComponent()>>>creator=%s, model=%s", creator, model);
    }

    @Override
    protected void onInitData() {
        ViewModelCreator<TestViewModel> creator = ViewModelCreator.newBuilder(this, this, TestViewModel.class);
        TestViewModel model = creator.create();
        LogUtils.debug("ZHANG", "onInitData()>>>creator=%s, model=%s", creator, model);

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