package com.trotri.android.java.sample.data.http;

import com.trotri.android.java.sample.data.bean.BookBean;
import com.trotri.android.library.base.BaseHttp;
import com.trotri.android.library.data.RequestAdapter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * BookHttp class file
 * Http请求类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BookHttp.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class BookHttp extends BaseHttp {

    public static final String TAG = "BookDb";

    /**
     * Http请求接口
     */
    private IBook mHttp;

    /**
     * 构造方法: 初始化Http请求接口
     */
    public BookHttp() {
        mHttp = getRetrofit().create(IBook.class);
    }

    /**
     * 获取列表
     *
     * @param offset 查询起始位置 SELECT * FROM table LIMIT [offset], limit;
     * @param limit  查询记录数 SELECT * FROM table LIMIT offset, [limit];
     * @return 结果集，an Observable<RequestAdapter.ResultList<BookBean>> Object
     */
    public Observable<RequestAdapter.ResultList<BookBean>> findRows(int offset, int limit) {
        return mHttp.findRows(offset, limit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 通过Id获取详情
     *
     * @param id Id
     * @return 详情结果，an Observable<RequestAdapter.Result<BookBean>> Object
     */
    public Observable<RequestAdapter.Result<BookBean>> getRow(long id) {
        return mHttp.getRow(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
