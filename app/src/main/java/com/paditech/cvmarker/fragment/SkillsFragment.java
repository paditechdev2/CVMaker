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
import com.paditech.cvmarker.adapter.SkillAdapter;
import com.paditech.cvmarker.base.BaseFragment;
import com.paditech.cvmarker.utils.LocaleUtil;
import com.paditech.cvmarker.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by USER on 12/6/2016.
 */
public class SkillsFragment extends BaseFragment implements SkillAdapter.OnSkillItemClicked {

    @InjectView(R.id.list_skills)
    ExpandableHeightListView mListView;

    @InjectView(R.id.input_skill)
    EditText mTextObjective;

    @InjectView(R.id.scroll_view)
    ScrollView mScrollView;

    public List<String> mListSkill;
    private SkillAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skills, container, false);
        hideKeyBoard(view);
        return view;
    }

    @Override
    protected void initView() {
        if(mListSkill == null || mListSkill.size()==0) {
            mListSkill = new ArrayList<>();
        }
        mAdapter = new SkillAdapter(getActivity(), mListSkill);

        mListView.setAdapter(mAdapter);
        mListView.setExpanded(true);
        mAdapter.setmOnSkillItemClicked(this);

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

        if(!StringUtils.isEmpty(mTextObjective.getText().toString())) {
            mListSkill.add(mTextObjective.getText().toString());
            mAdapter.setListSkill(mListSkill);
            reset();
        }
    }

    public boolean doContinue() {
        addDone();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        addDone();
    }

    public void reset() {
        mTextObjective.setText("");
    }

    @Override
    public void onSkillItemClick(int position) {
        mTextObjective.setText(mListSkill.get(position));
        mListSkill.remove(position);
        mAdapter.setListSkill(mListSkill);
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
