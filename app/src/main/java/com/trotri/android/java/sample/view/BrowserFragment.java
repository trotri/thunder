package com.trotri.android.java.sample.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.TouchDelegate;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentBrowserBinding;
import com.trotri.android.java.sample.view.vm.BrowserViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.rice.js.Explorer;
import com.trotri.android.thunder.inj.ViewModelInject;
import com.trotri.android.thunder.view.ImmersiveHelper;

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

    private static final String DEFAULT_URL = "file:///android_asset/browser/demo.html";

    public static final String ARG_URL = "URL";

    /**
     * Callback JavaScript
     */
    public static final String JS_TITLEBAR_ONCLICK_NAME = "javascript:TitleBar.onClick";

    /**
     * 后退按钮
     */
    private static final String TYPE_BACKWARD = "backward";

    /**
     * 菜单按钮
     */
    private static final String TYPE_MENUS = "menus";

    private String mUrl;

    private FragmentBrowserBinding mDataBinding;

    public static BaseFragment newInstance(Bundle data) {
        BaseFragment f = new BrowserFragment();

        f.setArguments(data);
        return f;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_browser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersiveHelper.setImmersive(getActivity());

        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
        }

        if (TextUtils.isEmpty(mUrl)) {
            mUrl = DEFAULT_URL;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentBrowserBinding.bind(view);

        ImmersiveHelper.setTitleBar(getActivity(), mDataBinding.titleBar.flayTitleBar);

        mDataBinding.eContent.onInitialize();

        mDataBinding.eContent.setWebViewClient(new Explorer.WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int code, String desc, String url) {
                super.onReceivedError(view, code, desc, url);
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
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mDataBinding.titleBar.tvTitle.setText(title);
            }

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

        mDataBinding.eContent.loadUrl(mUrl);

        setTouchDelegate(mDataBinding.titleBar.btnBackward);
        setTouchDelegate(mDataBinding.titleBar.btnMenus);
    }

    @Override
    public void onStart() {
        super.onStart();

        mDataBinding.srlayContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataBinding.eContent.loadUrl(mUrl);
            }
        });

        mDataBinding.tvReceivedError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBinding.tvReceivedError.setVisibility(View.GONE);
                mDataBinding.eContent.loadUrl(mUrl);
            }
        });

        mDataBinding.titleBar.btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleBarClick(TYPE_BACKWARD);
            }
        });

        mDataBinding.titleBar.btnMenus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleBarClick(TYPE_MENUS);
            }
        });
    }

    /**
     * 通知标题栏点击
     *
     * @param type 点击类型
     */
    private void onTitleBarClick(String type) {
        String code = JS_TITLEBAR_ONCLICK_NAME + "('" + type + "')";
        mDataBinding.eContent.getJsBridge().loadJs(code);
    }

    /**
     * 扩大按钮点击范围
     *
     * @param view 按钮
     */
    private void setTouchDelegate(final Button view) {
        if (!View.class.isInstance(view.getParent())) {
            return;
        }

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                float parentHeight = getResources().getDimension(R.dimen.titlebar_height);
                float buttonHeight = getResources().getDimension(R.dimen.titlebar_button_height);
                float region = parentHeight - buttonHeight;

                Rect rect = new Rect();
                view.setEnabled(true);
                view.getHitRect(rect);

                rect.top -= region;
                rect.bottom += region;
                rect.left -= region;
                rect.right += region;

                TouchDelegate delegate = new TouchDelegate(rect, view);
                ((View) view.getParent()).setTouchDelegate(delegate);
            }
        });
    }

    @Override
    public void onDestroyView() {
        mDataBinding.eContent.onDestroy();

        super.onDestroyView();
    }

}
