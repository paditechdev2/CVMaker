package com.paditech.cvmarker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.paditech.cvmarker.R;
import com.paditech.cvmarker.adapter.OtherInfoAdapter;
import com.paditech.cvmarker.base.BaseFragment;
import com.paditech.cvmarker.model.OtherInfo;
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
public class OtherSectionFragment extends BaseFragment implements OtherInfoAdapter.OnOtherItemClicked {

    @InjectView(R.id.list_other_section)
    ExpandableHeightListView mListView;

    @InjectView(R.id.input_section_name)
    EditText mName;

    @InjectView(R.id.input_section_decs)
    EditText mDesc;

    @InjectView(R.id.scroll_view)
    ScrollView mScrollView;

    public List<OtherInfo> mListOther;
    private OtherInfoAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_section, container, false);
        hideKeyBoard(view);
        return view;
    }

    @Override
    protected void initView() {
        if (mListOther == null || mListOther.size() == 0) {
            mListOther = new ArrayList<>();
        }
        mAdapter = new OtherInfoAdapter(getActivity(), mListOther);

        mListView.setAdapter(mAdapter);
        mListView.setExpanded(true);
        mAdapter.setmOnOtherItemClicked(this);

    }

    public boolean doContinue() {

        if (StringUtils.isEmpty(mName.getText().toString().trim()) && StringUtils.isEmpty(mDesc.getText().toString().trim()))
            return true;
        else if (StringUtils.isEmpty(mName.getText().toString().trim())) {
            mName.requestFocus();
            return false;
        } else {
            if (StringUtils.isEmpty(mDesc.getText().toString().trim())) {
                mDesc.requestFocus();
                return false;
            } else {
                return true;
            }
        }
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

        OtherInfo otherInfo = new OtherInfo();
        if (!StringUtils.isEmpty(mName.getText().toString())) {
            otherInfo.setSectionName(mName.getText().toString());
        }
        if (!StringUtils.isEmpty(mDesc.getText().toString())) {
            otherInfo.setSectionDesc(mDesc.getText().toString());
        }

        if (OtherInfo.othersIsEmpty(otherInfo)) {
        } else {
            mListOther.add(otherInfo);
            reset();
            mAdapter.setOtherInfoList(mListOther);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        addDone();
    }


    public void reset() {
        mName.setText("");
        mDesc.setText("");

    }

    @Override
    public void onOtherItemClick(int position) {
        OtherInfo otherInfo = mListOther.get(position);
        mName.setText(otherInfo.getSectionName());
        mDesc.setText(otherInfo.getSectionDesc());

        mListOther.remove(position);
        mAdapter.setOtherInfoList(mListOther);
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
