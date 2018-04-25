package com.trotri.android.java.sample.data.db;

import com.trotri.android.java.sample.data.bean.BookBean;
import com.trotri.android.library.base.BaseDb;
import com.trotri.android.library.data.RequestAdapter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * BookDb class file
 * Db请求类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BookDb.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class BookDb extends BaseDb {

    public static final String TAG = "BookDb";

    /**
     * Db请求接口
     */
    private IBook mDb;

    /**
     * 构造方法: 初始化Db请求接口
     */
    public BookDb() {
        mDb = getRetrofit().create(IBook.class);
    }

    /**
     * 获取首页列表
     *
     * @return 结果集，an Observable<RequestAdapter.ResultList<BookBean>> Object
     */
    public Observable<RequestAdapter.ResultList<BookBean>> findRows() {
        return mDb.findRows().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 设置首页列表
     *
     * @param value 结果集，a RequestAdapter.ResultList<BookBean> Object
     * @return Returns True, or False
     */
    public boolean putRows(RequestAdapter.ResultList<BookBean> value) {
        return mDb.putRows(value);
    }

    /**
     * 通过Id获取详情
     *
     * @param id Id
     * @return 详情结果，an Observable<RequestAdapter.Result<BookBean>> Object
     */
    public Observable<RequestAdapter.Result<BookBean>> getRow(long id) {
        return mDb.getRow(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 设置Id和详情
     *
     * @param id    Id
     * @param value 详情数据，a RequestAdapter.Result<BookBean> Object
     * @return Returns True, or False
     */
    public boolean setRow(long id, RequestAdapter.Result<BookBean> value) {
        return mDb.setRow(id, value);
    }

}
