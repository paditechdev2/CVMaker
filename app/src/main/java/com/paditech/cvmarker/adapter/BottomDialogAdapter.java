package com.paditech.cvmarker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paditech.cvmarker.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by USER on 13/6/2016.
 */
public class BottomDialogAdapter extends BaseAdapter {

    private Context context;
    private OnDialogItemClick onDialogItemClick;

    public BottomDialogAdapter(Context context) {
        this.context = context;
    }

    public void setOnDialogItemClick(OnDialogItemClick onClick) {
        onDialogItemClick = onClick;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bottom_dialog, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        switch (position) {
            case 0: viewHolder.img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_edit));
                viewHolder.title.setText(context.getString(R.string.edit));
                break;
            case 1: viewHolder.img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_preview_bottom));
                viewHolder.title.setText(context.getString(R.string.preview));
                break;

            case 2: viewHolder.img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_export));
                viewHolder.title.setText(context.getString(R.string.send));
                break;

            case 3: viewHolder.img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_print));
                viewHolder.title.setText(context.getString(R.string.print));
                break;

            case 4: viewHolder.img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_rename));
                viewHolder.title.setText(context.getString(R.string.rename));
                break;

            case 5: viewHolder.img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_delete));
                viewHolder.title.setText(context.getString(R.string.delete));
                break;

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogItemClick.onDialogItemClick(position);
            }
        });

        return convertView;
    }

    public class ViewHolder {

        @InjectView(R.id.img_item)
        ImageView img;

        @InjectView(R.id.title_item)
        TextView title;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public interface OnDialogItemClick{
        void onDialogItemClick(int position);
    }
}
