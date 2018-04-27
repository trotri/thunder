package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.trotri.android.java.sample.MainActivity;
import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.data.bean.ButtonPanelBean;
import com.trotri.android.java.sample.databinding.FragmentBrowserInputBinding;
import com.trotri.android.java.sample.view.vm.BrowserInputViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.library.util.Cookie;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * BrowserInputFragment class file
 * 浏览器输入框视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BrowserInputFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(BrowserInputViewModel.class)
public class BrowserInputFragment extends BaseFragment<BrowserInputViewModel> {

    private static final String TAG = "BrowserInputFragment";

    public static final String COOKIE_KEY_URL = "URL";

    private FragmentBrowserInputBinding mDataBinding;

    public static BaseFragment newInstance() {
        return new BrowserInputFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_browser_input;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentBrowserInputBinding.bind(view);
        mDataBinding.etUrl.setText(Cookie.getInstance().getString(COOKIE_KEY_URL, ""));
    }

    @Override
    public void onStart() {
        super.onStart();

        mDataBinding.btnEnterUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString(BrowserFragment.ARG_URL, getUrl());

                MainActivity.actionStart(getActivity(), ButtonPanelBean.BROWSER, data);
            }
        });
    }

    /**
     * 从EditText中获取URL
     */
    private String getUrl() {
        String url = mDataBinding.etUrl.getText().toString();
        if (TextUtils.isEmpty(url)) {
            return "";
        }

        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        Cookie.getInstance().putString(COOKIE_KEY_URL, url);
        return url;
    }

}
