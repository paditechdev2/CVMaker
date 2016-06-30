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
import com.paditech.cvmarker.adapter.ExperienceAdapter;
import com.paditech.cvmarker.base.BaseFragment;
import com.paditech.cvmarker.model.WorkExperience;
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
public class ExperienceFragment extends BaseFragment implements ExperienceAdapter.OnExpItemClicked {

    @InjectView(R.id.list_experience)
    ExpandableHeightListView mListView;

    @InjectView(R.id.scroll_view)
    ScrollView mScrollView;

    @InjectView(R.id.input_company_name)
    EditText mName;

    @InjectView(R.id.input_job_title)
    EditText mJobTitle;

    @InjectView(R.id.input_job_location)
    EditText mJobLocation;

    @InjectView(R.id.input_job_experience)
    EditText mJobExp;

    @InjectView(R.id.btn_date1)
    Button mBtnDate1;

    @InjectView(R.id.btn_date2)
    Button mBtnDate2;

    public List<WorkExperience> mListExp;
    private ExperienceAdapter mAdapter;
    private String date = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_experience, container, false);
        hideKeyBoard(view);
        return view;
    }

    @Override
    protected void initView() {
        if (mListExp == null || mListExp.size() == 0) {
            mListExp = new ArrayList<>();
        }
        mAdapter = new ExperienceAdapter(getActivity(), mListExp);

        mListView.setAdapter(mAdapter);
        mListView.setExpanded(true);
        mAdapter.setmOnExpItemClicked(this);
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

        WorkExperience experience = new WorkExperience();
        if (!StringUtils.isEmpty(mName.getText().toString())) {
            experience.setCompanyName(mName.getText().toString());
        }
        if (!StringUtils.isEmpty(mJobExp.getText().toString())) {
            experience.setJobResponsibility(mJobExp.getText().toString());
        }
        if (!StringUtils.isEmpty(mJobLocation.getText().toString())) {
            experience.setJobLocation(mJobLocation.getText().toString());
        }
        if (!StringUtils.isEmpty(mJobTitle.getText().toString())) {
            experience.setJobTitle(mJobTitle.getText().toString());
        }
        date = mBtnDate1.getText().toString().trim();

        if (!StringUtils.isEmpty(mBtnDate2.getText().toString())) {
            date += "-" + mBtnDate2.getText().toString();
        }
        if (!StringUtils.isEmpty(date)) {
            experience.setDatePeriod(date);
        }

        if (WorkExperience.expIsEmpty(experience)) {
        } else {
            mListExp.add(experience);
            mAdapter.setListObjective(mListExp);
            reset();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        addDone();
    }

    public boolean doContinue() {
        if (StringUtils.isEmpty(mName.getText().toString().trim()) && StringUtils.isEmpty(mJobTitle.getText().toString().trim())
                && StringUtils.isEmpty(mJobLocation.getText().toString().trim()) && StringUtils.isEmpty(mBtnDate1.getText().toString().trim())
                && StringUtils.isEmpty(mJobExp.getText().toString().trim())) return true;
        else if (StringUtils.isEmpty(mName.getText().toString().trim())) {
            mName.requestFocus();
            return false;
        } else {
            if (StringUtils.isEmpty(mJobTitle.getText().toString().trim())) {
                mJobTitle.requestFocus();
                return false;
            } else {
                if (StringUtils.isEmpty(mJobLocation.getText().toString().trim())) {
                    mJobLocation.requestFocus();
                    return false;
                } else {
                    if (StringUtils.isEmpty(mBtnDate1.getText().toString().trim())) {
                        fromDate();
                        return false;
                    } else {
                        if (StringUtils.isEmpty(mJobExp.getText().toString().trim())) {
                            mJobExp.requestFocus();
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
    }

    public void reset() {
        mJobLocation.setText("");
        mJobTitle.setText("");
        mJobExp.setText("");
        mBtnDate1.setText("");
        mBtnDate2.setText(getActivity().getResources().getString(R.string.now).toLowerCase());
        mName.setText("");

    }

    @Override
    public void onExpItemClick(int position) {
        WorkExperience experience = mListExp.get(position);
        mJobExp.setText(experience.getJobResponsibility());
        mJobTitle.setText(experience.getJobTitle());
        mJobLocation.setText(experience.getJobTitle());
        mName.setText(experience.getCompanyName());
        date = experience.getDatePeriod();
        String ds[] = date.split("-");
        if (ds.length == 1) {
            mBtnDate1.setText(ds[0]);
            mBtnDate2.setText("");
        } else if (ds.length == 2) {
            mBtnDate1.setText(ds[0]);
            mBtnDate2.setText(ds[1]);
        }

        mListExp.remove(position);
        mAdapter.setListObjective(mListExp);
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
