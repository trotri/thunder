package com.trotri.android.java.sample.data;

import com.trotri.android.java.sample.data.bean.BookBean;
import com.trotri.android.java.sample.data.db.BookDb;
import com.trotri.android.java.sample.data.http.BookHttp;
import com.trotri.android.library.data.RequestAdapter;
import com.trotri.android.thunder.ap.Logger;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * BookService class file
 * Book数据业务类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BookService.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class BookService {

    public static final String TAG = "BookService";

    /**
     * Http请求类
     */
    private BookHttp mHttp;

    /**
     * Db请求类
     */
    private BookDb mDb;

    /**
     * 构造方法: 初始化Http请求类和Db请求类
     */
    public BookService() {
        mHttp = new BookHttp();
        mDb = new BookDb();
    }

    /**
     * 获取列表
     *
     * @param offset 查询起始位置 SELECT * FROM table LIMIT [offset], limit;
     * @param limit  查询记录数 SELECT * FROM table LIMIT offset, [limit];
     * @return 结果集，an Observable<RequestAdapter.ResultList<BookBean>> Object
     */
    public Observable<RequestAdapter.ResultList<BookBean>> findRows(final int offset, final int limit) {
        if (offset != 0) {
            return findRowsFromHttp(offset, limit, false);
        }

        final Observable<RequestAdapter.ResultList<BookBean>> dbResult = findRowsFromDb();

        return dbResult.flatMap(new Function<RequestAdapter.ResultList<BookBean>, ObservableSource<RequestAdapter.ResultList<BookBean>>>() {
            @Override
            public ObservableSource<RequestAdapter.ResultList<BookBean>> apply(RequestAdapter.ResultList<BookBean> data) throws Exception {
                if (RequestAdapter.hasErr(data)) {
                    Logger.w(TAG, "findRows() offset: " + offset + ", limit: " + limit + ", db " + RequestAdapter.toLog(data));
                    return findRowsFromHttp(offset, limit, true);
                }

                Logger.d(TAG, "findRows() offset: " + offset + ", limit: " + limit + ", use db result");
                return dbResult;
            }
        }, new Function<Throwable, ObservableSource<RequestAdapter.ResultList<BookBean>>>() {
            @Override
            public ObservableSource<RequestAdapter.ResultList<BookBean>> apply(Throwable tr) throws Exception {
                Logger.w(TAG, "findRows() offset: " + offset + ", limit: " + limit + ", db has err, use http result, errMsg: " + tr);
                return findRowsFromHttp(offset, limit, true);
            }
        }, new Callable<ObservableSource<RequestAdapter.ResultList<BookBean>>>() {
            @Override
            public ObservableSource<RequestAdapter.ResultList<BookBean>> call() throws Exception {
                return dbResult;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 通过Id获取详情
     *
     * @param id Id
     * @return 详情结果，an Observable<RequestAdapter.Result<BookBean>> Object
     */
    public Observable<RequestAdapter.Result<BookBean>> getRow(final long id) {
        final Observable<RequestAdapter.Result<BookBean>> httpResult = getRowFromHttp(id, true);

        return httpResult.flatMap(new Function<RequestAdapter.Result<BookBean>, ObservableSource<RequestAdapter.Result<BookBean>>>() {
            @Override
            public ObservableSource<RequestAdapter.Result<BookBean>> apply(RequestAdapter.Result<BookBean> data) throws Exception {
                if (RequestAdapter.hasErr(data)) {
                    Logger.w(TAG, "getRow() id: " + id + ", http " + RequestAdapter.toLog(data));
                    return getRowFromDb(id);
                }

                Logger.d(TAG, "getRow() id: " + id + ", use http result");
                return httpResult;
            }
        }, new Function<Throwable, ObservableSource<RequestAdapter.Result<BookBean>>>() {
            @Override
            public ObservableSource<RequestAdapter.Result<BookBean>> apply(Throwable tr) throws Exception {
                Logger.w(TAG, "getRow() id: " + id + ", http has err, use db result, errMsg: " + tr);
                return getRowFromDb(id);
            }
        }, new Callable<ObservableSource<RequestAdapter.Result<BookBean>>>() {
            @Override
            public ObservableSource<RequestAdapter.Result<BookBean>> call() throws Exception {
                return httpResult;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取列表 - Http
     *
     * @param offset 查询起始位置 SELECT * FROM table LIMIT [offset], limit;
     * @param limit  查询记录数 SELECT * FROM table LIMIT offset, [limit];
     * @param saveDb 是否保存到Db
     * @return 结果集，an Observable<RequestAdapter.ResultList<BookBean>> Object
     */
    public Observable<RequestAdapter.ResultList<BookBean>> findRowsFromHttp(final int offset, final int limit, final boolean saveDb) {
        Observable<RequestAdapter.ResultList<BookBean>> result = mHttp.findRows(offset, limit);

        if (saveDb) {
            result.subscribe(new Consumer<RequestAdapter.ResultList<BookBean>>() {
                @Override
                public void accept(RequestAdapter.ResultList<BookBean> data) throws Exception {
                    if (RequestAdapter.hasErr(data)) {
                        Logger.w(TAG, "findRowsFromHttp() offset: " + offset + ", limit: " + limit + ", saveDb: " + saveDb + ", http " + RequestAdapter.toLog(data));
                        return;
                    }

                    putRowsToDb(data);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable tr) throws Exception {
                    Logger.e(TAG, "findRowsFromHttp() offset: " + offset + ", limit: " + limit + ", saveDb: " + saveDb, tr);
                }
            });
        }

        return result.observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 通过Id获取详情 - Http
     *
     * @param id     Id
     * @param saveDb 是否保存到Db
     * @return 详情结果，an Observable<RequestAdapter.Result<BookBean>> Object
     */
    public Observable<RequestAdapter.Result<BookBean>> getRowFromHttp(final long id, final boolean saveDb) {
        Observable<RequestAdapter.Result<BookBean>> result = mHttp.getRow(id);

        if (saveDb) {
            result.subscribe(new Consumer<RequestAdapter.Result<BookBean>>() {
                @Override
                public void accept(RequestAdapter.Result<BookBean> data) throws Exception {
                    if (RequestAdapter.hasErr(data)) {
                        Logger.e(TAG, "getRowFromHttp() id: " + id + ", saveDb: " + saveDb + ", http " + RequestAdapter.toLog(data));
                        return;
                    }

                    setRowToDb(id, data);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable tr) throws Exception {
                    Logger.e(TAG, "getRowFromHttp() id: " + id + ", saveDb: " + saveDb, tr);
                }
            });
        }

        return result.observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取首页列表 - Db
     *
     * @return 结果集，an Observable<RequestAdapter.ResultList<BookBean>> Object
     */
    public Observable<RequestAdapter.ResultList<BookBean>> findRowsFromDb() {
        return mDb.findRows();
    }

    /**
     * 设置首页列表 - Db
     *
     * @param value 结果集，a RequestAdapter.ResultList<BookBean> Object
     * @return Returns True, or False
     */
    public boolean putRowsToDb(RequestAdapter.ResultList<BookBean> value) {
        return mDb.putRows(value);
    }

    /**
     * 通过Id获取详情 - Db
     *
     * @param id Id
     * @return 详情结果，an Observable<RequestAdapter.Result<BookBean>> Object
     */
    public Observable<RequestAdapter.Result<BookBean>> getRowFromDb(long id) {
        return mDb.getRow(id);
    }

    /**
     * 设置Id和详情 - Db
     *
     * @param id    Id
     * @param value 详情数据，a RequestAdapter.Result<BookBean> Object
     * @return Returns True, or False
     */
    public boolean setRowToDb(long id, RequestAdapter.Result<BookBean> value) {
        return mDb.setRow(id, value);
    }

}
