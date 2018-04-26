package com.trotri.android.java.sample.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.trotri.android.java.sample.R;
import com.trotri.android.java.sample.databinding.FragmentRoundedImageViewBinding;
import com.trotri.android.java.sample.view.vm.RoundedImageViewViewModel;
import com.trotri.android.library.base.BaseFragment;
import com.trotri.android.library.view.RoundedImageView;
import com.trotri.android.thunder.inj.ViewModelInject;

/**
 * RoundedImageViewFragment class file
 * 圆角图片视图
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: RoundedImageViewFragment.java 1 2017-05-01 10:00:06Z huan.song $
 * @since 1.0
 */
@ViewModelInject(RoundedImageViewViewModel.class)
public class RoundedImageViewFragment extends BaseFragment<RoundedImageViewViewModel> {

    public static final String TAG = "RoundedImageViewFragment";

    private RoundedImageView.Builder mIvBuilder;

    private LinearLayout.LayoutParams mIvLayoutParams;

    public static BaseFragment newInstance() {
        return new RoundedImageViewFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_rounded_image_view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentRoundedImageViewBinding dataBinding = FragmentRoundedImageViewBinding.bind(view);

        mIvBuilder = new RoundedImageView.Builder(getContext());

        Resources res = getResources();
        float layoutWidth = res.getDimension(R.dimen.rounded_image_view_layout_width);
        float layoutHeight = res.getDimension(R.dimen.rounded_image_view_layout_height);
        float verticalMargin = res.getDimension(R.dimen.wrapper_vertical_margin);
        float cornerRadiusLeftTop = res.getDimension(R.dimen.rounded_image_view_cornerRadius_leftTop);
        float cornerRadiusRightTop = res.getDimension(R.dimen.rounded_image_view_cornerRadius_rightTop);
        float cornerRadiusRightBottom = res.getDimension(R.dimen.rounded_image_view_cornerRadius_rightBottom);
        float cornerRadiusLeftBottom = res.getDimension(R.dimen.rounded_image_view_cornerRadius_leftBottom);

        mIvLayoutParams = new LinearLayout.LayoutParams((int) layoutWidth, (int) layoutHeight);
        mIvLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        mIvLayoutParams.topMargin = (int) verticalMargin;
        mIvLayoutParams.bottomMargin = mIvLayoutParams.topMargin;

        dataBinding.llayWrapper.addView(getImageView());

        mIvBuilder.setLeftTopCornerRadius(cornerRadiusLeftTop);
        mIvBuilder.setRightTopCornerRadius(cornerRadiusRightTop);
        mIvBuilder.setRightBottomCornerRadius(cornerRadiusRightBottom);
        mIvBuilder.setLeftBottomCornerRadius(cornerRadiusLeftBottom);
        mIvBuilder.setCircular(false);

        dataBinding.llayWrapper.addView(getImageView());

        mIvBuilder.setCircular(true);
        dataBinding.llayWrapper.addView(getImageView());
    }

    private RoundedImageView getImageView() {
        RoundedImageView view = mIvBuilder.create();

        view.setLayoutParams(mIvLayoutParams);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setImageResource(R.mipmap.totoro);

        return view;
    }

}
