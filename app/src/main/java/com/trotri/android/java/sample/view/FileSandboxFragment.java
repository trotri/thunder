package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentFileSandboxBinding;
import com.trotri.android.java.sample.view.vm.FileSandboxViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.thunder.file.Sandbox;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * FileSandboxFragment class file
 * 应用沙盒文件读写视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: FileSandboxFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(FileSandboxViewModel.class)
public class FileSandboxFragment extends BaseFragment<FileSandboxViewModel> implements View.OnClickListener {

    public static final String TAG = "FileSandboxFragment";

    private FragmentFileSandboxBinding mDataBinding;

    public static BaseFragment newInstance() {
        return new FileSandboxFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_file_sandbox;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentFileSandboxBinding.bind(view);
        mDataBinding.fileIo.tvFileName.setText(FileSandboxViewModel.FILE_NAME);
        mDataBinding.fileIo.btnWrite.setText(mDataBinding.fileIo.btnWrite.getText() + FileSandboxViewModel.WRITE_DATA);
        mDataBinding.fileIo.btnAppendWrite.setText(mDataBinding.fileIo.btnAppendWrite.getText() + FileSandboxViewModel.WRITE_DATA);
    }

    @Override
    public void onStart() {
        super.onStart();

        refreshResult();

        mDataBinding.fileIo.btnWrite.setOnClickListener(this);
        mDataBinding.fileIo.btnAppendWrite.setOnClickListener(this);
    }

    public void refreshResult() {
        mViewModel.read(new Sandbox.Listener() {
            @Override
            public void onComplete(String data) {
                mDataBinding.fileIo.tvContent.setText(data);
            }

            @Override
            public void onError(Throwable tr) {
                Toast.makeText(getActivity(), tr.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mDataBinding.fileIo.btnWrite) {
            mViewModel.write(new Sandbox.Listener() {
                @Override
                public void onComplete(String data) {
                    refreshResult();
                }

                @Override
                public void onError(Throwable tr) {
                    Toast.makeText(getActivity(), tr.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (v == mDataBinding.fileIo.btnAppendWrite) {
            mViewModel.appendWrite(new Sandbox.Listener() {
                @Override
                public void onComplete(String data) {
                    refreshResult();
                }

                @Override
                public void onError(Throwable tr) {
                    Toast.makeText(getActivity(), tr.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
