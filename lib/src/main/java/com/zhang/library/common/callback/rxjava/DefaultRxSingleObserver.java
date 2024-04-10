package com.zhang.library.common.callback.rxjava;

import com.zhang.library.utils.LogUtils;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 默认空实现了{@link #onSubscribe(Disposable)}和{@link #onError(Throwable)}方法的{@link Single}观察者
 *
 * @author ZhangXiaoMing 2023-09-03 21:03 周日
 */
public interface DefaultRxSingleObserver<T> extends SingleObserver<T> {

    /**
     * Provides the {@link SingleObserver} with the means of cancelling (disposing) the connection
     * (channel) with the Single in both synchronous (from within {@code onSubscribe(Disposable)}
     * itself) and asynchronous manner.
     *
     * @param d the Disposable instance whose {@link Disposable#dispose()} can be called anytime to
     *          cancel the connection
     *
     * @since 2.0
     */
    @Override
    default void onSubscribe(@NonNull Disposable d) {
    }

    /**
     * Notifies the {@link SingleObserver} with a single item and that the {@link Single} has
     * finished sending push-based notifications.
     * <p>
     * The {@code Single} will not call this method if it calls {@link #onError}.
     *
     * @param t the item emitted by the {@code Single}
     */
    @Override
    void onSuccess(@NonNull T t);

    /**
     * Notifies the {@link SingleObserver} that the {@link Single} has experienced an error
     * condition.
     * <p>
     * If the {@code Single} calls this method, it will not thereafter call {@link #onSuccess}.
     *
     * @param e the exception encountered by the {@code Single}
     */
    @Override
    @CallSuper
    default void onError(@NonNull Throwable e) {
        LogUtils.error("DefaultRxObserverError", getClass().getName() + ">>>onError>>>e=%s", e.toString());
        e.printStackTrace();
    }
}
