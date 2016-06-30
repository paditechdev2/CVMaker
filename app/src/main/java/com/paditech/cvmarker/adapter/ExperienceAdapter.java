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
import com.paditech.cvmarker.model.WorkExperience;
import com.paditech.cvmarker.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
/**
 * Created by USER on 14/6/2016.
 */
public class ExperienceAdapter extends BaseAdapter {

    private Activity context;
    private List<WorkExperience> experienceList;
    private OnExpItemClicked mOnExpItemClicked;

    public ExperienceAdapter(Activity context, List<WorkExperience> experiences) {
        this.context = context;
        this.experienceList = experiences;
    }

    public void setListObjective(List<WorkExperience> experiences) {
        experienceList = experiences;
        notifyDataSetChanged();
    }

    public List<WorkExperience> getListObjective() {
        return experienceList;
    }

    public void setmOnExpItemClicked(OnExpItemClicked onClick) {
        mOnExpItemClicked = onClick;
    }

    @Override
    public int getCount() {
        return experienceList.size();
    }

    @Override
    public Object getItem(int position) {
        return experienceList.get(position);
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

        if(!StringUtils.isEmpty(experienceList.get(position).getDatePeriod()))
            viewHolder.mEducation.setText(Html.fromHtml("<b> "+ experienceList.get(position).getDatePeriod()+"</b>: " +
                    experienceList.get(position).getCompanyName()));

        viewHolder.mDeleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                experienceList.remove(position);
                setListObjective(experienceList);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnExpItemClicked.onExpItemClick(position);
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

    public interface OnExpItemClicked{
        void onExpItemClick(int position);
    }

}
