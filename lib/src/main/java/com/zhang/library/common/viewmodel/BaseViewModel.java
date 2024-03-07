package com.zhang.library.common.viewmodel;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelCreator;

import com.zhang.library.utils.CollectionUtils;

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

    private final List<Disposable> mDisposableList;


    public <T extends BaseViewModel> BaseViewModel(ViewModelCreator<T> creator) {
        TAG = getClass().getName();
        mDisposableList = new ArrayList<>();
        onParseBundle(creator.getBundle());

        creator.getLifecycleOwner().getLifecycle().addObserver(this);
    }

    protected void onParseBundle(@NonNull Bundle bundle) {
    }

    protected void addDisposable(Disposable disposable) {
        mDisposableList.add(disposable);
    }

    protected void removeDisposable(Disposable disposable) {
        mDisposableList.remove(disposable);
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
    }

    @CallSuper
    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
