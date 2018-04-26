package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebView;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentBrowserBinding;
import com.trotri.android.java.sample.view.vm.BrowserViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.rice.js.Explorer;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * BrowserFragment class file
 * 浏览器视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BrowserFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(BrowserViewModel.class)
public class BrowserFragment extends BaseFragment<BrowserViewModel> {

    private static final String TAG = "BrowserFragment";

    private static final String URL = "file:///android_asset/browser/demo.html";

    private FragmentBrowserBinding mDataBinding;

    public static BaseFragment newInstance() {
        return new BrowserFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_browser;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentBrowserBinding.bind(view);

        mDataBinding.eContent.onInitialize();

        mDataBinding.eContent.setWebViewClient(new Explorer.WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mDataBinding.tvReceivedError.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mDataBinding.srlayContainer.setRefreshing(false);
            }

        });

        mDataBinding.eContent.setWebChromeClient(new Explorer.WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 100) {
                    mDataBinding.pbarLoading.setVisibility(View.GONE);
                } else {
                    if (mDataBinding.pbarLoading.getVisibility() == View.GONE) {
                        mDataBinding.pbarLoading.setVisibility(View.VISIBLE);
                    }

                    if (newProgress > mDataBinding.pbarLoading.getProgress()) {
                        mDataBinding.pbarLoading.setProgress(newProgress);
                    }
                }

                super.onProgressChanged(view, newProgress);
            }

        });

        mDataBinding.eContent.setOnExtraScrollChangeListener(new Explorer.OnExtraScrollChangeListener() {
            @Override
            public void onScrollChanged(WebView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mDataBinding.srlayContainer.setEnabled(scrollY == 0);
            }

        });

        mDataBinding.eContent.loadUrl(URL);
    }

    @Override
    public void onStart() {
        super.onStart();

        mDataBinding.srlayContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataBinding.eContent.loadUrl(URL);
            }
        });

        mDataBinding.tvReceivedError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBinding.tvReceivedError.setVisibility(View.GONE);
                mDataBinding.eContent.loadUrl(URL);
            }
        });
    }

    @Override
    public void onDestroyView() {
        mDataBinding.eContent.onDestroy();

        super.onDestroyView();
    }

}
