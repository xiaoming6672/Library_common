package com.zhang.library.common.viewmodel;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;

/**
 * 封装的ViewModel
 *
 * @author ZhangXiaoMing 2023-08-06 19:52 周日
 */
public class BaseViewModel extends ViewModel
        implements DefaultLifecycleObserver {

    protected final String TAG;

    protected WeakReference<FragmentActivity> mActivity;
    protected WeakReference<Fragment> mFragment;
    protected WeakReference<DialogFragment> mDialog;

    {
        TAG = getClass().getName();
    }

    public BaseViewModel(@NonNull FragmentActivity activity) {
        this.mActivity = new WeakReference<>(activity);

        activity.getLifecycle().addObserver(this);
    }

    public BaseViewModel(@NonNull Fragment fragment) {
        this.mActivity = new WeakReference<>(fragment.requireActivity());
        this.mFragment = new WeakReference<>(fragment);

        fragment.getLifecycle().addObserver(this);
    }

    public BaseViewModel(@NonNull DialogFragment dialog) {
        this.mActivity = new WeakReference<>(dialog.requireActivity());
        this.mDialog = new WeakReference<>(dialog);

        dialog.getLifecycle().addObserver(this);
    }


    @Override
    protected void onCleared() {
        super.onCleared();

        if (mDialog != null) {
            mDialog.clear();
            mDialog = null;
        }

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
