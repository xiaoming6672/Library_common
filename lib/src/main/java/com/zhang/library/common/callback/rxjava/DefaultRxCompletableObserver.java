package com.zhang.library.common.callback.rxjava;

import com.zhang.library.utils.LogUtils;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 默认空实现的{@link Completable}的观察者
 *
 * @author ZhangXiaoMing 2023-09-08 11:07 周五
 */
public interface DefaultRxCompletableObserver extends CompletableObserver {

    /**
     * Called once by the {@link Completable} to set a {@link Disposable} on this instance which
     * then can be used to cancel the subscription at any time.
     *
     * @param d the {@code Disposable} instance to call dispose on for cancellation, not null
     */
    @Override
    default void onSubscribe(@NonNull Disposable d) {
    }

    /**
     * Called once if the deferred computation 'throws' an exception.
     *
     * @param e the exception, not {@code null}.
     */
    @Override
    default void onError(@NonNull Throwable e) {
        LogUtils.error("DefaultRxObserverError", getClass().getName() + ">>>onError>>>e=%s", e.toString());
        e.printStackTrace();
    }
}
