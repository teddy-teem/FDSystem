package com.example.fdsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class DataTableAdapter extends ArrayAdapter<Data> {
    private static final String TAG = "DataTableAdapter";
    private Context mContext;
    int mResource;
    public DataTableAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Data> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String D_id=getItem(position).getDeviceID(), D_name=getItem(position).getDeviceName(),
                wL=getItem(position).getwLevel(),UD=getItem(position).getUpDown();
        Data data = new Data(D_id,D_name,wL,UD);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvD_id = (TextView) convertView.findViewById(R.id.d_id);
        TextView tvD_name = (TextView) convertView.findViewById(R.id.d_name);
        TextView tvD_wl = (TextView) convertView.findViewById(R.id.d_wlevel);
        TextView tvD_ud = (TextView) convertView.findViewById(R.id.d_up_down);

        tvD_id.setText(D_id);
        tvD_name.setText(D_name);
        tvD_wl.setText(wL);
        tvD_ud.setText(UD);
        return convertView;

    }











}
