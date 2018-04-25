package com.trotri.android.java.sample.view.vm;

import com.trotri.android.java.sample.data.ButtonPanelService;
import com.trotri.android.java.sample.data.bean.ButtonPanelBean;
import com.trotri.android.library.base.BaseViewModel;
import com.trotri.android.library.data.ListProvider;

import java.util.List;

/**
 * ButtonPanelViewModel class file
 * 按钮面板ViewModel
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ButtonPanelViewModel.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class ButtonPanelViewModel extends BaseViewModel {

    public static final String TAG = "ButtonPanelViewModel";

    /**
     * 数据业务类
     */
    private ButtonPanelService mService = new ButtonPanelService();

    /**
     * 列表数据提供者
     */
    private ListProvider<ButtonPanelBean.Item> mProvider;

    /**
     * 获取列表数据提供者
     *
     * @return 列表数据提供者，a ListProvider<ButtonPanelBean.Item> Object
     */
    public ListProvider<ButtonPanelBean.Item> getListProvider() {
        if (mProvider == null) {
            List<ButtonPanelBean.Item> rows = mService.findRows();

            mProvider = new ListProvider<>();

            mProvider.setTotal(rows.size());
            mProvider.setLimit(rows.size());
            mProvider.setOffset(0);
            mProvider.addRows(rows);
        }

        return mProvider;
    }

}
