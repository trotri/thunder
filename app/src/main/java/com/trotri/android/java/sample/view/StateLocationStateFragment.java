package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentStateLocationStateBinding;
import com.trotri.android.java.sample.view.vm.StateLocationStateViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.library.util.SingletonManager;
import com.trotri.android.thunder.inj.ViewModelInject;
import com.trotri.android.thunder.state.LocationState;

/**
 * StateLocationStateFragment class file
 * 位置信息视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: StateLocationStateFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(StateLocationStateViewModel.class)
public class StateLocationStateFragment extends BaseFragment<StateLocationStateViewModel> {

    private FragmentStateLocationStateBinding mDataBinding;

    public static BaseFragment newInstance() {
        return new StateLocationStateFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_state_location_state;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentStateLocationStateBinding.bind(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        mDataBinding.setLocationState(SingletonManager.getLocationState());
        mDataBinding.setView(this);
    }

    public void toSetting() {
        LocationState.toSetting(getActivity());
    }

}
