package com.paditech.cvmarker.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.paditech.cvmarker.R;
import com.paditech.cvmarker.base.BaseActivity;
import com.paditech.cvmarker.model.Personal;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.utils.Constant;
import com.paditech.cvmarker.utils.LocaleUtil;
import com.paditech.cvmarker.utils.PDFUtils;
import com.paditech.cvmarker.utils.PreferenceUtils;
import com.paditech.cvmarker.utils.StringUtils;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by USER on 10/6/2016.
 */
public class FillResumeDetailActivity extends BaseActivity {

    @InjectView(R.id.personal_filter)
    RelativeLayout mPersonalFilter;

    @InjectView(R.id.personal_button)
    RelativeLayout mPersonalButton;

    @InjectView(R.id.objective_button)
    RelativeLayout mObjectiveButton;

    @InjectView(R.id.education_button)
    RelativeLayout mEducationButton;

    @InjectView(R.id.experience_button)
    RelativeLayout mExperButton;

    @InjectView(R.id.skill_button)
    RelativeLayout mSkillButton;

    @InjectView(R.id.custom_button)
    RelativeLayout mCustomButton;

    @InjectView(R.id.objective_filter)
    RelativeLayout mObjectiveFilter;

    @InjectView(R.id.education_filter)
    RelativeLayout mEducationFilter;

    @InjectView(R.id.experience_filter)
    RelativeLayout mExperienceFilter;

    @InjectView(R.id.skill_filter)
    RelativeLayout mSkillFilter;

    @InjectView(R.id.custom_filter)
    RelativeLayout mCustomFilter;

    @InjectView(R.id.text_language)
    TextView mTextLanguage;

    @InjectView(R.id.text_style)
    TextView mTextStyle;

    private Resume resume;
    private int index;

    @Override
    protected void initView() {
        resume = new Resume();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            index = bundle.getInt(Constant.RESUME_INDEX);
            resume = PreferenceUtils.getResume(this, index);
        }

