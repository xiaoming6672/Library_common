package com.zhang.library.common.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.zhang.library.common.viewmodel.BaseViewModel;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;

/**
 * DialogFragment创建ViewModel使用的Factory类
 *
 * @author ZhangXiaoMing 2023-08-03 10:25 周四
 */
public class DialogFragmentVMFactory extends ViewModelProvider.NewInstanceFactory {

    private final WeakReference<DialogFragment> mDialog;

    public DialogFragmentVMFactory(DialogFragment dialog) {
        this.mDialog = new WeakReference<>(dialog);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            if (BaseViewModel.class.isAssignableFrom(modelClass)) {
                Constructor<T> constructor = modelClass.getConstructor(DialogFragment.class);
                constructor.setAccessible(true);
                return constructor.newInstance(mDialog.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.create(modelClass);
    }
}
