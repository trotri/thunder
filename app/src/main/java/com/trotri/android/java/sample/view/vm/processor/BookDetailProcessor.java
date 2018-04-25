package com.trotri.android.java.sample.view.vm.processor;

import com.trotri.android.java.sample.data.BookService;
import com.trotri.android.java.sample.data.bean.BookBean;
import com.trotri.android.library.data.AbstractDetailProcessor;
import com.trotri.android.library.data.RequestAdapter;

import io.reactivex.Observable;

/**
 * BookDetailProcessor class file
 * Book详情数据处理类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BookDetailProcessor.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class BookDetailProcessor extends AbstractDetailProcessor<BookBean> {

    public static final String TAG = "BookDetailProcessor";

    /**
     * 数据业务类
     */
    private BookService mService = new BookService();

    @Override
    protected <P> Observable<RequestAdapter.Result<BookBean>> request(P id) {
        return mService.getRow((Long) id);
    }

}
