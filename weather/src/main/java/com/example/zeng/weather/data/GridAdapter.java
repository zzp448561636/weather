package com.example.zeng.weather.data;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zeng.weather.AppController;
import com.example.zeng.weather.R;

import java.util.List;

/**
 * Created by Zeng on 2016/8/3.
 */
public class GridAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private List<String> cityList;

    public GridAdapter(Context context,List<String> list){
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.cityList = AppController.getInstance().getCityNameList();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_gridview,null);
            holder.textView =(TextView) view.findViewById(R.id.tv_item_grid);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        String name = list.get(i);
        if(cityList.contains(name)){
            holder.textView.setTextColor(ContextCompat.getColor(context,R.color.holo_blue_dark));
            holder.textView.setBackgroundResource(R.drawable.corners_shape_checked);
        }
        if(name.length()>3){
            float textSize = 75/name.length();
            holder.textView.setTextSize(textSize);
        }
        holder.textView.setText(list.get(i));
        return view;
    }

    private class ViewHolder {
        TextView textView;
    }
}
