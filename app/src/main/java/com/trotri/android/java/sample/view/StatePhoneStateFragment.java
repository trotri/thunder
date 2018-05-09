package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentStatePhoneStateBinding;
import com.trotri.android.java.sample.view.vm.StatePhoneStateViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.library.util.SingletonManager;

/**
 * StatePhoneStateFragment class file
 * 手机信息视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: StatePhoneStateFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class StatePhoneStateFragment extends BaseFragment<StatePhoneStateViewModel> {

    public static final String TAG = "StatePhoneStateFragment";

    public static BaseFragment newInstance() {
        return new StatePhoneStateFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_state_phone_state;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentStatePhoneStateBinding.bind(view).setPhoneState(SingletonManager.getPhoneState());
    }

}
