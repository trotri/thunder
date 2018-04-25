package com.trotri.android.java.sample.view.vm.processor;

import com.trotri.android.java.sample.data.BookService;
import com.trotri.android.java.sample.data.bean.BookBean;
import com.trotri.android.library.data.AbstractListProcessor;
import com.trotri.android.library.data.RequestAdapter;

import io.reactivex.Observable;

/**
 * BookListProcessor class file
 * Book列表数据处理类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BookListProcessor.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class BookListProcessor extends AbstractListProcessor<BookBean> {

    public static final String TAG = "BookListProcessor";

    /**
     * 数据业务类
     */
    private BookService mService = new BookService();

    @Override
    protected Observable<RequestAdapter.ResultList<BookBean>> request(boolean refresh) {
        if (refresh) {
            return mService.findRowsFromHttp(getOffset(), getLimit(), true);
        }

        return mService.findRows(getOffset(), getLimit());
    }

}
