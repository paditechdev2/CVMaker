package com.paditech.cvmarker.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paditech.cvmarker.R;
import com.paditech.cvmarker.model.Resume;
import com.paditech.cvmarker.utils.LocaleUtil;
import com.paditech.cvmarker.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by USER on 10/6/2016.
 */
public class ResumeListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Resume> mResumeList;
    private OnCreateNewClicked mOnCreateNewClicked;
    private OnResumeItemClicked mOnResumeItemClicked;

    public ResumeListAdapter(Context context, List<Resume> resumeList) {
        mContext = context;
        mResumeList = resumeList;
    }

    public void setmResumeList(List<Resume> resumeList) {
        mResumeList = resumeList;
        notifyDataSetChanged();
    }

    public void setOnCreateNewClick(OnCreateNewClicked onCreateNewClick) {
        mOnCreateNewClicked = onCreateNewClick;
    }

    public void setmOnResumeItemClick(OnResumeItemClicked onResumeItemClick) {
        mOnResumeItemClicked = onResumeItemClick;
    }

    @Override
    public int getCount() {
        if(mResumeList != null) return mResumeList.size() + 1;
        else return 1;
    }

    @Override
    public Object getItem(int position) {
        return mResumeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_resume, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        if(position == mResumeList.size()) {
            viewHolder.mBtnDown.setVisibility(View.GONE);
            viewHolder.mResumeDateCreated.setVisibility(View.INVISIBLE);
            viewHolder.mResumeName.setText(mContext.getString(R.string.create_new));
            viewHolder.mResumeName.setTextColor(mContext.getResources().getColor(R.color.green_dark));
            viewHolder.mResumeName.setTypeface(Typeface.DEFAULT_BOLD);
            viewHolder.mImgResume.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_add_resume));
            viewHolder.mResumeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.theme_color_light));

            viewHolder.mResumeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCreateNewClicked.onCreateNewClicked();
                }
            });
        } else {
            int[] resource = getResource(position);
            viewHolder.mBtnDown.setVisibility(View.VISIBLE);
            viewHolder.mBtnDown.setImageDrawable(mContext.getResources().getDrawable(resource[2]));

            if(!StringUtils.isEmpty(mResumeList.get(position).getDateCreated())) {
                viewHolder.mResumeDateCreated.setVisibility(View.VISIBLE);
                viewHolder.mResumeDateCreated.setText(mResumeList.get(position).getDateCreated());
            }
            else {
                viewHolder.mResumeDateCreated.setVisibility(View.INVISIBLE);
            }

            if(!StringUtils.isEmpty(mResumeList.get(position).getName())) {
                viewHolder.mResumeName.setVisibility(View.VISIBLE);
                viewHolder.mResumeName.setText(mResumeList.get(position).getName());
                viewHolder.mResumeName.setTextColor(mContext.getResources().getColor(R.color.black));
                viewHolder.mResumeName.setTypeface(Typeface.DEFAULT);

            }

            viewHolder.mImgResume.setImageDrawable(mContext.getResources().getDrawable(resource[1]));
            viewHolder.mResumeLayout.setBackgroundColor(mContext.getResources().getColor(resource[0]));

            viewHolder.mResumeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnResumeItemClicked.onResumeItemClick(position);
                }
            });
        }

        float w = (LocaleUtil.getWidthScreen((Activity) mContext) - 80)/2;
        float h = (float) (w* 0.8);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) w, (int) h);
        viewHolder.mResumeLayout.setLayoutParams(params);

        return convertView;
    }

    private int[] getResource(int position) {
        int offset = position % 3;
        int[] rs = new int[3];
        switch (offset) {
            case 0: {
                rs[0] = R.color.green_cv_light;
                rs[1] = R.mipmap.ic_green_resume;
                rs[2] = R.mipmap.ic_down_green;
                break;
            }
            case 1: {
                rs[0] = R.color.red_cv_light;
                rs[1] = R.mipmap.ic_red_resume;
                rs[2] = R.mipmap.ic_down_red;
                break;
            }
            case 2: {
                rs[0] = R.color.purple_cv_light;
                rs[1] = R.mipmap.ic_purple_resume;
                rs[2] = R.mipmap.ic_down_purple;
                break;
            }
        }
        return rs;
    }

    public class ViewHolder {
        @InjectView(R.id.image_resume)
        ImageView mImgResume;

        @InjectView(R.id.btn_down)
        ImageView mBtnDown;

        @InjectView(R.id.resume_item)
        RelativeLayout mResumeLayout;

        @InjectView(R.id.resume_date_created)
        TextView mResumeDateCreated;

        @InjectView(R.id.resume_name)
        TextView mResumeName;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public interface OnCreateNewClicked{
        void onCreateNewClicked();
    }

    public interface OnResumeItemClicked {
        void onResumeItemClick(int position);
    }
}
