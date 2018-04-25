package com.trotri.android.java.sample.data.http;

import com.trotri.android.java.sample.data.bean.BookBean;
import com.trotri.android.library.data.RequestAdapter;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * IBook class file
 * Http请求接口
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: IBook.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public interface IBook {
    /**
     * 获取列表
     *
     * @param offset 查询起始位置 SELECT * FROM table LIMIT [offset], limit;
     * @param limit  查询记录数 SELECT * FROM table LIMIT offset, [limit];
     * @return 结果集，an Observable<RequestAdapter.ResultList<BookBean>> Object
     */
    @GET("action/book_list")
    Observable<RequestAdapter.ResultList<BookBean>> findRows(@Query("offset") int offset, @Query("limit") int limit);

    /**
     * 通过Id获取详情
     *
     * @param id Id
     * @return 详情结果，an Observable<RequestAdapter.Result<BookBean>> Object
     */
    @GET("action/book_detail")
    Observable<RequestAdapter.Result<BookBean>> getRow(@Query("id") long id);

}
