package com.zhang.library.common.viewmodel;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.zhang.library.utils.CollectionUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

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

    private final List<Disposable> mDisposableList;

    {
        TAG = getClass().getName();
    }

    public BaseViewModel(@NonNull FragmentActivity activity) {
        this.mActivity = new WeakReference<>(activity);
        this.mDisposableList = new ArrayList<>();

        activity.getLifecycle().addObserver(this);
    }

    public BaseViewModel(@NonNull Fragment fragment) {
        this.mActivity = new WeakReference<>(fragment.requireActivity());
        this.mFragment = new WeakReference<>(fragment);
        this.mDisposableList = new ArrayList<>();

        fragment.getLifecycle().addObserver(this);
    }

    public BaseViewModel(@NonNull DialogFragment dialog) {
        this.mActivity = new WeakReference<>(dialog.requireActivity());
        this.mDialog = new WeakReference<>(dialog);
        this.mDisposableList = new ArrayList<>();

        dialog.getLifecycle().addObserver(this);
    }

    protected void addDisposable(Disposable disposable) {
        mDisposableList.add(disposable);
    }

    protected void clearDisposable() {
        if (CollectionUtils.isEmpty(mDisposableList))
            return;

        Iterator<Disposable> it = mDisposableList.iterator();
        while (it.hasNext()) {
            Disposable next = it.next();
            if (!next.isDisposed())
                next.dispose();

            it.remove();
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();

        clearDisposable();

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
