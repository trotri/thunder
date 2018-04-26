package com.trotri.android.java.sample.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentFileSdCardBinding;
import com.trotri.android.java.sample.view.vm.FileSdCardViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.thunder.file.SdCard;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * FileSdCardFragment class file
 * SD Card文件读写视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: FileSdCardFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(FileSdCardViewModel.class)
public class FileSdCardFragment extends BaseFragment<FileSdCardViewModel> implements View.OnClickListener {

    public static final String TAG = "FileSdCardFragment";

    private FragmentFileSdCardBinding mDataBinding;

    public static BaseFragment newInstance() {
        return new FileSdCardFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_file_sd_card;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding = FragmentFileSdCardBinding.bind(view);
        mDataBinding.fileIo.tvFileName.setText(FileSdCardViewModel.FILE_NAME);
        mDataBinding.fileIo.btnWrite.setText(mDataBinding.fileIo.btnWrite.getText() + FileSdCardViewModel.WRITE_DATA);
        mDataBinding.fileIo.btnAppendWrite.setText(mDataBinding.fileIo.btnAppendWrite.getText() + FileSdCardViewModel.WRITE_DATA);
    }

    @Override
    public void onStart() {
        super.onStart();

        refreshResult();

        mDataBinding.fileIo.btnWrite.setOnClickListener(this);
        mDataBinding.fileIo.btnAppendWrite.setOnClickListener(this);
    }

    public void refreshResult() {
        mViewModel.read(new SdCard.Listener() {
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
            mViewModel.write(new SdCard.Listener() {
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
            mViewModel.appendWrite(new SdCard.Listener() {
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
