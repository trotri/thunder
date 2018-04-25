package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.data.bean.BookBean;
import com.trotri.android.java.sample.databinding.FragmentBookDetailBinding;
import com.trotri.android.java.sample.view.vm.BookDetailViewModel;
import com.trotri.android.java.sample.view.vm.processor.BookDetailProcessor;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.rice.util.ActionManager;
import com.trotri.android.thunder.inj.ViewModelInject;

import io.reactivex.functions.Consumer;

/**
 * BookDetailFragment class file
 * Book详情视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BookDetailFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(BookDetailViewModel.class)
public class BookDetailFragment extends BaseFragment<BookDetailViewModel> {

    public static final String KEY_BOOK_ID = "book_id";

    private FragmentBookDetailBinding mDataBinding;

    private BookDetailProcessor mProcessor;

    private long mBookId;

    public static BaseFragment newInstance(long bookId) {
        BaseFragment f = new BookDetailFragment();

        Bundle b = new Bundle();
        b.putLong(KEY_BOOK_ID, bookId);

        f.setArguments(b);
        return f;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_book_detail;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentBookDetailBinding.bind(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        mBookId = getArguments().getLong(KEY_BOOK_ID);
        mProcessor = mViewModel.getProcessor();

        onBind();

        mProcessor.load(mBookId);
    }

    public void onBind() {
        // 请求中，正在请求数据
        mProcessor.bind(BookDetailProcessor.BIND_STATUS.LOADING.name(), new Consumer<ActionManager.Result>() {
            @Override
            public void accept(ActionManager.Result result) throws Exception {
                Toast.makeText(getActivity(), "Loading", Toast.LENGTH_SHORT).show();
            }
        });

        // 请求成功
        mProcessor.bind(BookDetailProcessor.BIND_STATUS.LOAD_SUCCESS.name(), new Consumer<ActionManager.Result>() {
            @Override
            public void accept(ActionManager.Result result) throws Exception {
                BookBean bean = mProcessor.getData();
                mDataBinding.setBean(bean);
            }
        });

        // 请求接口成功，但没有数据
        mProcessor.bind(BookDetailProcessor.BIND_STATUS.NO_DATA.name(), new Consumer<ActionManager.Result>() {
            @Override
            public void accept(ActionManager.Result result) throws Exception {
                Toast.makeText(getActivity(), "Load Success " + result.getErrMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        // 请求失败，通过错误码和错误消息获取失败原因
        mProcessor.bind(BookDetailProcessor.BIND_STATUS.LOAD_FAILURE.name(), new Consumer<ActionManager.Result>() {
            @Override
            public void accept(ActionManager.Result result) throws Exception {
                Toast.makeText(getActivity(), "Load Failure " + result.getErrMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
