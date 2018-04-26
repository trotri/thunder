package com.trotri.android.java.sample.view.vm;

import android.databinding.ObservableField;

import com.trotri.android.library.base.BaseViewModel;
import com.trotri.android.library.util.SingletonManager;
import com.trotri.android.thunder.ap.TypeCast;
import com.trotri.android.thunder.util.DimensionConverter;

/**
 * UtilDimensionConverterViewModel class file
 * 单位转换ViewModel
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: StateContactsViewModel.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class UtilDimensionConverterViewModel extends BaseViewModel {

    public static final String TAG = "UtilDimensionConverterViewModel";

    public static final int TYPE_DP_TO_PX = 1;
    public static final int TYPE_PX_TO_DP = 2;
    public static final int TYPE_SP_TO_PX = 3;
    public static final int TYPE_PX_TO_SP = 4;

    public static final class Bean {
        public ObservableField<String> mDp2pxDp = new ObservableField<>();
        public ObservableField<String> mPx2dpPx = new ObservableField<>();
        public ObservableField<String> mSp2pxSp = new ObservableField<>();
        public ObservableField<String> mPx2spPx = new ObservableField<>();
    }

    public String convert(int type, String s) {
        DimensionConverter dimensionConverter = SingletonManager.getDimensionConverter();

        float value = TypeCast.toFloat(s, 0);
        switch (type) {
            case TYPE_DP_TO_PX:
                value = dimensionConverter.dp2px(value);
                break;
            case TYPE_PX_TO_DP:
                value = dimensionConverter.px2dp(value);
                break;
            case TYPE_PX_TO_SP:
                value = dimensionConverter.px2sp(value);
                break;
            case TYPE_SP_TO_PX:
                value = dimensionConverter.sp2px(value);
                break;
        }

        return String.valueOf(value);
    }

}
