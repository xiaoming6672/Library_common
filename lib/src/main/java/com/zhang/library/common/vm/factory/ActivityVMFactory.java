package com.zhang.library.common.vm.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.zhang.library.common.activity.XMBaseActivity;
import com.zhang.library.common.vm.XMBaseViewModel;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;

/**
 * Activity中创建ViewModel的Factory
 *
 * @author ZhangXiaoMing 2023-08-06 19:50 周日
 */
public class ActivityVMFactory extends ViewModelProvider.NewInstanceFactory {


    protected WeakReference<XMBaseActivity> mActivity;

    public ActivityVMFactory(XMBaseActivity activity) {
        this.mActivity = new WeakReference<>(activity);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (XMBaseViewModel.class.isAssignableFrom(modelClass)) {
            try {
                Constructor<T> constructor = modelClass.getConstructor(XMBaseActivity.class);
                constructor.setAccessible(true);
                return constructor.newInstance(mActivity.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.create(modelClass);
    }
}
