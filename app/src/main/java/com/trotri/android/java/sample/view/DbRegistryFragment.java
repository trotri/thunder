package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.data.bean.RegistryBean;
import com.trotri.android.java.sample.databinding.FragmentDbRegistryBinding;
import com.trotri.android.java.sample.view.vm.DbRegistryViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * DbRegistryFragment class file
 * thunder.db.Registry视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: DbRegistryFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(DbRegistryViewModel.class)
public class DbRegistryFragment extends BaseFragment<DbRegistryViewModel> {

    private FragmentDbRegistryBinding mDataBinding;

    public static BaseFragment newInstance() {
        return new DbRegistryFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_db_registry;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentDbRegistryBinding.bind(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        mViewModel.refreshResult(new DbRegistryViewModel.OnSaveListener() {
            @Override
            public void onSave(RegistryBean bean) {
                mDataBinding.incRegistry.setBean(bean);
            }
        });

        mDataBinding.incRegistry.btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();

                b.putString(DbRegistryViewModel.KEY_INT, mDataBinding.incRegistry.etRegistryInt.getText().toString());
                b.putString(DbRegistryViewModel.KEY_STRING, mDataBinding.incRegistry.etRegistryString.getText().toString());
                b.putString(DbRegistryViewModel.KEY_LONG, mDataBinding.incRegistry.etRegistryLong.getText().toString());
                b.putString(DbRegistryViewModel.KEY_FLOAT, mDataBinding.incRegistry.etRegistryFloat.getText().toString());
                b.putBoolean(DbRegistryViewModel.KEY_BOOLEAN, mDataBinding.incRegistry.rbRegistryBooleanTrue.isChecked());

                mViewModel.save(b, new DbRegistryViewModel.OnSaveListener() {
                    @Override
                    public void onSave(RegistryBean bean) {
                        mDataBinding.incRegistry.setBean(bean);
                    }
                });
            }
        });
    }

}
