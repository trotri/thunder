package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentStateNetworkStateBinding;
import com.trotri.android.java.sample.view.vm.StateNetworkStateViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.library.util.SingletonManager;

/**
 * StateNetworkStateFragment class file
 * 网络信息视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: StateNetworkStateFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class StateNetworkStateFragment extends BaseFragment<StateNetworkStateViewModel> {

    public static final String TAG = "StateNetworkStateFragment";

    public static BaseFragment newInstance() {
        return new StateNetworkStateFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_state_network_state;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentStateNetworkStateBinding.bind(view).setNetworkState(SingletonManager.getNetworkState());
    }

}
