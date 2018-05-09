package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.view.vm.TestViewModel;
import com.trotri.android.library.base.BaseFragment;

/**
 * TestFragment class file
 * 测试视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: TestFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class TestFragment extends BaseFragment<TestViewModel> {

    public static final String TAG = "TestFragment";

    public static BaseFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
