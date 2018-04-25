package com.trotri.android.java.sample.view.vm;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.trotri.android.java.sample.data.bean.RegistryBean;
import com.trotri.android.library.App;
import com.trotri.android.library.base.BaseViewModel;
import com.trotri.android.thunder.ap.TypeCast;
import com.trotri.android.thunder.db.Registry;

/**
 * DbRegistryViewModel class file
 * thunder.db.Registry的ViewModel
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: DbRegistryViewModel.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class DbRegistryViewModel extends BaseViewModel {

    public static final String TAG = "DbRegistryViewModel";

    public static final String KEY_INT = "int";
    public static final String KEY_STRING = "string";
    public static final String KEY_LONG = "long";
    public static final String KEY_FLOAT = "float";
    public static final String KEY_BOOLEAN = "boolean";

    private Registry mRegistry;

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);

        mRegistry = new Registry(App.getContext());
    }

    public void save(@NonNull Bundle b, OnSaveListener listener) {
        String iValue = b.getString(KEY_INT, "");
        String lValue = b.getString(KEY_LONG, "");
        String fValue = b.getString(KEY_FLOAT, "");

        mRegistry.putInt(KEY_INT, TypeCast.toInt(iValue, 0));
        mRegistry.putString(KEY_STRING, b.getString(KEY_STRING, ""));
        mRegistry.putLong(KEY_LONG, TypeCast.toLong(lValue, 0));
        mRegistry.putFloat(KEY_FLOAT, TypeCast.toFloat(fValue, 0));
        mRegistry.putBoolean(KEY_BOOLEAN, b.getBoolean(KEY_BOOLEAN));

        refreshResult(listener);
    }

    public void refreshResult(OnSaveListener listener) {
        RegistryBean bean = new RegistryBean();

        bean.mIntValue = mRegistry.getInt(KEY_INT, 0);
        bean.mStrValue = mRegistry.getString(KEY_STRING, "");
        bean.mLongValue = mRegistry.getLong(KEY_LONG, 0);
        bean.mFloatValue = mRegistry.getFloat(KEY_FLOAT, 0);
        bean.mBoolValue = mRegistry.getBoolean(KEY_BOOLEAN, false);

        listener.onSave(bean);
    }

    public interface OnSaveListener {
        /**
         * 保存完成后回调方法
         */
        void onSave(RegistryBean bean);
    }

}
