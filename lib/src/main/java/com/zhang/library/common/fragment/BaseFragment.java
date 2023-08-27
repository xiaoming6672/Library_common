package com.zhang.library.common.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zhang.library.utils.LogUtils;

/**
 * 基类fragment
 *
 * @author ZhangXiaoMing 2020-11-02 11:08 星期一
 */
public abstract class BaseFragment extends Fragment
        implements View.OnClickListener {

    protected final String TAG = getClass().getSimpleName();


    /** 数据已初始化 */
    protected boolean isDataInitialed;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.info(TAG, "onCreate(savedInstanceState)");
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null)
            onParseArgument(bundle);

        onInitLogicComponent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.info(TAG, "onCreateView()");

        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null)
            return view;

        return inflater.inflate(getContentLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtils.info(TAG, "onViewCreated(view, savedInstanceState)");
        super.onViewCreated(view, savedInstanceState);

        onInitView(view);
        onInitListener(view);

        onPreInitData();
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        LogUtils.info(TAG, "onAttach()>>>activity=%s", activity.getClass().getName());
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

        if (!isDataInitialed) {
            onInitData();
            isDataInitialed = true;
        }
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

        isDataInitialed = false;
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
    public void onClick(View v) {
    }


    @Nullable
    public final <T extends View> T findViewById(int resId) {
        if (getView() == null) {
            return null;
        }

        return getView().findViewById(resId);
    }

    @NonNull
    public final String getFragmentName() {
        return TAG;
    }


    /**
     * 解析Bundle参数
     *
     * @param bundle {@link #setArguments(Bundle)}传递的参数
     */
    protected void onParseArgument(@NonNull Bundle bundle) {
    }

    /** 初始化逻辑类对象 */
    protected abstract void onInitLogicComponent();

    /** Fragment创建时候设置的布局layout */
    @LayoutRes
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     *
     * @param createdView {@link #onViewCreated(View, Bundle)}中的view
     */
    protected abstract void onInitView(@NonNull View createdView);

    /**
     * 初始化监听器
     *
     * @param createdView {@link #onViewCreated(View, Bundle)}中的view
     */
    protected abstract void onInitListener(@NonNull View createdView);

    /** 提前初始化数据 */
    protected void onPreInitData() {
    }

    /** 初始化数据 */
    protected abstract void onInitData();
}
