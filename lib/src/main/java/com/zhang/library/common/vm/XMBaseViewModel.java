package com.zhang.library.common.vm;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.trello.rxlifecycle4.LifecycleTransformer;
import com.zhang.library.common.activity.XMBaseActivity;
import com.zhang.library.common.fragment.XMBaseFragment;

import java.lang.ref.WeakReference;

/**
 * 封装的ViewModel
 *
 * @author ZhangXiaoMing 2023-08-06 19:52 周日
 */
public class XMBaseViewModel extends ViewModel
        implements DefaultLifecycleObserver {

    protected final String TAG;

    protected WeakReference<XMBaseActivity> mActivity;
    protected WeakReference<XMBaseFragment> mFragment;

    {
        TAG = getClass().getName();
    }

    public XMBaseViewModel(@NonNull XMBaseActivity activity) {
        this.mActivity = new WeakReference<>(activity);

        activity.getLifecycle().addObserver(this);
    }

    public XMBaseViewModel(@NonNull XMBaseFragment fragment) {
        this.mActivity = new WeakReference<>((XMBaseActivity) fragment.getActivity());
        this.mFragment = new WeakReference<>(fragment);
    }

    /** 绑定Activity/Fragment的lifecycle */
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        if (mFragment != null)
            return mFragment.get().bindToLifecycle();
        else
            return mActivity.get().bindToLifecycle();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mFragment != null) {
            mFragment.clear();
            mFragment = null;
        }

        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }

    @CallSuper
    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
