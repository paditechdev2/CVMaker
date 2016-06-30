package com.paditech.cvmarker.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.paditech.cvmarker.R;
import com.paditech.cvmarker.adapter.FillOneByOneAdapter;
import com.paditech.cvmarker.base.BaseActivity;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.utils.Constant;
import com.paditech.cvmarker.utils.LocaleUtil;
import com.paditech.cvmarker.utils.PreferenceUtils;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by USER on 12/6/2016.
 */
public class FillOneByOneActivity extends BaseActivity {

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    @InjectView(R.id.continue_button)
    Button mContinue;

    @InjectView(R.id.reset)
    Button mReset;

    private FillOneByOneAdapter adapter;
    private Resume resume;

    private int mCurrFragment = 0;
    private int index = 0;


    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        resume = new Resume();
        if(bundle != null) {
            mCurrFragment = bundle.getInt(Constant.FILL_TYPE);
            index = bundle.getInt(Constant.RESUME_INDEX);
            resume = PreferenceUtils.getResume(this, index);
        }
        adapter = new FillOneByOneAdapter(getSupportFragmentManager(), this, resume);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mCurrFragment);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrFragment = position;
                setHeaderContent(mCurrFragment);
                LocaleUtil.hideSoftKeyboard(FillOneByOneActivity.this);
                if(mCurrFragment == 5) {
                    mContinue.setText(getResources().getString(R.string.complete));
                } else mContinue.setText(getResources().getString(R.string.next));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setHeaderContent(mCurrFragment);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fill_one_by_one_form;
    }

    @Override
    protected String getHeaderTitle() {
        switch (mCurrFragment) {
            case 0: return getString(R.string.personal_info);
            case 1: return getString(R.string.objective);
            case 2: return getString(R.string.education);
            case 3: return getString(R.string.work_experence);
            case 4: return getString(R.string.skills);
            case 5: return getString(R.string.other_info);
        }
        return getString(R.string.personal_info);
    }

    @Override
    protected Drawable getLeftDrawable() {
        return getResources().getDrawable(R.mipmap.ic_back);
    }

    @Override
    protected Drawable getRightDrawable() {
        return null;
    }

    private void savePreference() {
        adapter.savePersonalForm(mCurrFragment);
        List<Resume> list = PreferenceUtils.getListResume(this);
        if(index < list.size()) list.set(index, adapter.getResume());
        PreferenceUtils.saveListResume(this, list);
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreference();
    }

    @OnClick(R.id.btn_left)
    public void backClick() {
        finish();
    }

    @OnClick(R.id.btn_right)
    public void doneClick() {
        adapter.onDoneClicked(mCurrFragment);
    }

    @OnClick(R.id.btn_right)
    public void addClick() {
        adapter.onAddClicked(mCurrFragment);
    }

    @OnClick(R.id.reset_button)
    public void addNew(){
        adapter.onDoneClicked(mCurrFragment);
        adapter.onResetClicked(mCurrFragment);
    }

    @OnClick(R.id.reset)
    public void resetClick(){
        adapter.onResetClicked(mCurrFragment);
    }

    @OnClick(R.id.continue_button)
    public void continueClick() {
        if(mCurrFragment < 5) {
            boolean doContinue = adapter.doContinue(mCurrFragment);
            if (doContinue == true) {
                doneClick();
                mViewPager.setCurrentItem(mCurrFragment + 1);
            }
        } else {
            boolean doContinue = adapter.doContinue(mCurrFragment);
            if (doContinue == true) {
                doneClick();
                finish();
            }
        }
    }

    @OnClick(R.id.bottom_bar_layout)
    public void hideKeyboard() {
        LocaleUtil.hideSoftKeyboard(this);
    }

    @OnClick(R.id.header)
    public void hideKey() {
        LocaleUtil.hideSoftKeyboard(this);
    }

    public void setHeaderContent( int index) {
        switch (index) {
            case 0:
                setHeader(getResources().getString(R.string.personal_info), getLeftDrawable(), null);
                break;
            case 1:
                setHeader(getResources().getString(R.string.objective), getLeftDrawable(), null);
                break;
            case 2:
                setHeader(getResources().getString(R.string.education), getLeftDrawable(), null);
                break;
            case 3:
                setHeader(getResources().getString(R.string.work_experence), getLeftDrawable(), null);
                break;
            case 4:
                setHeader(getResources().getString(R.string.skills), getLeftDrawable(), null);
                break;
            case 5:
                setHeader(getResources().getString(R.string.other_info), getLeftDrawable(), null);
                break;
        }
        mReset.setVisibility(View.VISIBLE);
    }

}
