package com.paditech.cvmarker.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.paditech.cvmarker.R;
import com.paditech.cvmarker.base.BaseActivity;
import com.paditech.cvmarker.model.Personal;
import com.paditech.cvmarker.utils.Constant;
import com.paditech.cvmarker.utils.LocaleUtil;
import com.paditech.cvmarker.utils.PreferenceUtils;
import com.paditech.cvmarker.utils.StringUtils;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by USER on 20/6/2016.
 */
public class SettingsActivity extends BaseActivity {

    private final static String CV_EMAIL = "contact@paditech.com";
    private final static String link = "https://link_to_my_app";

    @InjectView(R.id.btn_is_default)
    ImageView btnIsDefault;

    @InjectView(R.id.personal_layout)
    View mPersonalLayout;

    @InjectView(R.id.input_name)
    EditText mInputName;

    @InjectView(R.id.input_address)
    EditText mInputAddress;

    @InjectView(R.id.input_email)
    EditText mInputEmail;

    @InjectView(R.id.input_phone)
    EditText mInputPhone;

    @InjectView(R.id.text_language)
    TextView mTextLang;

    @InjectView(R.id.main_layout)
    View mMainLayout;

    @InjectView(R.id.line)
    View mLine;

    public Personal personal;
    private boolean isDefault;

    @Override
    protected void initView() {
        isDefault= PreferenceUtils.getPesonalIsDefault(this);
        if(isDefault) {
            mPersonalLayout.setVisibility(View.VISIBLE);
            btnIsDefault.setImageDrawable(getResources().getDrawable(R.mipmap.ic_checked));

        } else {
            mPersonalLayout.setVisibility(View.GONE);
            btnIsDefault.setImageDrawable(getResources().getDrawable(R.mipmap.ic_no_checked));
        }
        personal = PreferenceUtils.getDefaultPesonal(this);
        if(personal == null) personal = new Personal();
        else setForm();

        hideKeyBoard(mMainLayout);

    }

    private void hideKeyBoard(View view) {
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    LocaleUtil.hideSoftKeyboard(SettingsActivity.this);
                    return false;
                }
            });
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    protected String getHeaderTitle() {
        return getResources().getString(R.string.settings);
    }

    @Override
    protected Drawable getLeftDrawable() {
        return getResources().getDrawable(R.mipmap.ic_back);
    }

    @Override
    protected Drawable getRightDrawable() {
        return null;
    }

    @OnClick(R.id.btn_left)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.btn_is_default)
    public void setIsDefaultClick() {
        if(isDefault) {
            mPersonalLayout.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
            btnIsDefault.setImageDrawable(getResources().getDrawable(R.mipmap.ic_no_checked));
            isDefault = false;
        } else {
            mLine.setVisibility(View.VISIBLE);
            mPersonalLayout.setVisibility(View.VISIBLE);
            btnIsDefault.setImageDrawable(getResources().getDrawable(R.mipmap.ic_checked));
            isDefault = true;
        }
        PreferenceUtils.setPersonalIsDefault(this, isDefault);
    }

    @OnClick(R.id.rate_app_layout)
    public void onRateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    @OnClick(R.id.feedback_layout)
    public void onFeedback() {
        String[] to = {CV_EMAIL};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , to);
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

    @OnClick(R.id.share_app_layout)
    public void onShareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Install CVMaker to create your resume faster!");
        intent.putExtra(Intent.EXTRA_TEXT, link);
        startActivity(Intent.createChooser(intent, "Please Choose Method"));
    }

    @OnClick(R.id.language_layout)
    public void onSelectLang() {
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

        String lang = PreferenceUtils.getDefaultLanguage(this);
        if(lang.equals(Constant.ENGLISH)) {
            en.setChecked(true);
        } else if (lang.equals(Constant.VIETNAMESE)) vi.setChecked(true);
        else ja.setChecked(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(group);
        builder.setCancelable(true);
        builder.setMessage(getResources().getString(R.string.select_language));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(en.isChecked()) {
                    setLanguage(Constant.ENGLISH);
                    mTextLang.setText(getResources().getString(R.string.language)
                            +": " + getResources().getString(R.string.english));
                    PreferenceUtils.saveDefaultLanguage(SettingsActivity.this, Constant.ENGLISH);
                }
                else {
                    if(vi.isChecked()) {
                        setLanguage(Constant.VIETNAMESE);
                        mTextLang.setText(getResources().getString(R.string.language)
                                +": " + getResources().getString(R.string.vietnamese));
                        PreferenceUtils.saveDefaultLanguage(SettingsActivity.this, Constant.VIETNAMESE);
                    } else {
                        if(ja.isChecked()) {
                            setLanguage(Constant.JAPAN);
                            mTextLang.setText(getResources().getString(R.string.language)
                                    +": " + getResources().getString(R.string.japan));
                            PreferenceUtils.saveDefaultLanguage(SettingsActivity.this, Constant.JAPAN);
                        }
                    }

                }

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);

        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveForm();
    }

    public void saveForm() {
        if(!StringUtils.isEmpty(mInputName.getText().toString().trim())) {
            personal.setName(mInputName.getText().toString().trim());
        }
        if(!StringUtils.isEmpty(mInputEmail.getText().toString().trim())) {
            personal.setEmail(mInputEmail.getText().toString().trim());
        }
        if(!StringUtils.isEmpty(mInputAddress.getText().toString().trim())) {
            personal.setAddress(mInputAddress.getText().toString().trim());
        }
        if(!StringUtils.isEmpty(mInputPhone.getText().toString().trim())) {
            personal.setPhoneNumber(mInputPhone.getText().toString().trim());
        }

        if(isDefault) PreferenceUtils.setDefaultPersonal(this, personal);
    }

    private void setForm() {
        if(personal != null) {
            if (!StringUtils.isEmpty(personal.getName())) {
                mInputName.setText(personal.getName());
            }

            if (!StringUtils.isEmpty(personal.getPhoneNumber())) {
                mInputPhone.setText(personal.getPhoneNumber());
            }

            if (!StringUtils.isEmpty(personal.getAddress())) {
                mInputAddress.setText(personal.getAddress());
            }

            if (!StringUtils.isEmpty(personal.getEmail())) {
                mInputEmail.setText(personal.getEmail());
            }
        } else personal = new Personal();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);

        LinearLayout mRootAds=(LinearLayout)findViewById(R.id.root_ads);
        adView.setAdUnitId(getResources().getString(R.string.banner_home_footer));
        mRootAds.addView(adView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        AdRequest ar = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(ar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String lang = PreferenceUtils.getDefaultLanguage(this);
        Log.e("Language", lang);
        if(lang.equals(Constant.VIETNAMESE)) mTextLang.setText(getResources().getString(R.string.language)
                +": " + getResources().getString(R.string.vietnamese));
        else {
            mTextLang.setText(getResources().getString(R.string.language)
                    +": " + getResources().getString(R.string.english));
        }
    }
}
