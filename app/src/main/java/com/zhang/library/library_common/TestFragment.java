package com.zhang.library.library_common;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.zhang.library.common.fragment.BaseRxFragment;

/**
 * @author ZhangXiaoMing 2020-11-02 11:21 星期一
 */
public class TestFragment extends BaseRxFragment {


    /** 初始化逻辑类对象 */
    @Override
    protected void onInitLogicComponent() {
    }

    /** Fragment创建时候设置的布局layout */
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test;
    }

    /**
     * 初始化控件
     *
     * @param createdView {@link #onViewCreated(View, Bundle)}中的view
     */
    @Override
    protected void onInitView(@NonNull View createdView) {
    }

    /** 初始化监听器 */
    @Override
    protected void onInitListener() {
    }

    /** 初始化数据 */
    @Override
    protected void onInitData() {
    }
}
