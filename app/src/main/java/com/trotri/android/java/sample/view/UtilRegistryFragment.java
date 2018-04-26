package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.data.bean.RegistryBean;
import com.trotri.android.java.sample.databinding.FragmentUtilRegistryBinding;
import com.trotri.android.java.sample.view.vm.UtilRegistryViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * UtilRegistryFragment class file
 * thunder.util.Registry视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: UtilRegistryFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(UtilRegistryViewModel.class)
public class UtilRegistryFragment extends BaseFragment<UtilRegistryViewModel> {

    public static final String TAG = "UtilRegistryFragment";

    private FragmentUtilRegistryBinding mDataBinding;

    public static BaseFragment newInstance() {
        return new UtilRegistryFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_util_registry;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentUtilRegistryBinding.bind(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        mViewModel.refreshResult(new UtilRegistryViewModel.OnSaveListener() {
            @Override
            public void onSave(RegistryBean bean) {
                mDataBinding.incRegistry.setBean(bean);
            }
        });

        mDataBinding.incRegistry.btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();

                b.putString(UtilRegistryViewModel.KEY_INT, mDataBinding.incRegistry.etRegistryInt.getText().toString());
                b.putString(UtilRegistryViewModel.KEY_STRING, mDataBinding.incRegistry.etRegistryString.getText().toString());
                b.putString(UtilRegistryViewModel.KEY_LONG, mDataBinding.incRegistry.etRegistryLong.getText().toString());
                b.putString(UtilRegistryViewModel.KEY_FLOAT, mDataBinding.incRegistry.etRegistryFloat.getText().toString());
                b.putBoolean(UtilRegistryViewModel.KEY_BOOLEAN, mDataBinding.incRegistry.rbRegistryBooleanTrue.isChecked());

                mViewModel.save(b, new UtilRegistryViewModel.OnSaveListener() {
                    @Override
                    public void onSave(RegistryBean bean) {
                        mDataBinding.incRegistry.setBean(bean);
                    }
                });
            }
        });
    }

}
