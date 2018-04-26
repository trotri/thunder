package com.trotri.android.java.sample.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.trotri.android.java.sample.MainActivity;
import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentButtonPanelBinding;
import com.trotri.android.java.sample.databinding.ItemButtonPanelBinding;
import com.trotri.android.java.sample.view.vm.ButtonPanelViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.library.view.StandardItemDecoration;
import com.trotri.android.rice.view.recycler.adapter.DataBindingAdapter;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * ButtonPanelFragment class file
 * 按钮面板视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ButtonPanelFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(ButtonPanelViewModel.class)
public class ButtonPanelFragment extends BaseFragment<ButtonPanelViewModel> {

    public static final String TAG = "ButtonPanelFragment";

    public static BaseFragment newInstance() {
        return new ButtonPanelFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_button_panel;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentButtonPanelBinding dataBinding = FragmentButtonPanelBinding.bind(view);

        dataBinding.rvButtonPanel.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataBinding.rvButtonPanel.setAdapter(new Adapter(getActivity()));
        dataBinding.rvButtonPanel.addItemDecoration(new StandardItemDecoration(getActivity()));
    }

    public void onButtonPanelItemClick(int buttonId) {
        MainActivity.actionStart(getActivity(), buttonId);
    }

    public class Adapter extends DataBindingAdapter<ItemButtonPanelBinding> {

        public Adapter(Context c) {
            super(c);
        }

        @Override
        public void bindData(ViewHolder holder, int position) {
            holder.getDataBinding().setItem(mViewModel.getListProvider().getItem(position));
            holder.getDataBinding().setView(ButtonPanelFragment.this);
        }

        @Override
        public ItemButtonPanelBinding newDataBinding(ViewGroup root) {
            return ItemButtonPanelBinding.inflate(getLayoutInflater(), root, false);
        }

        @Override
        public int getSize() {
            return mViewModel.getListProvider().getSize();
        }

    }

}
