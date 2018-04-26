package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentStateVersionBinding;
import com.trotri.android.java.sample.view.vm.StateVersionViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.library.util.SingletonManager;

/**
 * StateVersionFragment class file
 * 版本信息视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: StateVersionFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class StateVersionFragment extends BaseFragment<StateVersionViewModel> {

    public static final String TAG = "StateVersionFragment";

    public static BaseFragment newInstance() {
        return new StateVersionFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_state_version;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentStateVersionBinding.bind(view).setVersion(SingletonManager.getVersion());
    }

}
