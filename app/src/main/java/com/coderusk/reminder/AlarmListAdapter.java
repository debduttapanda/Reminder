package com.coderusk.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmListAdapter extends ArrayAdapter<AlarmData> implements View.OnClickListener{

    private ArrayList<AlarmData> dataSet;
    Context mContext;
    ListView listView;

    // View lookup cache
    private static class ViewHolder {
        TextView tv_id;
        Button bt_delete;
        TextView tv_second;
    }

    public AlarmListAdapter(ArrayList<AlarmData> data, Context context,ListView l) {
        super(context, R.layout.alarm_item, data);
        this.dataSet = data;
        this.mContext=context;
        this.listView=l;

    }

    private void deleteAlarm(int position)
    {
        String id=dataSet.get(position).getId();
        dataSet.remove(position);
        notifyDataSetChanged();
        ((MainActivity)mContext).onDeleteItem(id);
    }
    public ArrayList<AlarmData> getList()
    {
        return dataSet;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();

        switch (v.getId())
        {
            case R.id.bt_delete:
                deleteAlarm(position);
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        // Get the data item for this position
        final ListView l=(ListView) parent;
        final int p=position;
        AlarmData dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.alarm_item, parent, false);
            viewHolder.tv_id = convertView.findViewById(R.id.tv_id);
            viewHolder.bt_delete = convertView.findViewById(R.id.bt_delete);
            viewHolder.tv_second=convertView.findViewById(R.id.tv_second);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm(p);
            }
        });

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.tv_id.setText(dataModel.getId());
        viewHolder.tv_second.setText(dataModel.getSecond());
        // Return the completed view to render on screen
        return convertView;
    }
}
