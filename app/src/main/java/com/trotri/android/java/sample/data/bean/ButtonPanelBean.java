package com.trotri.android.java.sample.data.bean;

/**
 * ButtonPanelEntity final class file
 * 按钮面板数据模型类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ButtonPanelHelper.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public final class ButtonPanelBean {
    /**
     * 按钮Id
     */
    public static final int BUTTON_PANEL = 1;
    public static final int BOOK_LIST = 100;
    public static final int BOOK_DETAIL = 101;
    public static final int ROUNDED_IMAGE_VIEW = 200;
    public static final int BROWSER = 300;
    public static final int FILE_SANDBOX = 400;
    public static final int FILE_SD_CARD = 401;
    public static final int DB_REGISTRY = 500;
    public static final int UTIL_REGISTRY = 501;
    public static final int UTIL_DIMENSION_CONVERTER = 502;
    public static final int STATE_VERSION = 600;
    public static final int STATE_CONTACTS = 601;
    public static final int STATE_DISPLAY_PIXELS = 602;
    public static final int STATE_LOCATION_STATE = 603;
    public static final int HT_NETWORK_CHANGE_RECEIVER = 700;

    /**
     * Item final class
     * 按钮数据模型类
     *
     * @since 1.0
     */
    public static final class Item {
        /**
         * 按钮Id
         */
        private int mId;

        /**
         * 按钮名称
         */
        private String mName;

        /**
         * 构造方法：初始化按钮Id、按钮名称
         *
         * @param id   按钮Id
         * @param name 按钮名称
         */
        public Item(int id, String name) {
            setId(id);
            setName(name);
        }

        /**
         * 获取按钮Id
         *
         * @return 按钮Id
         */
        public int getId() {
            return mId;
        }

        /**
         * 设置按钮Id
         *
         * @param id 按钮Id
         */
        public void setId(int id) {
            mId = id;
        }

        /**
         * 获取按钮名称
         *
         * @return 按钮名称
         */
        public String getName() {
            return mName;
        }

        /**
         * 设置按钮名称
         *
         * @param name 按钮名称
         */
        public void setName(String name) {
            mName = name;
        }

    }

}
