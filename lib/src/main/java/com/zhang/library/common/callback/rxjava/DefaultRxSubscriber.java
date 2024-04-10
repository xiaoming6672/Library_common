package com.zhang.library.common.callback.rxjava;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.core.Flowable;

/**
 * 默认空实现的{@link Flowable}观察者
 *
 * @author ZhangXiaoMing 2024-04-10 11:19 周三
 */
public interface DefaultRxSubscriber<T> extends Subscriber<T> {

    /**
     * Invoked after calling {@link Publisher#subscribe(Subscriber)}.
     * <p>
     * No data will start flowing until {@link Subscription#request(long)} is invoked.
     * <p>
     * It is the responsibility of this {@link Subscriber} instance to call
     * {@link Subscription#request(long)} whenever more data is wanted.
     * <p>
     * The {@link Publisher} will send notifications only in response to
     * {@link Subscription#request(long)}.
     *
     * @param s the {@link Subscription} that allows requesting data via
     *          {@link Subscription#request(long)}
     */
    @Override
    default void onSubscribe(Subscription s) {
    }

//    /**
//     * Data notification sent by the {@link Publisher} in response to requests to
//     * {@link Subscription#request(long)}.
//     *
//     * @param o the element signaled
//     */
//    @Override
//    default void onNext(T o) {
//    }

    /**
     * Failed terminal state.
     * <p>
     * No further events will be sent even if {@link Subscription#request(long)} is invoked again.
     *
     * @param t the throwable signaled
     */
    @Override
    default void onError(Throwable t) {
    }

    /**
     * Successful terminal state.
     * <p>
     * No further events will be sent even if {@link Subscription#request(long)} is invoked again.
     */
    @Override
    default void onComplete() {
    }
}
