package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentErrorBinding;
import com.trotri.android.java.sample.view.vm.ErrorViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * ErrorFragment class file
 * 错误信息视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ErrorFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(ErrorViewModel.class)
public class ErrorFragment extends BaseFragment<ErrorViewModel> {

    public static final String KEY_TIPS = "tips";

    public static ErrorFragment newInstance(String tips) {
        ErrorFragment f = new ErrorFragment();

        Bundle b = new Bundle();
        b.putString(KEY_TIPS, tips);

        f.setArguments(b);
        return f;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_error;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentErrorBinding.bind(view).setTips(getArguments().getString(KEY_TIPS));
    }

}
