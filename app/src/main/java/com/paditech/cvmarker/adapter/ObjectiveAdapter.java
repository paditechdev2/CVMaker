package com.paditech.cvmarker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paditech.cvmarker.R;
import com.paditech.cvmarker.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by USER on 14/6/2016.
 */
public class ObjectiveAdapter extends BaseAdapter {

    private Activity context;
    private List<String> listObjective;
    private OnObjectiveItemClicked mOnObjectiveItemClicked;

    public ObjectiveAdapter(Activity context, List<String> listObjective) {
        this.context = context;
        this.listObjective = listObjective;
    }

    public void setListObjective(List<String> objective) {
        listObjective = objective;
        notifyDataSetChanged();
    }

    public void setmOnObjectiveItemClicked(OnObjectiveItemClicked onClick) {
        mOnObjectiveItemClicked = onClick;
    }

    @Override
    public int getCount() {
        return listObjective.size();
    }

    @Override
    public Object getItem(int position) {
        return listObjective.get(position);
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

        if(!StringUtils.isEmpty(listObjective.get(position))) {
            viewHolder.mObjective.setText(listObjective.get(position));
        }
        viewHolder.mDeleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listObjective.remove(position);
                setListObjective(listObjective);
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
        TextView mObjective;

        @InjectView(R.id.btn_delete)
        ImageView mDeleteButton;

        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }

    public interface OnObjectiveItemClicked{
        void onObjectiveItemClicked(int position);
    }

}
