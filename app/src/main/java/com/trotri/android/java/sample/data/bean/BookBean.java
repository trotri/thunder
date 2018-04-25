package com.trotri.android.java.sample.data.bean;

/**
 * BookBean final class file
 * Book数据模型类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BookBean.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public final class BookBean {
    /**
     * Id
     */
    private long mId;

    /**
     * 标题
     */
    private String mTitle;

    /**
     * 分类
     */
    private String mType;

    /**
     * 描述
     */
    private String mDescription;

    /**
     * 图片
     */
    private String mPicture;

    /**
     * 是否推荐
     */
    private boolean mIsRecommend;

    /**
     * 创建时间, 默认: "00-00 00:00"
     */
    private String mDtCreated = "00-00 00:00";

    /**
     * 默认构造方法
     */
    public BookBean() {
    }

    /**
     * 构造方法：初始化所有属性
     *
     * @param id          Id
     * @param title       标题
     * @param type        分类
     * @param description 描述
     * @param picture     图片
     * @param isRecommend 是否推荐
     * @param dtCreated   创建时间
     */
    public BookBean(long id, String title, String type, String description, String picture, boolean isRecommend, String dtCreated) {
        setId(id);
        setTitle(title);
        setType(type);
        setDescription(description);
        setPicture(picture);
        setRecommend(isRecommend);
        setDtCreated(dtCreated);
    }

    /**
     * 获取Id
     *
     * @return Id
     */
    public long getId() {
        return mId;
    }

    /**
     * 设置Id
     *
     * @param id Id
     */
    public void setId(long id) {
        mId = id;
    }

    /**
     * 获取标题
     *
     * @return 标题
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * 获取分类
     *
     * @return 分类
     */
    public String getType() {
        return mType;
    }

    /**
     * 设置分类
     *
     * @param type 分类
     */
    public void setType(String type) {
        mType = type;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        mDescription = description;
    }

    /**
     * 获取图片
     *
     * @return 图片
     */
    public String getPicture() {
        return mPicture;
    }

    /**
     * 设置图片
     *
     * @param picture 图片
     */
    public void setPicture(String picture) {
        mPicture = picture;
    }

    /**
     * 获取是否推荐
     *
     * @return Returns True, or False
     */
    public boolean isRecommend() {
        return mIsRecommend;
    }

    /**
     * 设置是否推荐
     *
     * @param isRecommend 是否推荐
     */
    public void setRecommend(boolean isRecommend) {
        mIsRecommend = isRecommend;
    }

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    public String getDtCreated() {
        return mDtCreated;
    }

    /**
     * 设置创建时间
     *
     * @param dtCreated 创建时间
     */
    public void setDtCreated(String dtCreated) {
        mDtCreated = dtCreated;
    }

    @Override
    public String toString() {
        return (new StringBuilder()).append("[ ")
                .append("id: ").append(getId())
                .append(", title: ").append(getTitle())
                .append(", type: ").append(getType())
                .append(", description: ").append(getDescription())
                .append(", picture: ").append(getPicture())
                .append(", isRecommend: ").append(isRecommend())
                .append(", dtCreated: ").append(getDtCreated())
                .append(" ]").toString();
    }

}
