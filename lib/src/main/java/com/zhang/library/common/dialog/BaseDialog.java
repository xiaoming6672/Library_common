package com.zhang.library.common.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zhang.library.common.R;
import com.zhang.library.utils.CollectionUtils;
import com.zhang.library.utils.LogUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 父类Dialog，继承自{@link DialogFragment}
 *
 * @author ZhangXiaoMing 2023-08-08 17:12 周二
 */
public abstract class BaseDialog extends AppCompatDialogFragment implements View.OnClickListener {

    protected final String TAG = getClass().getSimpleName();

    /** 数据是否已初始化 */
    protected boolean isDataInitialed;

    private final List<DialogCallback> mCallbackList;

    {
        mCallbackList = new ArrayList<>();
    }


    protected <T extends BaseDialog> BaseDialog(DialogBuilder<T> builder) {
        Bundle bundle = builder.getBundle();
        if (!bundle.isEmpty())
            setArguments(bundle);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.debug(TAG, "onCreate()");

        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, R.style.DialogStyle);

        onInitLogicComponent();

        Bundle bundle = getArguments();
        if (bundle != null)
            onParseArgument(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.debug(TAG, "onCreateView()");
        return inflater.inflate(getContentLayoutId(), container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog createDialog = super.onCreateDialog(savedInstanceState);
        createDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    return onBackPressed();
                }
            }

