package com.example.fdsystem;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LogAdapterClass extends RecyclerView.Adapter<LogAdapterClass.myViewHolder>  {
    ArrayList<LogData> list;
    SpannableString up = new SpannableString("▲");
    SpannableString down = new SpannableString("▼");
    ForegroundColorSpan fcsYello = new ForegroundColorSpan(Color.YELLOW);
    ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
    ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.BLUE);
    ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);



    public LogAdapterClass(ArrayList<LogData> mlist){
        list=mlist;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final LogData logData = list.get(position);
        holder.tv_id.setText(list.get(position).getDeviceID());
        holder.tv_area.setText(list.get(position).getDeviceArea());
        holder.tv_level.setText(list.get(position).getLevel());
        String up_d = list.get(position).getUp_down();

        if (up_d.equals("▲▲")){
            up.setSpan(fcsRed, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_ud.setText(up);
        }
        else if (up_d.equals("▲")){
            up.setSpan(fcsYello, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_ud.setText(up);
        }
        else if(up_d.equals("▼▼")){
            down.setSpan(fcsGreen, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_ud.setText(down);
        }
        else{
            down.setSpan(fcsBlue, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_ud.setText(down);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
    TextView tv_id, tv_area, tv_level, tv_ud;
    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_id = itemView.findViewById(R.id.d_id);
        tv_area = itemView.findViewById(R.id.d_name);
        tv_level = itemView.findViewById(R.id.d_level);
        tv_ud = itemView.findViewById(R.id.d_up_d);
      }

    }


}
