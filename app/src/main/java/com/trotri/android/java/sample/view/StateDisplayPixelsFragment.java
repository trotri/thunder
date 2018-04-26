package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentStateDisplayPixelsBinding;
import com.trotri.android.java.sample.view.vm.StateDisplayPixelsViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.library.util.SingletonManager;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * StateDisplayPixelsFragment class file
 * 屏幕信息视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: StateDisplayPixelsFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(StateDisplayPixelsViewModel.class)
public class StateDisplayPixelsFragment extends BaseFragment<StateDisplayPixelsViewModel> {

    public static BaseFragment newInstance() {
        return new StateDisplayPixelsFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_state_display_pixels;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentStateDisplayPixelsBinding.bind(view).setDisplayPixels(SingletonManager.getDisplayPixels());
    }

}