            return false;
        });
        return createDialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtils.debug(TAG, "onViewCreated()");

        super.onViewCreated(view, savedInstanceState);

        onInitView(view);
        onInitListener(view);

        onPreInitData();
    }

    @Override
    public void onStart() {
        LogUtils.debug(TAG, "onStart()");

        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            onInitDialogWindow(dialog.getWindow());
        }
    }

    @Override
    public void onResume() {
        LogUtils.debug(TAG, "onResume()");

        super.onResume();

        if (!isDataInitialed) {
            LogUtils.debug(TAG, "onResume()>>>onInitData");
            onInitData();
            isDataInitialed = true;
        }

        onDialogShown();
    }

    @Override
    public void onDestroyView() {
        LogUtils.debug(TAG, "onDestroyView()");

        super.onDestroyView();

        isDataInitialed = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCallbackList.clear();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        LogUtils.debug(TAG, "onDismiss()");

        super.onDismiss(dialog);

        notifyDialogDismiss(dialog);
    }

    @Override
    @CallSuper
    public void onClick(View v) {
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
//        super.show(manager, tag);

        try {
            //由于父类方法中mDismissed，mShownByMe不可直接访问，所以此处采用反射修改他们的值
            Class<DialogFragment> clazz = DialogFragment.class;
            Field mDismissed = clazz.getDeclaredField("mDismissed");
            mDismissed.setAccessible(true);
            mDismissed.set(this, false);

            Field mShownByMe = clazz.getDeclaredField("mShownByMe");
            mShownByMe.setAccessible(true);
            mShownByMe.set(this, true);

            FragmentTransaction ft = manager.beginTransaction();
            ft.setReorderingAllowed(true);
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
        try {
            //由于父类方法中mDismissed，mShownByMe不可直接访问，所以此处采用反射修改他们的值
            Class<DialogFragment> clazz = DialogFragment.class;
            Field mDismissed = clazz.getDeclaredField("mDismissed");
            mDismissed.setAccessible(true);
            mDismissed.set(this, false);

            Field mShownByMe = clazz.getDeclaredField("mShownByMe");
            mShownByMe.setAccessible(true);
            mShownByMe.set(this, true);

            transaction.add(this, tag);

            Field mViewDestroyed = clazz.getDeclaredField("mViewDestroyed");
            mViewDestroyed.setAccessible(true);
            mViewDestroyed.set(this, false);

            int backStackId = transaction.commitAllowingStateLoss();

            Field mBackStackId = clazz.getDeclaredField("mBackStackId");
            mBackStackId.setAccessible(true);
            mBackStackId.set(this, backStackId);

            return backStackId;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.show(transaction, tag);
    }


    /**
     * 显示弹窗，默认tag为{@link Class#getName()}
     *
     * @param manager {@link FragmentManager}
     */
    public void show(@NonNull FragmentManager manager) {
        show(manager, getClass().getName());
    }

    /**
     * 显示弹窗，默认tag为{@link Class#getName()}
     *
     * @param transaction {@link FragmentTransaction}
     */
    public void show(@NonNull FragmentTransaction transaction) {
        show(transaction, getClass().getName());
    }

    //<editor-fold desc="初始化窗口大小">

    /**
     * 初始化窗口大小
     *
     * @param window 窗口
     */
    protected void onInitDialogWindow(@NonNull Window window) {
        window.setBackgroundDrawableResource(R.color.transparent);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = getWindowWidth();
        lp.height = getWindowHeight();
        lp.gravity = getWindowGravity();
        window.setAttributes(lp);
    }

    /** 设定的window的宽度 */
    protected int getWindowWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    /** 设定的window的高度 */
    protected int getWindowHeight() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    /** 设定的window的显示位置 */
    protected int getWindowGravity() {
        return Gravity.CENTER;
    }
    //</editor-fold>

    //<editor-fold desc="弹窗回调通知">

    /** 添加DialogCallback回调 */
    @SuppressWarnings("unchecked")
    public <T extends BaseDialog> T addDialogCallback(DialogCallback callback) {
        if (mCallbackList.contains(callback))
            return (T) this;

        mCallbackList.add(callback);
        return (T) this;
    }

    /** 移除DialogCallback回调 */
    public <T extends BaseDialog> T removeDialogCallback(DialogCallback callback) {
        mCallbackList.remove(callback);
        //noinspection unchecked
        return (T) this;
    }

    /** 通知弹窗显示 */
    private void notifyDialogShow() {
        if (!CollectionUtils.isEmpty(mCallbackList)) {
            for (DialogCallback callback : mCallbackList) {
                callback.onShow();
            }
        }
    }

    /** 通知弹窗关闭消失 */
    protected void notifyDialogDismiss(DialogInterface dialogInterface) {
        if (!CollectionUtils.isEmpty(mCallbackList)) {
            for (DialogCallback callback : mCallbackList) {
                callback.onDismiss(dialogInterface);
            }
        }
    }

    /** 通知弹窗确认回调 */
    protected void notifyOnConfirmCallback() {
        if (!CollectionUtils.isEmpty(mCallbackList)) {
            for (DialogCallback callback : mCallbackList) {
                callback.onConfirm();
            }
        }
    }

    /** 通知弹窗确认回调 */
    protected void notifyOnConfirmCallback(@NonNull Bundle bundle) {
        if (!CollectionUtils.isEmpty(mCallbackList)) {
            for (DialogCallback callback : mCallbackList) {
                callback.onConfirm(bundle);
            }
        }
    }

    /** 通知弹窗取消回调 */
    protected void notifyOnCancelCallback() {
        if (!CollectionUtils.isEmpty(mCallbackList)) {
            for (DialogCallback callback : mCallbackList) {
                callback.onCancel();
            }
        }
    }

    /** 通知弹窗取消回调 */
    protected void notifyOnCancelCallback(@NonNull Bundle bundle) {
        if (!CollectionUtils.isEmpty(mCallbackList)) {
            for (DialogCallback callback : mCallbackList) {
                callback.onCancel(bundle);
            }
        }
    }
    //</editor-fold>


    /**
     * 解析Bundle参数
     *
     * @param bundle {@link #setArguments(Bundle)}传递的参数
     */
    protected void onParseArgument(@NonNull Bundle bundle) {
    }

    /** 初始化逻辑类对象 */
    protected abstract void onInitLogicComponent();

    @LayoutRes
    protected abstract int getContentLayoutId();

    /**
     * 控件初始化
     *
     * @param view {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}生成的view
     */
    protected abstract void onInitView(@NonNull View view);

    /**
     * 初始化监听器
     *
     * @param view {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}生成的view
     */
    protected abstract void onInitListener(@NonNull View view);

    /** 提前初始化数据 */
    protected void onPreInitData() {
    }

    /** 初始化数据 */
    protected abstract void onInitData();

    /** 弹窗显示 */
    @CallSuper
    protected void onDialogShown() {
        notifyDialogShow();
    }

    /** 点击后退按钮 */
    protected boolean onBackPressed() {
        return !isCancelable();
    }


    public interface DialogCallback {
        /** 弹窗显示 */
        default void onShow() {
        }

        /** 弹窗消失 */
        default void onDismiss(DialogInterface dialogInterface) {
        }

        /** 点击确认，无参 */
        default void onConfirm() {
        }

        /**
         * 点击确认，有参
         *
         * @param bundle 附带参数
         */
        default void onConfirm(@NonNull Bundle bundle) {
        }

        /** 点击取消，无参 */
        default void onCancel() {
        }

        /**
         * 点击取消，有参
         *
         * @param bundle 附带参数
         */
        default void onCancel(@NonNull Bundle bundle) {
        }
    }

    public static class DialogBuilder<T extends BaseDialog> {

        private final Class<T> mClass;
        private final Bundle mBundle;


        private DialogBuilder(Class<T> clazz) {
            mClass = clazz;
            mBundle = new Bundle();
        }

        /**
         * 构建
         *
         * @param clazz 弹窗类
         * @param <R>   继承于{@link BaseDialog}的子类
         */
        public static <R extends BaseDialog> DialogBuilder<R> newBuilder(Class<R> clazz) {
            return new DialogBuilder<>(clazz);
        }

        /**
         * 添加参数，默认key为{@link Class#getName()}
         *
         * @param value 存储的数据
         * @param <V>   数据类型
         */
        public <V> DialogBuilder<T> put(V value) {
            return put(value.getClass().getName(), value);
        }

        /**
         * 添加参数
         *
         * @param key   存储的key
         * @param value 存储的数据
         * @param <V>   数据类型
         */
        public <V> DialogBuilder<T> put(String key, V value) {
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
        public DialogBuilder<T> put(@NonNull Bundle bundle) {
            mBundle.putAll(bundle);
            return this;
        }

        /** 获取参数合集 */
        @NonNull
        public Bundle getBundle() {
            return mBundle;
        }

        /** 创建弹窗 */
        @NonNull
        public T create() {
            T dialog;
            try {
                Constructor<T> constructor = mClass.getDeclaredConstructor(DialogBuilder.class);
                constructor.setAccessible(true);
                dialog = constructor.newInstance(DialogBuilder.this);
            } catch (Exception e) {
                e.printStackTrace();
                dialog = createDefaultDialog();
            }

            return dialog;
        }

        /** 创建默认弹窗 */
        @SuppressWarnings("unchecked")
        private T createDefaultDialog() {
            return (T) new BaseDialog(this) {
                /** 初始化逻辑类对象 */
                @Override
                protected void onInitLogicComponent() {
                }

                @Override
                protected int getContentLayoutId() {
                    return 0;
                }

                /**
                 * 控件初始化
                 *
                 * @param view {@link #onCreateView(LayoutInflater, ViewGroup,
                 *             Bundle)}生成的view
                 */
                @Override
                protected void onInitView(@NonNull View view) {
                }

                /**
                 * 初始化监听器
                 *
                 * @param view {@link #onCreateView(LayoutInflater, ViewGroup,
                 *             Bundle)}生成的view
                 */
                @Override
                protected void onInitListener(@NonNull View view) {
                }

                /** 初始化数据 */
                @Override
                protected void onInitData() {
                }
            };
        }
    }
}
