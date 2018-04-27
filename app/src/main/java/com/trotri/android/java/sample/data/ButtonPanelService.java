package com.trotri.android.java.sample.data;

import android.content.Context;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.data.bean.ButtonPanelBean;
import com.trotri.android.library.App;

import java.util.ArrayList;
import java.util.List;

/**
 * ButtonPanelService class file
 * 按钮面板数据业务类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ButtonPanelService.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class ButtonPanelService {
    /**
     * 按钮实体列表
     */
    private List<ButtonPanelBean.Item> mItems;

    /**
     * 获取按钮列表
     *
     * @return 按钮列表，a List<ButtonPanelBean.Item> Object
     */
    public List<ButtonPanelBean.Item> findRows() {
        if (mItems == null) {
            mItems = new ArrayList<>();
            Context c = App.getContext();

            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.BOOK_LIST, c.getString(R.string.book_list)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.ROUNDED_IMAGE_VIEW, c.getString(R.string.rounded_image_view)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.BROWSER_INPUT, c.getString(R.string.browser_input)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.FILE_SANDBOX, c.getString(R.string.file_sandbox)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.FILE_SD_CARD, c.getString(R.string.file_sd_card)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.DB_REGISTRY, c.getString(R.string.db_registry)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.UTIL_REGISTRY, c.getString(R.string.util_registry)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.UTIL_DIMENSION_CONVERTER, c.getString(R.string.util_dimension_converter)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.STATE_VERSION, c.getString(R.string.state_version)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.STATE_CONTACTS, c.getString(R.string.state_contacts)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.STATE_DISPLAY_PIXELS, c.getString(R.string.state_display_pixels)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.STATE_LOCATION_STATE, c.getString(R.string.state_location_state)));
            mItems.add(new ButtonPanelBean.Item(ButtonPanelBean.HT_NETWORK_CHANGE_RECEIVER, c.getString(R.string.ht_network_change_receiver)));
        }

        return mItems;
    }

}
