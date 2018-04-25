package com.trotri.android.java.sample.view.vm;

import com.trotri.android.java.sample.view.vm.processor.BookDetailProcessor;
import com.trotri.android.library.base.BaseViewModel;

/**
 * BookDetailViewModel class file
 * Book详情ViewModel
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BookDetailViewModel.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class BookDetailViewModel extends BaseViewModel {

    public static final String TAG = "BookDetailViewModel";

    /**
     * Book详情数据处理类
     */
    private BookDetailProcessor mProcessor = new BookDetailProcessor();

    /**
     * 获取Book详情数据处理类
     *
     * @return Book详情数据处理类，a BookDetailProcessor Object
     */
    public BookDetailProcessor getProcessor() {
        return mProcessor;
    }

}
