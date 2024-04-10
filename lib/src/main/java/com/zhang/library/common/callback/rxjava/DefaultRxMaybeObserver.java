package com.zhang.library.common.callback.rxjava;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 默认空实现的{@link Maybe}的观察者
 *
 * @author ZhangXiaoMing 2024-04-10 11:15 周三
 */
public interface DefaultRxMaybeObserver<T> extends MaybeObserver<T> {

    /**
     * Provides the {@link MaybeObserver} with the means of cancelling (disposing) the connection
     * (channel) with the {@link Maybe} in both synchronous (from within
     * {@code onSubscribe(Disposable)} itself) and asynchronous manner.
     *
     * @param d the {@link Disposable} instance whose {@link Disposable#dispose()} can be called
     *          anytime to cancel the connection
     */
    @Override
    default void onSubscribe(@NonNull Disposable d) {
    }

//    /**
//     * Notifies the {@link MaybeObserver} with one item and that the {@link Maybe} has finished
//     * sending push-based notifications.
//     * <p>
//     * The {@link Maybe} will not call this method if it calls {@link #onError}.
//     *
//     * @param t the item emitted by the {@code Maybe}
//     */
//    @Override
//    default void onSuccess(@NonNull T t) {
//    }

    /**
     * Notifies the {@link MaybeObserver} that the {@link Maybe} has experienced an error
     * condition.
     * <p>
     * If the {@link Maybe} calls this method, it will not thereafter call {@link #onSuccess}.
     *
     * @param e the exception encountered by the {@code Maybe}
     */
    @Override
    default void onError(@NonNull Throwable e) {
    }

    /**
     * Called once the deferred computation completes normally.
     */
    @Override
    default void onComplete() {
    }
}
