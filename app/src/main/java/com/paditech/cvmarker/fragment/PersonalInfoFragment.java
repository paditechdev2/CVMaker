package com.paditech.cvmarker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.paditech.cvmarker.R;
import com.paditech.cvmarker.base.BaseFragment;
import com.paditech.cvmarker.model.Personal;
import com.paditech.cvmarker.utils.LocaleUtil;
import com.paditech.cvmarker.utils.StringUtils;

import butterknife.InjectView;

/**
 * Created by USER on 12/6/2016.
 */
public class PersonalInfoFragment extends BaseFragment {

    @InjectView(R.id.input_name)
    EditText mInputName;

    @InjectView(R.id.input_address)
    EditText mInputAddress;

    @InjectView(R.id.input_email)
    EditText mInputEmail;

    @InjectView(R.id.input_phone)
    EditText mInputPhone;

    public Personal personal;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        hideKeyBoard(view);
        return view;
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
    public void onPause() {
        super.onPause();
        saveForm();
    }

    public boolean doContinue() {
        if(StringUtils.isEmpty(mInputName.getText().toString().trim())) {
            mInputName.requestFocus();
            return false;
        } else {
            if(StringUtils.isEmpty(mInputAddress.getText().toString().trim())) {
                mInputAddress.requestFocus();
                return false;
            } else {
                if(StringUtils.isEmpty(mInputEmail.getText().toString().trim())) {
                    mInputEmail.requestFocus();
                    return false;
                } else {
                    if(StringUtils.isEmpty(mInputPhone.getText().toString().trim())) {
                        mInputPhone.requestFocus();
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    public void onReset() {
        mInputPhone.setText("");
        mInputEmail.setText("");
        mInputAddress.setText("");
        mInputName.setText("");
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
    }

    @Override
    protected void initView() {
        setForm();
        LocaleUtil.hideSoftKeyboard(getActivity());
    }

    private void hideKeyBoard(View view) {
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    LocaleUtil.hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }
    }
}
