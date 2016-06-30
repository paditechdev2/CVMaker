package com.paditech.cvmarker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.paditech.cvmarker.R;
import com.paditech.cvmarker.utils.StringUtils;

/**
 * Created by USER on 14/6/2016.
 */
public class SkillAdapter extends BaseAdapter {

    private Activity context;
    private List<String> listSkill;
    private OnSkillItemClicked mOnItemClick;

    public SkillAdapter(Activity context, List<String> skills) {
        this.context = context;
        this.listSkill = skills;
    }

    public void setListSkill(List<String> skills) {
        listSkill = skills;
        notifyDataSetChanged();
    }

    public void setmOnSkillItemClicked(OnSkillItemClicked onClick) {
        mOnItemClick = onClick;
    }

    @Override
    public int getCount() {
        return listSkill.size();
    }

    @Override
    public Object getItem(int position) {
        return listSkill.get(position);
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

        if(!StringUtils.isEmpty(listSkill.get(position))) {
            viewHolder.mObjective.setText(listSkill.get(position));
        }
        viewHolder.mDeleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listSkill.remove(position);
                setListSkill(listSkill);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClick.onSkillItemClick(position);
            }
        });
        return convertView;
    }

    public class ViewHolder {

        @InjectView(R.id.objective_text)
        TextView mObjective;

        @InjectView(R.id.btn_delete)
        ImageView mDeleteButton;


        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }

    public interface OnSkillItemClicked{
        void onSkillItemClick(int position);
    }

}
