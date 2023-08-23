package com.zhang.library.common.viewmodel;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.zhang.library.common.viewmodel.factory.ActivityVMFactory;
import com.zhang.library.common.viewmodel.factory.DialogFragmentVMFactory;
import com.zhang.library.common.viewmodel.factory.FragmentVMFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ViewModel创建器
 *
 * @author ZhangXiaoMing 2023-08-22 10:40 周二
 */
public class ViewModelCreator {

    /** 存储Provider的Map，使得一个Activity/Fragment中只有一个{@link ViewModelProvider} */
    private static final Map<LifecycleOwner, ViewModelProvider> mProviderMap = new HashMap<>();


    //<editor-fold desc="创建ViewModel">

    public static <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> modelClass) {
        ActivityVMFactory factory = new ActivityVMFactory(activity);
        ViewModelProvider provider = getActivityViewModelProvider(activity, factory);
        return provider.get(modelClass);
    }

    public static <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> modelClass) {
        FragmentVMFactory factory = new FragmentVMFactory(fragment);
        ViewModelProvider provider = getFragmentViewModelProvider(fragment, factory);
        return provider.get(modelClass);
    }

    public static <T extends ViewModel> T createViewModel(DialogFragment dialog, Class<T> modelClass) {
        DialogFragmentVMFactory factory = new DialogFragmentVMFactory(dialog);
        ViewModelProvider provider = getFragmentViewModelProvider(dialog, factory);
        return provider.get(modelClass);
    }

    //</editor-fold>


    /**
     * 获取Fragment的ViewModelProvider
     *
     * @param fragment fragment
     * @param factory  创建工厂类
     */
    private static ViewModelProvider getFragmentViewModelProvider(Fragment fragment, ViewModelProvider.Factory factory) {
        ViewModelProvider provider = mProviderMap.get(fragment);

        if (provider == null) {
            provider = new ViewModelProvider(fragment, factory);
            mProviderMap.put(fragment, provider);

            fragment.getLifecycle().addObserver(new DefaultLifecycleObserver() {
                @Override
                public void onDestroy(@NonNull LifecycleOwner owner) {
                    owner.getLifecycle().removeObserver(this);
                    mProviderMap.remove(owner);
                }
            });
        }

        return provider;
    }


    /**
     * 获取Activity的ViewModelProvider
     *
     * @param activity activity
     * @param factory  创建工厂类
     */
    private static ViewModelProvider getActivityViewModelProvider(FragmentActivity activity, ViewModelProvider.Factory factory) {
        ViewModelProvider provider = mProviderMap.get(activity);

        if (provider == null) {
            provider = new ViewModelProvider(activity, factory);
            mProviderMap.put(activity, provider);

            activity.getLifecycle().addObserver(new DefaultLifecycleObserver() {
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
