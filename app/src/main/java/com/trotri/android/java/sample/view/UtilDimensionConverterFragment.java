package com.trotri.android.java.sample.view;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentUtilDimensionConverterBinding;
import com.trotri.android.java.sample.view.vm.UtilDimensionConverterViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * UtilDimensionConverterFragment class file
 * 单位转换视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: UtilDimensionConverterFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(UtilDimensionConverterViewModel.class)
public class UtilDimensionConverterFragment extends BaseFragment<UtilDimensionConverterViewModel> {

    public static final String TAG = "UtilDimensionConverterFragment";

    private FragmentUtilDimensionConverterBinding mDataBinding;
    private UtilDimensionConverterViewModel.Bean mBean;

    public static BaseFragment newInstance() {
        return new UtilDimensionConverterFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_util_dimension_converter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentUtilDimensionConverterBinding.bind(view);
        mBean = new UtilDimensionConverterViewModel.Bean();
    }

    @Override
    public void onStart() {
        super.onStart();

        mDataBinding.setBean(mBean);

        mBean.mDp2pxDp.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mDataBinding.tvDp2pxPx.setText(mViewModel.convert(UtilDimensionConverterViewModel.TYPE_DP_TO_PX, mBean.mDp2pxDp.get()));
            }
        });

        mBean.mPx2dpPx.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mDataBinding.tvPx2dpDp.setText(mViewModel.convert(UtilDimensionConverterViewModel.TYPE_PX_TO_DP, mBean.mPx2dpPx.get()));
            }
        });

        mBean.mSp2pxSp.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mDataBinding.tvSp2pxPx.setText(mViewModel.convert(UtilDimensionConverterViewModel.TYPE_SP_TO_PX, mBean.mSp2pxSp.get()));
            }
        });

        mBean.mPx2spPx.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mDataBinding.tvPx2spSp.setText(mViewModel.convert(UtilDimensionConverterViewModel.TYPE_PX_TO_SP, mBean.mPx2spPx.get()));
            }
        });

    }

}
