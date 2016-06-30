package com.paditech.cvmarker.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.paditech.cvmarker.R;
import com.paditech.cvmarker.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by USER on 10/6/2016.
 */
public abstract class BaseActivity extends LocalizationActivity {

    @InjectView(R.id.header_title_text)
    TextView mHeaderText;

    @InjectView(R.id.btn_left)
    ImageView mLeftButton;

    @InjectView(R.id.btn_right)
    ImageView mRightButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.inject(this);
        initView();
        setHeader();
    }

    protected abstract void initView();

    protected abstract int getContentView();

    protected abstract String getHeaderTitle();

    protected abstract Drawable getLeftDrawable();

    protected abstract Drawable getRightDrawable();


    private void setHeader() {
        if(!StringUtils.isEmpty(getHeaderTitle())) {
            if(mHeaderText != null) {
                mHeaderText.setText(getHeaderTitle());
                if (android.os.Build.VERSION.SDK_INT >= 21)
                    setStatusBarColor();
            }
            if(mLeftButton != null && getLeftDrawable()!=null) {
                mLeftButton.setImageDrawable(getLeftDrawable());
            }
            if(mRightButton != null && getRightDrawable() != null) {
                mRightButton.setImageDrawable(getRightDrawable());
            }
        }
    }

    public void setHeader(String headerTitle, Drawable left, Drawable right) {
        if(!StringUtils.isEmpty(headerTitle)) {
            if(mHeaderText != null) {
                mHeaderText.setText(headerTitle);
                if (android.os.Build.VERSION.SDK_INT >= 21)
                    setStatusBarColor();
            }
            if(mLeftButton != null && left !=null) {
                mLeftButton.setImageDrawable(left);
            }
            if(mRightButton != null && right != null) {
                mRightButton.setImageDrawable(right);
            }
        }
    }

    private void setStatusBarColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.green_dark));
    }

}
