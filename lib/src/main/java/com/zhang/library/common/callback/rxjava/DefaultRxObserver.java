package com.zhang.library.common.callback.rxjava;

import com.zhang.library.utils.LogUtils;

import androidx.annotation.CallSuper;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 默认空实现了{@link #onSubscribe}、{@link #onError}和{@link #onComplete}的{@link Observable}观察者
 *
 * @author ZhangXiaoMing 2023-07-14 10:16 周五
 */
public interface DefaultRxObserver<T> extends Observer<T> {

    /**
     * Provides the {@link Observer} with the means of cancelling (disposing) the connection
     * (channel) with the {@link Observable} in both synchronous (from within
     * {@link #onNext(Object)}) and asynchronous manner.
     *
     * @param d the {@link Disposable} instance whose {@link Disposable#dispose()} can be called
     *          anytime to cancel the connection
     *
     * @since 2.0
     */
    @Override
    default void onSubscribe(@NonNull Disposable d) {
    }

//    /**
//     * Provides the {@link Observer} with a new item to observe.
//     * <p>
//     * The {@link Observable} may call this method 0 or more times.
//     * <p>
//     * The {@code Observable} will not call this method again after it calls either {@link
//     * #onComplete} or {@link #onError}.
//     *
//     * @param t the item emitted by the Observable
//     */
//    @Override
//    default void onNext(@NonNull T t) {
//    }


    /**
     * Provides the {@link Observer} with a new item to observe.
     * <p>
     * The {@link Observable} may call this method 0 or more times.
     * <p>
     * The {@code Observable} will not call this method again after it calls either
     * {@link #onComplete} or {@link #onError}.
     *
     * @param t the item emitted by the Observable
     */
    @Override
    void onNext(@androidx.annotation.NonNull T t);

    /**
     * Notifies the {@link Observer} that the {@link Observable} has experienced an error
     * condition.
     * <p>
     * If the {@code Observable} calls this method, it will not thereafter call {@link #onNext} or
     * {@link #onComplete}.
     *
     * @param e the exception encountered by the Observable
     */
    @Override
    @CallSuper
    default void onError(@NonNull Throwable e) {
        LogUtils.error("DefaultRxObserverError", getClass().getName() + ">>>onError>>>e=%s", LogUtils.getStackTraceAsString(e));
        e.printStackTrace();
    }

    /**
     * Notifies the {@link Observer} that the {@link Observable} has finished sending push-based
     * notifications.
     * <p>
     * The {@code Observable} will not call this method if it calls {@link #onError}.
     */
    @Override
    default void onComplete() {
    }
}
