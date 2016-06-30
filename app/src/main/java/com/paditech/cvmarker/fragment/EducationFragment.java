package com.paditech.cvmarker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.paditech.cvmarker.R;
import com.paditech.cvmarker.adapter.EducationAdapter;
import com.paditech.cvmarker.base.BaseFragment;
import com.paditech.cvmarker.model.Education;
import com.paditech.cvmarker.utils.DialogUtil;
import com.paditech.cvmarker.utils.LocaleUtil;
import com.paditech.cvmarker.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by USER on 12/6/2016.
 */
public class EducationFragment extends BaseFragment implements EducationAdapter.OnEducationItemClicked {

    @InjectView(R.id.list_education)
    ExpandableHeightListView mListView;

    @InjectView(R.id.scroll_view)
    ScrollView mScrollView;

    @InjectView(R.id.input_school_name)
    EditText mSchoolName;

    @InjectView(R.id.input_school_address)
    EditText mSchoolAddr;

    @InjectView(R.id.input_school_degree)
    EditText mSchoolDegree;

    @InjectView(R.id.input_school_major)
    EditText mSchoolMajor;

    @InjectView(R.id.btn_date1)
    Button mBtnDate1;

    @InjectView(R.id.btn_date2)
    Button mBtnDate2;

    private String date = "";
    public List<Education> mListEducation;
    private EducationAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_education, container, false);
        hideKeyBoard(view);
        return view;
    }

    @Override
    protected void initView() {
        if (mListEducation == null || mListEducation.size() == 0) {
            mListEducation = new ArrayList<>();
        }
        mAdapter = new EducationAdapter(getActivity(), mListEducation);

        mListView.setAdapter(mAdapter);
        mListView.setExpanded(true);
        mAdapter.setmOnObjectiveItemClicked(this);

        mBtnDate2.setText(getActivity().getResources().getString(R.string.now).toLowerCase());
    }

    @OnClick(R.id.btn_date1)
    public void fromDate() {
        DatePickerFragment dialog1 = new DatePickerFragment();
        dialog1.show(getActivity().getSupportFragmentManager(), "date_picker");
        dialog1.setDateSelectedListener(new DatePickerFragment.OnDatePicked() {
            @Override
            public void onDateSelected(int month, int year) {
                mBtnDate1.setText(month + "/" + year);
            }
        });
    }

    @OnClick(R.id.btn_date2)
    public void toDate() {
        DatePickerFragment dialog2 = new DatePickerFragment();
        dialog2.show(getActivity().getSupportFragmentManager(), "date_picker");
        dialog2.setDateSelectedListener(new DatePickerFragment.OnDatePicked() {
            @Override
            public void onDateSelected(int month, int year) {

                mBtnDate2.setText(month + "/" + year);
            }
        });
    }

    @OnClick(R.id.done_button)
    public void addDone() {
        LocaleUtil.hideSoftKeyboard(getActivity());
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        Education education = new Education();
        if (!StringUtils.isEmpty(mSchoolAddr.getText().toString())) {
            education.setLocation(mSchoolAddr.getText().toString());
        }
        if (!StringUtils.isEmpty(mSchoolName.getText().toString())) {
            education.setSchoolName(mSchoolName.getText().toString());
        }

        if (!StringUtils.isEmpty(mSchoolDegree.getText().toString())) {
            education.setDegree(mSchoolDegree.getText().toString());
        }
        if (!StringUtils.isEmpty(mSchoolMajor.getText().toString())) {
            education.setMajor(mSchoolMajor.getText().toString());
        }

        date = mBtnDate1.getText().toString().trim();

        if (!StringUtils.isEmpty(mBtnDate2.getText().toString())) {
            date += "-" + mBtnDate2.getText().toString();
        }
        if (!StringUtils.isEmpty(date)) {
            education.setDatePeriod(date);
        }

        if (Education.educationIsEmpty(education)) {
        } else {
            mListEducation.add(education);
            mAdapter.setListObjective(mListEducation);
            reset();
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        addDone();
    }

    @Override
    public void onObjectiveItemClicked(int position) {
        Education education = mListEducation.get(position);
        mSchoolAddr.setText(education.getLocation());
        mSchoolMajor.setText(education.getMajor());
        mSchoolDegree.setText(education.getDegree());
        mSchoolName.setText(education.getSchoolName());
        date = education.getDatePeriod();
        String ds[] = date.split("-");
        if (ds.length == 1) {
            mBtnDate1.setText(ds[0]);
            mBtnDate2.setText("");
        } else if (ds.length == 2) {
            mBtnDate1.setText(ds[0]);
            mBtnDate2.setText(ds[1]);
        }

        mListEducation.remove(position);
        mAdapter.setListObjective(mListEducation);
    }

    public void reset() {
        mSchoolAddr.setText("");
        mSchoolMajor.setText("");
        mSchoolDegree.setText("");
        mBtnDate1.setText("");
        mBtnDate2.setText(getActivity().getResources().getString(R.string.now).toLowerCase());
        mSchoolName.setText("");

    }

    public boolean doContinue() {

        if (StringUtils.isEmpty(mSchoolName.getText().toString().trim()) && StringUtils.isEmpty(mSchoolAddr.getText().toString().trim())
                && StringUtils.isEmpty(mSchoolDegree.getText().toString().trim()) && StringUtils.isEmpty(mSchoolMajor.getText().toString().trim())
                && StringUtils.isEmpty(mBtnDate1.getText().toString().trim())) return true;
        else if (StringUtils.isEmpty(mSchoolName.getText().toString().trim())) {
            mSchoolName.requestFocus();
            return false;
        } else {
            if (StringUtils.isEmpty(mSchoolAddr.getText().toString().trim())) {
                mSchoolAddr.requestFocus();
                return false;
            } else {
                if (StringUtils.isEmpty(mSchoolDegree.getText().toString().trim())) {
                    mSchoolDegree.requestFocus();
                    return false;
                } else {
                    if (StringUtils.isEmpty(mSchoolMajor.getText().toString().trim())) {
                        mSchoolMajor.requestFocus();
                        return false;
                    } else {
                        if (StringUtils.isEmpty(mBtnDate1.getText().toString().trim())) {
                            fromDate();
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
    }

    private void hideKeyBoard(View view) {
        if (!(view instanceof EditText)) {
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
