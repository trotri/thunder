package com.trotri.android.java.sample.view.vm;

import com.trotri.android.java.sample.data.ContactsService;
import com.trotri.android.java.sample.data.bean.ContactsBean;
import com.trotri.android.library.base.BaseViewModel;
import com.trotri.android.library.data.ListProvider;
import com.trotri.android.thunder.ap.Logger;

import java.util.List;

/**
 * StateContactsViewModel class file
 * 联系人ViewModel
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: StateContactsViewModel.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class StateContactsViewModel extends BaseViewModel {

    public static final String TAG = "StateContactsViewModel";

    /**
     * 数据业务类
     */
    private ContactsService mService = new ContactsService();

    /**
     * 列表数据提供者
     */
    private ListProvider<ContactsBean> mProvider;

    /**
     * 获取列表数据提供者
     *
     * @return 列表数据提供者，a ListProvider<ContactsBean> Object
     */
    public ListProvider<ContactsBean> getListProvider() {
        if (mProvider == null) {
            List<ContactsBean> rows = mService.findRows();

            mProvider = new ListProvider<>();

            mProvider.setTotal(rows.size());
            mProvider.setLimit(rows.size());
            mProvider.setOffset(0);
            mProvider.addRows(rows);
        }

        return mProvider;
    }

}
