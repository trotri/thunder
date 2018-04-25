package com.trotri.android.java.sample.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trotri.android.java.sample.MainActivity;
import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.data.bean.BookBean;
import com.trotri.android.java.sample.data.bean.ButtonPanelBean;
import com.trotri.android.java.sample.databinding.FragmentBookListBinding;
import com.trotri.android.java.sample.databinding.ItemBookListBinding;
import com.trotri.android.java.sample.view.vm.BookListViewModel;
import com.trotri.android.java.sample.view.vm.processor.BookListProcessor;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.rice.util.ActionManager;
import com.trotri.android.rice.view.recycler.adapter.DataBindingAdapter;
import com.trotri.android.rice.view.recycler.decoration.ExpandableItemDecoration;
import com.trotri.android.thunder.ap.Logger;
import com.trotri.android.thunder.inj.ViewModelInject;
import com.trotri.android.thunder.view.SwipeUpRefreshLayout;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * BookListViewModel class file
 * Book列表视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BookListFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(BookListViewModel.class)
public class BookListFragment extends BaseFragment<BookListViewModel> {

    public static final String TAG = "BookListFragment";

    private FragmentBookListBinding mDataBinding;

    private BookListProcessor mProcessor;

    private SwipeUpRefreshLayout mSurLayFooter;
    private LinearLayoutManager mLLayManager;
    private ExpandableItemDecoration mItemDecoration;
    private BookListAdapter mBookListAdapter;

    private boolean mLoaded = false;

    public static BaseFragment newInstance() {
        return new BookListFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_book_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentBookListBinding.bind(view);

        mDataBinding.srlayContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load(true);
            }
        });

        mSurLayFooter = (new SwipeUpRefreshLayout.Builder(getActivity())).setOnLoadMoreClickListener(new SwipeUpRefreshLayout.OnLoadMoreClickListener() {
            @Override
            public void onClick(View v) {
                load(false);
            }
        }).create();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mLoaded) {
            return;
        }

        mLoaded = true;

        mProcessor = mViewModel.getProcessor();

        mLLayManager = new LinearLayoutManager(getActivity());
        mItemDecoration = (new ExpandableItemDecoration.Builder(getActivity())).create();
        mBookListAdapter = new BookListAdapter(getActivity());
        mBookListAdapter.setFooterView(mSurLayFooter);

        mDataBinding.rvBookList.setLayoutManager(mLLayManager);
        mDataBinding.rvBookList.addItemDecoration(mItemDecoration);
        mDataBinding.rvBookList.setAdapter(mBookListAdapter);

        mDataBinding.rvBookList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);

                if (dy <= 0) {
                    return;
                }

                if (mSurLayFooter.loadReady(mLLayManager)) {
                    load(false);
                }
            }
        });

        onBind();

        load(false);

        Observable.timer(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long l) throws Exception {
                load(true);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable tr) throws Exception {
                Logger.e(TAG, "delay refresh failure", tr);
            }
        });
    }

    public void onBind() {
        // 请求中，正在请求数据
        mProcessor.bind(BookListProcessor.BIND_STATUS.LOADING.name(), new Consumer<ActionManager.Result>() {
            @Override
            public void accept(ActionManager.Result result) throws Exception {
                Toast.makeText(getActivity(), "Loading", Toast.LENGTH_SHORT).show();
                mDataBinding.srlayContainer.setRefreshing(true);
                mSurLayFooter.setStatus(SwipeUpRefreshLayout.STATUS_LOADING);
            }
        });

        // 请求成功，还可以再次请求更多数据
        mProcessor.bind(BookListProcessor.BIND_STATUS.LOAD_MORE.name(), new Consumer<ActionManager.Result>() {
            @Override
            public void accept(ActionManager.Result result) throws Exception {
                mItemDecoration.setGroups(mProcessor.getTypes());
                mBookListAdapter.notifyDataSetChanged();
                mDataBinding.srlayContainer.setRefreshing(false);
                mSurLayFooter.setStatus(SwipeUpRefreshLayout.STATUS_LOAD_MORE);
            }
        });

        // 请求接口成功，但没有数据或不可再次请求数据
        mProcessor.bind(BookListProcessor.BIND_STATUS.NO_DATA.name(), new Consumer<ActionManager.Result>() {
            @Override
            public void accept(ActionManager.Result result) throws Exception {
                mItemDecoration.setGroups(mProcessor.getTypes());
                mBookListAdapter.notifyDataSetChanged();
                mDataBinding.srlayContainer.setRefreshing(false);
                mSurLayFooter.setStatus(SwipeUpRefreshLayout.STATUS_NO_DATA);
            }
        });

        // 请求失败，通过错误码和错误消息获取失败原因
        mProcessor.bind(BookListProcessor.BIND_STATUS.LOAD_FAILURE.name(), new Consumer<ActionManager.Result>() {
            @Override
            public void accept(ActionManager.Result result) throws Exception {
                Toast.makeText(getActivity(), "Load Failure " + result.getErrMsg(), Toast.LENGTH_SHORT).show();
                mDataBinding.srlayContainer.setRefreshing(false);
                mSurLayFooter.setStatus(SwipeUpRefreshLayout.STATUS_NO_DATA);
            }
        });
    }

    /**
     * 请求数据
     *
     * @param refresh 是否是下拉刷新，下拉刷新时：refresh = true
     */
    public void load(boolean refresh) {
        mProcessor.load(refresh);
    }

    public void onBookListItemClick(long id) {
        Bundle data = new Bundle();
        data.putLong(BookDetailFragment.KEY_BOOK_ID, id);

        MainActivity.actionStart(getActivity(), ButtonPanelBean.BOOK_DETAIL, data);
    }

    class BookListAdapter extends DataBindingAdapter<ItemBookListBinding> {

        public BookListAdapter(Context c) {
            super(c);
        }

        @Override
        public void bindData(ViewHolder holder, int position) {
            BookBean bean = mProcessor.getItem(position);

            holder.getDataBinding().setView(BookListFragment.this);
            if (bean != null) {
                holder.getDataBinding().setItem(bean);
            }
        }

        @Override
        public ItemBookListBinding newDataBinding(ViewGroup root) {
            return ItemBookListBinding.inflate(getLayoutInflater(), root, false);
        }

        @Override
        public int getSize() {
            return mProcessor.getSize();
        }

    }

}
