package com.trotri.android.java.sample.data.db;

import com.trotri.android.java.sample.data.bean.BookBean;
import com.trotri.android.library.data.RequestAdapter;
import com.trotri.android.rice.db.retrofit.GET;
import com.trotri.android.rice.db.retrofit.SET;

import io.reactivex.Observable;

/**
 * IBook class file
 * Db请求接口
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: IBook.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public interface IBook {
    /**
     * 获取首页列表
     *
     * @return 结果集，an Observable<RequestAdapter.ResultList<BookBean>> Object
     */
    @GET("book_list")
    Observable<RequestAdapter.ResultList<BookBean>> findRows();

    /**
     * 设置首页列表
     *
     * @param value 结果集，a RequestAdapter.ResultList<BookBean> Object
     * @return Returns True, or False
     */
    @SET("book_list")
    boolean putRows(RequestAdapter.ResultList<BookBean> value);

    /**
     * 通过Id获取详情
     *
     * @param id Id
     * @return 详情结果，an Observable<RequestAdapter.Result<BookBean>> Object
     */
    @GET("book_id_")
    Observable<RequestAdapter.Result<BookBean>> getRow(long id);

    /**
     * 设置Id和详情
     *
     * @param id    Id
     * @param value 详情数据，a RequestAdapter.Result<BookBean> Object
     * @return Returns True, or False
     */
    @SET("book_id_")
    boolean setRow(long id, RequestAdapter.Result<BookBean> value);

}