        float w = (LocaleUtil.getWidthScreen(this) - 70)/2;
        float h = (float) (w* 0.7);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) w, (int) h);
        mCustomButton.setLayoutParams(params);
        mCustomFilter.setLayoutParams(params);

        mPersonalButton.setLayoutParams(params);
        mPersonalFilter.setLayoutParams(params);

        mEducationButton.setLayoutParams(params);
        mEducationFilter.setLayoutParams(params);

        mExperienceFilter.setLayoutParams(params);
        mExperButton.setLayoutParams(params);

        mSkillFilter.setLayoutParams(params);
        mSkillButton.setLayoutParams(params);

        mObjectiveButton.setLayoutParams(params);
        mObjectiveFilter.setLayoutParams(params);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fill_resume_detail;
    }

    @Override
    protected String getHeaderTitle() {
        if(!StringUtils.isEmpty(resume.getName()))
            return resume.getName();
        return "";
    }

    @Override
    protected Drawable getLeftDrawable() {
        return getResources().getDrawable(R.drawable.btn_back_pressed);
    }

    @Override
    protected Drawable getRightDrawable() {
        return getResources().getDrawable(R.drawable.btn_preview_pressed);
    }

    @OnClick(R.id.personal_button)
    public void personalInfo() {
        Intent intent = new Intent(this, FillOneByOneActivity.class);
        intent.putExtra(Constant.RESUME_INDEX, index);
        intent.putExtra(Constant.FILL_TYPE, 0);
        startActivity(intent);
    }

    @OnClick(R.id.objective_button)
    public void objective() {
        Intent intent = new Intent(this, FillOneByOneActivity.class);
        intent.putExtra(Constant.RESUME_INDEX, index);
        intent.putExtra(Constant.FILL_TYPE, 1);
        startActivity(intent);
    }

    @OnClick(R.id.education_button)
    public void educaton() {
        Intent intent = new Intent(this, FillOneByOneActivity.class);
        intent.putExtra(Constant.RESUME_INDEX, index);
        intent.putExtra(Constant.FILL_TYPE, 2);
        startActivity(intent);
    }

    @OnClick(R.id.experience_button)
    public void experience() {
        Intent intent = new Intent(this, FillOneByOneActivity.class);
        intent.putExtra(Constant.RESUME_INDEX, index);
        intent.putExtra(Constant.FILL_TYPE, 3);
        startActivity(intent);
    }

    @OnClick(R.id.skill_button)
    public void skill() {
        Intent intent = new Intent(this, FillOneByOneActivity.class);
        intent.putExtra(Constant.RESUME_INDEX, index);
        intent.putExtra(Constant.FILL_TYPE, 4);
        startActivity(intent);
    }

    @OnClick(R.id.custom_button)
    public void otherSection() {
        Intent intent = new Intent(this, FillOneByOneActivity.class);
        intent.putExtra(Constant.RESUME_INDEX, index);
        intent.putExtra(Constant.FILL_TYPE, 5);
        startActivity(intent);
    }

    @OnClick(R.id.template_button)
    public void template() {
        Intent intent = new Intent(this, TemplateResumeActivity.class);
        intent.putExtra(Constant.RESUME_INDEX, index);
        intent.putExtra(Constant.RESUME_STYLE, resume.getStyle());
        startActivity(intent);
    }

    private void setUI() {

        resume = PreferenceUtils.getResume(this, index);

        if(!StringUtils.isEmpty(resume.getLanguage())) {
            if(resume.getLanguage().equals(Constant.VIETNAMESE)) {
                mTextLanguage.setText(getResources().getString(R.string.vietnamese));
            }
        } else {
            mTextLanguage.setText(getResources().getString(R.string.english));
        }

        if(!Personal.personalIsEmpty(resume.getPersonal())) {
            mPersonalFilter.setBackgroundColor(getResources().getColor(R.color.tranferance));
        } else {
            mPersonalFilter.setBackgroundColor(getResources().getColor(R.color.white_tranfer));
        }
        if(resume.getObjectiveList() == null || resume.getObjectiveList().size() == 0) {
            mObjectiveFilter.setBackgroundColor(getResources().getColor(R.color.white_tranfer));
        } else {
            mObjectiveFilter.setBackgroundColor(getResources().getColor(R.color.tranferance));
        }

        if(resume.getEducationList() == null || resume.getEducationList().size() == 0) {
            mEducationFilter.setBackgroundColor(getResources().getColor(R.color.white_tranfer));
        } else {
            mEducationFilter.setBackgroundColor(getResources().getColor(R.color.tranferance));
        }

        if(resume.getWorkExperienceList() == null || resume.getWorkExperienceList().size() == 0) {
            mExperienceFilter.setBackgroundColor(getResources().getColor(R.color.white_tranfer));
        } else {
            mExperienceFilter.setBackgroundColor(getResources().getColor(R.color.tranferance));
        }

        if(resume.getSkillList() == null || resume.getSkillList().size() == 0) {
            mSkillFilter.setBackgroundColor(getResources().getColor(R.color.white_tranfer));
        } else {
            mSkillFilter.setBackgroundColor(getResources().getColor(R.color.tranferance));
        }

        if(resume.getOtherInfoList() == null || resume.getOtherInfoList().size() == 0) {
            mCustomFilter.setBackgroundColor(getResources().getColor(R.color.white_tranfer));
        } else {
            mCustomFilter.setBackgroundColor(getResources().getColor(R.color.tranferance));
        }



        if(!StringUtils.isEmpty(PDFUtils.getStyleName(resume.getStyle()))) {
            mTextStyle.setText(PDFUtils.getStyleName(resume.getStyle()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUI();
        setLanguage(PreferenceUtils.getDefaultLanguage(this));
    }

    @OnClick(R.id.btn_edit_section)
    public void onOpenEdit() {
        Intent intent = new Intent(this, FillOneByOneActivity.class);
        intent.putExtra(Constant.RESUME_INDEX, index);
        intent.putExtra(Constant.FILL_TYPE, 0);
        startActivity(intent);
    }

    @OnClick(R.id.btn_left)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.btn_right)
    public void onPreView() {
        Intent intent = new Intent(this, TemplateResumeActivity.class);
        intent.putExtra(Constant.PREVIEW_TYPE, 1);
        intent.putExtra(Constant.RESUME_INDEX, index);
        intent.putExtra(Constant.RESUME_NAME, resume.getName());
        startActivity(intent);
    }

    @OnClick(R.id.language_button)
    public void onLanguageChange() {
        final RadioGroup group = new RadioGroup(this);
        group.setPadding(10,10,10,10);
        group.setOrientation(LinearLayout.HORIZONTAL);
        group.setGravity(Gravity.CENTER);
        group.setBackgroundColor(getResources().getColor(R.color.tranferance));
        final RadioButton en = new RadioButton(this);
        en.setText(getResources().getString(R.string.english));
        en.setTextColor(getResources().getColor(R.color.green_dark));

        final RadioButton vi = new RadioButton(this);
        vi.setText(getResources().getString(R.string.vietnamese));
        vi.setTextColor(getResources().getColor(R.color.green_dark));

        final RadioButton ja = new RadioButton(this);
        ja.setText(getResources().getString(R.string.japan));
        ja.setTextColor(getResources().getColor(R.color.green_dark));

        group.addView(en);
        group.addView(vi);
        group.addView(ja);

        if(mTextLanguage.getText().equals(getResources().getString(R.string.english))) {
            en.setChecked(true);
        } else if(mTextLanguage.getText().equals(getResources().getString(R.string.vietnamese))) vi.setChecked(true);
        else ja.setChecked(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(group);
        builder.setMessage(getResources().getString(R.string.select_language));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(en.isChecked()) {
                    mTextLanguage.setText(getResources().getString(R.string.english));
                    PreferenceUtils.saveLanguage(FillResumeDetailActivity.this, index,Constant.ENGLISH);
                }
                else {
                    if(vi.isChecked()) {
                        mTextLanguage.setText(getResources().getString(R.string.vietnamese));
                        PreferenceUtils.saveLanguage(FillResumeDetailActivity.this, index, Constant.VIETNAMESE);
                    } else {
                        if(ja.isChecked()) {
                            mTextLanguage.setText(getResources().getString(R.string.japan));
                            PreferenceUtils.saveLanguage(FillResumeDetailActivity.this, index, Constant.JAPAN);
                        }
                    }
                }
                initView();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);

        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);

        LinearLayout mRootAds=(LinearLayout)findViewById(R.id.root_ads);
        adView.setAdUnitId(getResources().getString(R.string.banner_home_footer));
        mRootAds.addView(adView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        AdRequest ar = new AdRequest.Builder().build();
        adView.loadAd(ar);
    }



}
