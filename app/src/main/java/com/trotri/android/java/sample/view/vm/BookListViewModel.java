package com.trotri.android.java.sample.view.vm;

import com.trotri.android.java.sample.view.vm.processor.BookListProcessor;
import com.trotri.android.library.base.BaseViewModel;

/**
 * BookListViewModel class file
 * Book列表ViewModel
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BookListViewModel.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
public class BookListViewModel extends BaseViewModel {

    public static final String TAG = "BookListViewModel";

    /**
     * Book列表数据处理类
     */
    private BookListProcessor mProcessor = new BookListProcessor();

    /**
     * 获取Book列表数据处理类
     *
     * @return Book列表数据处理类，a BookListProcessor Object
     */
    public BookListProcessor getProcessor() {
        return mProcessor;
    }

}
