package com.trotri.android.java.sample.data.bean;

import com.trotri.android.thunder.state.Contacts;

import java.util.List;

/**
 * ContactsBean final class file
 * 联系人数据模型类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ContactsBean.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public final class ContactsBean {
    /**
     * 联系人Id
     */
    private int mId;

    /**
     * Display Name
     */
    private String mDisplayName;

    /**
     * 号码列表，多个手机号码之间用空格分隔
     */
    private String mPhoneNumbers = "";

    /**
     * 邮箱列表，多个邮箱之间用空格分隔
     */
    private String mEmails = "";

    /**
     * 构造方法：通过Contacts.item初始化联系人Id、Display Name、号码列表、邮箱列表
     *
     * @param data a Contacts.Item Object
     */
    public ContactsBean(Contacts.Item data) {
        if (data == null) {
            return;
        }

        mId = data.getId();
        mDisplayName = data.getDisplayName();

        List<String> tmpPhones = data.getPhoneNumbers();
        for (String phone : tmpPhones) {
            mPhoneNumbers += phone + " ";
        }

        List<String> tmpEmails = data.getEmails();
        for (String email : tmpEmails) {
            mEmails += email + " ";
        }
    }

    /**
     * 获取联系人Id
     *
     * @return 联系人Id
     */
    public int getId() {
        return mId;
    }

    /**
     * 设置联系人Id
     *
     * @param id 联系人Id
     */
    public void setId(int id) {
        mId = id;
    }

    /**
     * 获取Display Name
     *
     * @return Display Name
     */
    public String getDisplayName() {
        return mDisplayName;
    }

    /**
     * 设置Display Name
     *
     * @param displayName Display Name
     */
    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    /**
     * 获取号码列表
     *
     * @return 号码列表，多个手机号码之间用空格分隔
     */
    public String getPhoneNumbers() {
        return mPhoneNumbers;
    }

    /**
     * 设置号码列表
     *
     * @param phoneNumbers 号码列表，多个手机号码之间用空格分隔
     */
    public void setPhoneNumbers(String phoneNumbers) {
        mPhoneNumbers = phoneNumbers;
    }

    /**
     * 获取邮箱列表
     *
     * @return 邮箱列表，多个邮箱之间用空格分隔
     */
    public String getEmails() {
        return mEmails;
    }

    /**
     * 设置邮箱列表
     *
     * @param emails 邮箱列表，多个邮箱之间用空格分隔
     */
    public void setEmails(String emails) {
        mEmails = emails;
    }

    @Override
    public String toString() {
        return (new StringBuilder()).append("[ ")
                .append("id: ").append(getId())
                .append(", displayName: ").append(getDisplayName())
                .append(", phoneNumbers: ").append(getPhoneNumbers())
                .append(", emails: ").append(getEmails())
                .append(" ]").toString();
    }

}
