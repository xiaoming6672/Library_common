package com.zhang.library.common.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.zhang.library.common.viewmodel.BaseViewModel;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;

/**
 * Fragment中创建ViewModel时候使用的Factory
 *
 * @author ZhangXiaoMing 2023-08-06 20:17 周日
 */
public class FragmentVMFactory extends ViewModelProvider.NewInstanceFactory {

    protected WeakReference<Fragment> mFragment;

    public FragmentVMFactory(Fragment fragment) {
        this.mFragment = new WeakReference<>(fragment);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (BaseViewModel.class.isAssignableFrom(modelClass)) {
            try {
                Constructor<T> constructor = modelClass.getConstructor(Fragment.class);
                constructor.setAccessible(true);
                return constructor.newInstance(mFragment.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.create(modelClass);
    }
}
