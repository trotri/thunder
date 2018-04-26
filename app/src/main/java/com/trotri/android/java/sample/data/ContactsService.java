package com.trotri.android.java.sample.data;

import com.trotri.android.java.sample.data.bean.ContactsBean;
import com.trotri.android.library.util.SingletonManager;
import com.trotri.android.thunder.state.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * ContactsService class file
 * 联系人数据业务类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ContactsService.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class ContactsService {
    /**
     * 联系人列表
     */
    private List<ContactsBean> mItems;

    /**
     * 联系人列表
     *
     * @return 联系人列表
     */
    public List<ContactsBean> findRows() {
        if (mItems == null) {
            mItems = new ArrayList<>();

            List<Contacts.Item> data = SingletonManager.getContacts().getData();
            if (data != null) {
                for (Contacts.Item row : data) {
                    mItems.add(new ContactsBean(row));
                }
            }
        }

        return mItems;
    }

}
