package com.trotri.android.java.sample.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentHtNetworkChangeReceiverBinding;
import com.trotri.android.java.sample.view.vm.HtNetworkChangeReceiverViewModel;
import com.trotri.android.library.App;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.library.util.SingletonManager;
import com.trotri.android.thunder.ap.Network;
import com.trotri.android.thunder.ht.NetworkChangeReceiver;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * HtNetworkChangeReceiverFragment class file
 * 手机网络改变时广播的视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: HtNetworkChangeReceiverFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(HtNetworkChangeReceiverViewModel.class)
public class HtNetworkChangeReceiverFragment extends BaseFragment<HtNetworkChangeReceiverViewModel> {

    private FragmentHtNetworkChangeReceiverBinding mDataBinding;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    public static BaseFragment newInstance() {
        return new HtNetworkChangeReceiverFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_ht_network_change_receiver;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentHtNetworkChangeReceiverBinding.bind(view);

        mNetworkChangeReceiver = new NetworkChangeReceiver(App.getContext(), new NetworkChangeReceiver.Listener() {
            @Override
            public void onReceive(Context c, Intent i) {
                networkChange();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mNetworkChangeReceiver != null) {
            mNetworkChangeReceiver.register();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mNetworkChangeReceiver != null) {
            mNetworkChangeReceiver.unregister();
            mNetworkChangeReceiver = null;
        }
    }

    public void networkChange() {
        Network network = SingletonManager.getNetworkState().getNetwork();

        boolean hasAccessNetworkState = network.hasAccessNetworkState();
        boolean isConnected = network.isConnected();
        boolean isWifi = network.isWifi();
        boolean isMobile = network.isMobile();

        mDataBinding.setHasAccessNetworkState(hasAccessNetworkState);
        mDataBinding.setIsConnected(isConnected);
        mDataBinding.setIsWifi(isWifi);
        mDataBinding.setIsMobile(isMobile);
    }

}
