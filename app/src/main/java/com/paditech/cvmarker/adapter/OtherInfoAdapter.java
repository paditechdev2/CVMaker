package com.paditech.cvmarker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paditech.cvmarker.R;
import com.paditech.cvmarker.model.OtherInfo;
import com.paditech.cvmarker.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by USER on 14/6/2016.
 */
public class OtherInfoAdapter extends BaseAdapter {

    private Activity context;
    private List<OtherInfo> otherInfoList;
    private OnOtherItemClicked mOtherItemClicked;

    public OtherInfoAdapter(Activity context, List<OtherInfo> list) {
        this.context = context;
        this.otherInfoList = list;
    }

    public void setOtherInfoList(List<OtherInfo> otherInfos) {
        otherInfoList = otherInfos;
        notifyDataSetChanged();
    }

    public List<OtherInfo> getOtherInfoList() {
        return otherInfoList;
    }

    public void setmOnOtherItemClicked(OnOtherItemClicked onClick) {
        mOtherItemClicked = onClick;
    }

    @Override
    public int getCount() {
        return otherInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return otherInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_objective, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        if(!StringUtils.isEmpty(otherInfoList.get(position).getSectionName()))
            viewHolder.mEducation.setText(otherInfoList.get(position).getSectionName());

        viewHolder.mDeleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                otherInfoList.remove(position);
                setOtherInfoList(otherInfoList);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOtherItemClicked.onOtherItemClick(position);
            }
        });
        return convertView;
    }

    public class ViewHolder {

        @InjectView(R.id.objective_text)
        TextView mEducation;

        @InjectView(R.id.btn_delete)
        ImageView mDeleteButton;


        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }

    public interface OnOtherItemClicked{
        void onOtherItemClick(int position);
    }

}
