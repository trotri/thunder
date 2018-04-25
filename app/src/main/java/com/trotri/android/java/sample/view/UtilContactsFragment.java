package com.trotri.android.java.sample.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.data.bean.ContactsBean;
import com.trotri.android.java.sample.databinding.FragmentUtilContactsBinding;
import com.trotri.android.java.sample.databinding.ItemUtilContactsBinding;
import com.trotri.android.java.sample.view.vm.UtilContactsViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.library.view.StandardItemDecoration;
import com.trotri.android.rice.view.recycler.adapter.DataBindingAdapter;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * UtilContactsFragment class file
 * 联系人视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: UtilContactsFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(UtilContactsViewModel.class)
public class UtilContactsFragment extends BaseFragment<UtilContactsViewModel> {

    public static BaseFragment newInstance() {
        return new UtilContactsFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_util_contacts;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentUtilContactsBinding dataBinding = FragmentUtilContactsBinding.bind(view);

        dataBinding.rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataBinding.rvContacts.setAdapter(new Adapter(getActivity()));
        dataBinding.rvContacts.addItemDecoration(new StandardItemDecoration(getActivity()));
    }

    public class Adapter extends DataBindingAdapter<ItemUtilContactsBinding> {

        public Adapter(Context c) {
            super(c);
        }

        @Override
        public void bindData(ViewHolder holder, int position) {
            ContactsBean bean = mViewModel.getListProvider().getItem(position);
            if (bean != null) {
                holder.getDataBinding().setItem(bean);
            }
        }

        @Override
        public ItemUtilContactsBinding newDataBinding(ViewGroup root) {
            return ItemUtilContactsBinding.inflate(getLayoutInflater(), root, false);
        }

        @Override
        public int getSize() {
            return mViewModel.getListProvider().getSize();
        }

    }

}
