package androidx.lifecycle;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.zhang.library.common.viewmodel.BaseViewModel;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel创建器
 *
 * @author ZhangXiaoMing 2023-10-12 18:13 周四
 */
@Keep
public class ViewModelCreator<T extends ViewModel> {

    /** 存储Provider的Map，使得一个Activity/Fragment中只有一个{@link ViewModelProvider} */
    private static final Map<LifecycleOwner, ViewModelProvider> mProviderMap = new HashMap<>();


    @NonNull
    private final LifecycleOwner mLifecycleOwner;
    @NonNull
    private final ViewModelStoreOwner mStoreOwner;
    @NonNull
    private final Class<T> mModelClass;
    @NonNull
    private final Bundle mBundle;


    public ViewModelCreator(@NonNull LifecycleOwner lifecycleOwner, @NonNull ViewModelStoreOwner storeOwner, @NonNull Class<T> modelClass) {
        this.mLifecycleOwner = lifecycleOwner;
        this.mStoreOwner = storeOwner;
        this.mModelClass = modelClass;
        this.mBundle = new Bundle();
    }


    /**
     * 新建生成器
     *
     * @param lifecycleOwner 生命周期所有者
     * @param storeOwner     ViewModel存储所有者
     * @param clazz          类
     * @param <T>            继承{@link ViewModel}的子类
     */
    public static <T extends ViewModel> ViewModelCreator<T> newBuilder(@NonNull LifecycleOwner lifecycleOwner,
                                                                       @NonNull ViewModelStoreOwner storeOwner,
                                                                       @NonNull Class<T> clazz) {
        return new ViewModelCreator<>(lifecycleOwner, storeOwner, clazz);
    }

    /**
     * 添加参数，默认key为{@link Class#getName()}
     *
     * @param value 存储的数据
     * @param <V>   数据类型
     */
    public <V> ViewModelCreator<T> put(@NonNull V value) {
        return put(value.getClass().getName(), value);
    }

    /**
     * 添加参数
     *
     * @param key   存储的key
     * @param value 存储的数据
     * @param <V>   数据类型
     */
    public <V> ViewModelCreator<T> put(@NonNull String key, @NonNull V value) {
        if (value instanceof String)
            mBundle.putString(key, (String) value);
        else if (value instanceof CharSequence)
            mBundle.putCharSequence(key, (CharSequence) value);
        else if (value instanceof Integer)
            mBundle.putInt(key, (Integer) value);
        else if (value instanceof Long)
            mBundle.putLong(key, (Long) value);
        else if (value instanceof Float)
            mBundle.putFloat(key, (Float) value);
        else if (value instanceof Double)
            mBundle.putDouble(key, (Double) value);
        else if (value instanceof Character)
            mBundle.putChar(key, (Character) value);
        else if (value instanceof Boolean)
            mBundle.putBoolean(key, (Boolean) value);
        else if (value instanceof Serializable)
            mBundle.putSerializable(key, (Serializable) value);
        else if (value instanceof Parcelable)
            mBundle.putParcelable(key, (Parcelable) value);

        return this;
    }

    /**
     * 添加参数
     *
     * @param bundle 参数合集
     */
    public ViewModelCreator<T> put(@NonNull Bundle bundle) {
        mBundle.putAll(bundle);
        return this;
    }

    @NonNull
    public LifecycleOwner getLifecycleOwner() {
        return mLifecycleOwner;
    }

    @NonNull
    public ViewModelStoreOwner getStoreOwner() {
        return mStoreOwner;
    }

    @NonNull
    public Bundle getBundle() {
        return mBundle;
    }

    /** 创建ViewModel实例 */
    public T create() {
        ViewModelProvider provider = getViewModelProvider();
        return provider.get(mModelClass);
    }

    /** 获取{@link ViewModelProvider} */
    private ViewModelProvider getViewModelProvider() {
        ViewModelProvider provider = mProviderMap.get(mLifecycleOwner);

        if (provider == null) {
            provider = new ViewModelProvider(mStoreOwner, new ViewModelProvider.Factory() {
                @NonNull
                @Override
                public <VM extends ViewModel> VM create(@NonNull Class<VM> modelClass) {
                    if (BaseViewModel.class.isAssignableFrom(modelClass)) {
                        try {
                            Constructor<VM> constructor = modelClass.getDeclaredConstructor(ViewModelCreator.class);
                            constructor.setAccessible(true);
                            return constructor.newInstance(ViewModelCreator.this);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return ViewModelProvider.NewInstanceFactory.getInstance().create(modelClass);
                }
            });
            mProviderMap.put(mLifecycleOwner, provider);

            mLifecycleOwner.getLifecycle().addObserver(new DefaultLifecycleObserver() {
                @Override
                public void onDestroy(@NonNull LifecycleOwner owner) {
                    owner.getLifecycle().removeObserver(this);
                    mProviderMap.remove(owner);
                }
            });
        }

        return provider;
    }

}
