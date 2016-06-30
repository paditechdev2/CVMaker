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
import com.paditech.cvmarker.adapter.ObjectiveAdapter;
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
public class ObjectiveFragment extends BaseFragment implements ObjectiveAdapter.OnObjectiveItemClicked {

    @InjectView(R.id.list_objective)
    ExpandableHeightListView mListView;

    @InjectView(R.id.input_objective)
    EditText mTextObjective;

    @InjectView(R.id.scroll_view)
    ScrollView mScrollView;

    public List<String> mListObjective;
    private ObjectiveAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_objective, container, false);
        hideKeyBoard(view);
        return view;
    }

    @Override
    protected void initView() {
        if(mListObjective == null || mListObjective.size()==0) {
            mListObjective = new ArrayList<>();
        }
        mAdapter = new ObjectiveAdapter(getActivity(), mListObjective);

        mListView.setAdapter(mAdapter);
        mListView.setExpanded(true);
        mAdapter.setmOnObjectiveItemClicked(this);

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
            mListObjective.add(mTextObjective.getText().toString());
            mAdapter.setListObjective(mListObjective);
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
        mTextObjective.setText(mListObjective.get(position));
        mListObjective.remove(position);
        mAdapter.setListObjective(mListObjective);
    }

    public boolean doContinue() {
        addDone();
        return true;
    }

    public void reset() {
        mTextObjective.setText("");
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
