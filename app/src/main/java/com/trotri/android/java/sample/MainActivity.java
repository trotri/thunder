package com.trotri.android.java.sample;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.trotri.android.java.sample.data.bean.ButtonPanelBean;
import com.trotri.android.java.sample.view.BookDetailFragment;
import com.trotri.android.java.sample.view.ButtonPanelFragment;
import com.trotri.android.java.sample.view.ErrorFragment;
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

    public static void actionStart(Context c, int buttonId) {
        Intent intent = new Intent(c, MainActivity.class);
        intent.putExtra(BUTTON_ID, buttonId);
        c.startActivity(intent);
    }

    @Override
    protected boolean hasMultiFragments() {
        return false;
    }

    @Override
    protected Fragment newFragment() {
        int buttonId = getIntent().getIntExtra(BUTTON_ID, ButtonPanelBean.BUTTON_PANEL);
        switch (buttonId) {
            case ButtonPanelBean.BUTTON_PANEL:
                return ButtonPanelFragment.newInstance();
            case ButtonPanelBean.DATA_ADAPTER:
                return BookDetailFragment.newInstance(2);
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
