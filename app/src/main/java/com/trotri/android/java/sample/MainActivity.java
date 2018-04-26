package com.trotri.android.java.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.trotri.android.java.sample.data.bean.ButtonPanelBean;
import com.trotri.android.java.sample.view.BookDetailFragment;
import com.trotri.android.java.sample.view.BookListFragment;
import com.trotri.android.java.sample.view.BrowserFragment;
import com.trotri.android.java.sample.view.ButtonPanelFragment;
import com.trotri.android.java.sample.view.DbRegistryFragment;
import com.trotri.android.java.sample.view.ErrorFragment;
import com.trotri.android.java.sample.view.FileSandboxFragment;
import com.trotri.android.java.sample.view.FileSdCardFragment;
import com.trotri.android.java.sample.view.HtNetworkChangeReceiverFragment;
import com.trotri.android.java.sample.view.RoundedImageViewFragment;
import com.trotri.android.java.sample.view.StateContactsFragment;
import com.trotri.android.java.sample.view.StateDisplayPixelsFragment;
import com.trotri.android.java.sample.view.StateLocationStateFragment;
import com.trotri.android.java.sample.view.StateVersionFragment;
import com.trotri.android.java.sample.view.UtilDimensionConverterFragment;
import com.trotri.android.java.sample.view.UtilRegistryFragment;
import com.trotri.android.library.base.BaseActivity;

import java.util.Map;

/**
 * MainActivity class file
 * 全局唯一的Activity
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: MainActivity.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class MainActivity extends BaseActivity {

    private final static String BUTTON_ID = "button_id";
    private final static String DATA = "data";

    public static void actionStart(Context c, int buttonId) {
        actionStart(c, buttonId, null);
    }

    public static void actionStart(Context c, int buttonId, Bundle data) {
        Intent intent = new Intent(c, MainActivity.class);
        intent.putExtra(BUTTON_ID, buttonId);
        intent.putExtra(DATA, data);
        c.startActivity(intent);
    }

    @Override
    protected boolean hasMultiFragments() {
        return false;
    }

    @Override
    protected Fragment newFragment() {
        int buttonId = getIntent().getIntExtra(BUTTON_ID, ButtonPanelBean.BUTTON_PANEL);
        Bundle data = getIntent().getBundleExtra(DATA);

        switch (buttonId) {
            case ButtonPanelBean.BUTTON_PANEL:
                return ButtonPanelFragment.newInstance();
            case ButtonPanelBean.BOOK_LIST:
                return BookListFragment.newInstance();
            case ButtonPanelBean.BOOK_DETAIL:
                return BookDetailFragment.newInstance(data);
            case ButtonPanelBean.ROUNDED_IMAGE_VIEW:
                return RoundedImageViewFragment.newInstance();
            case ButtonPanelBean.BROWSER:
                return BrowserFragment.newInstance();
            case ButtonPanelBean.FILE_SANDBOX:
                return FileSandboxFragment.newInstance();
            case ButtonPanelBean.FILE_SD_CARD:
                return FileSdCardFragment.newInstance();
            case ButtonPanelBean.DB_REGISTRY:
                return DbRegistryFragment.newInstance();
            case ButtonPanelBean.UTIL_REGISTRY:
                return UtilRegistryFragment.newInstance();
            case ButtonPanelBean.UTIL_DIMENSION_CONVERTER:
                return UtilDimensionConverterFragment.newInstance();
            case ButtonPanelBean.STATE_VERSION:
                return StateVersionFragment.newInstance();
            case ButtonPanelBean.STATE_CONTACTS:
                return StateContactsFragment.newInstance();
            case ButtonPanelBean.STATE_DISPLAY_PIXELS:
                return StateDisplayPixelsFragment.newInstance();
            case ButtonPanelBean.STATE_LOCATION_STATE:
                return StateLocationStateFragment.newInstance();
            case ButtonPanelBean.HT_NETWORK_CHANGE_RECEIVER:
                return HtNetworkChangeReceiverFragment.newInstance();
            default:
                break;
        }

        return ErrorFragment.newInstance(getResources().getString(R.string.tips_fragment_not_exists));
    }

    @Override
    protected Map<Integer, Fragment> newFragments() {
        return null;
    }

}
