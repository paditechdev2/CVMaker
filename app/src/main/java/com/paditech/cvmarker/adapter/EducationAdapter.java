package com.paditech.cvmarker.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paditech.cvmarker.R;
import com.paditech.cvmarker.model.Education;
import com.paditech.cvmarker.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by USER on 14/6/2016.
 */
public class EducationAdapter extends BaseAdapter {

    private Activity context;
    private List<Education> listEducation;
    private OnEducationItemClicked mOnObjectiveItemClicked;

    public EducationAdapter(Activity context, List<Education> listEducation) {
        this.context = context;
        this.listEducation = listEducation;
    }

    public void setListObjective(List<Education> educations) {
        listEducation = educations;
        notifyDataSetChanged();
    }

    public List<Education> getListObjective() {
        return listEducation;
    }

    public void setmOnObjectiveItemClicked(OnEducationItemClicked onClick) {
        mOnObjectiveItemClicked = onClick;
    }

    @Override
    public int getCount() {
        return listEducation.size();
    }

    @Override
    public Object getItem(int position) {
        return listEducation.get(position);
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

        if(!StringUtils.isEmpty(listEducation.get(position).getDatePeriod()))
            viewHolder.mEducation.setText(Html.fromHtml("<b> "+ listEducation.get(position).getDatePeriod()+"</b>: " +
                listEducation.get(position).getSchoolName()));

        viewHolder.mDeleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listEducation.remove(position);
                setListObjective(listEducation);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnObjectiveItemClicked.onObjectiveItemClicked(position);
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

    public interface OnEducationItemClicked{
        void onObjectiveItemClicked(int position);
    }

}
