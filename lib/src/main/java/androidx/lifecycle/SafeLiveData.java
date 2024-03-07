package androidx.lifecycle;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册新观察者的时候不会引发数据倒灌的LiveData
 *
 * <br><a href="https://www.jianshu.com/p/984ffed95fc1">参考链接</a>
 *
 * @author ZhangXiaoMing 2023-09-04 17:45 周一
 */
public class SafeLiveData<T> extends MutableLiveData<T> {


    private final List<SafeObserver<? super T>> mForeverObserverList;


    /**
     * Creates a MutableLiveData initialized with the given {@code value}.
     *
     * @param value initial value
     */
    public SafeLiveData(T value) {
        super(value);
        mForeverObserverList = new ArrayList<>();
    }

    /**
     * Creates a MutableLiveData with no value assigned to it.
     */
    public SafeLiveData() {
        mForeverObserverList = new ArrayList<>();
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        // 直接可以通过this.version获取到版本号

        SafeObserver<? super T> safeObserver = new SafeObserver<>(observer, getVersion() > START_VERSION);
        super.observe(owner, safeObserver);
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        SafeObserver<? super T> safeObserver = new SafeObserver<>(observer, getVersion() > START_VERSION);
        mForeverObserverList.add(safeObserver);
        super.observeForever(safeObserver);
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        boolean foreverObserver = isForeverObserver(observer);
        if (foreverObserver) {
            SafeObserver<? super T> safeObserver = findSafeObserver(observer);
            super.removeObserver(safeObserver != null ? safeObserver : observer);

            mForeverObserverList.remove(safeObserver);
            return;
        }

        super.removeObserver(observer);
    }


    /**
     * 是否是{@link #observeForever(Observer)}注册的观察者
     *
     * @param observer 观察者
     */
    private boolean isForeverObserver(@NonNull Observer<? super T> observer) {
        for (SafeObserver<?> item : mForeverObserverList) {
            if (item.mRealObserver != observer)
                continue;

            return true;
        }

        return false;
    }

    /**
     * 找到{@link #observeForever(Observer)}注册生成的{@link  SafeObserver}
     *
     * @param observer 观察者
     */
    private SafeObserver<? super T> findSafeObserver(@NonNull Observer<? super T> observer) {
        for (SafeObserver<? super T> item : mForeverObserverList) {
            if (item.mRealObserver != observer)
                continue;

            return item;
        }

        return null;
    }


    static class SafeObserver<T> implements Observer<T> {

        private final Observer<T> mRealObserver;
        private boolean preventDispatch;

        public SafeObserver(Observer<T> observer) {
            this.mRealObserver = observer;
            preventDispatch = false;
        }

        public SafeObserver(Observer<T> observer, boolean preventDispatch) {
            this.mRealObserver = observer;
            this.preventDispatch = preventDispatch;
        }

        @Override
        public void onChanged(T t) {
            // 如果版本有差异，第一次不处理
            if (preventDispatch) {
                preventDispatch = false;
                return;
            }

            mRealObserver.onChanged(t);
        }
    }

}
