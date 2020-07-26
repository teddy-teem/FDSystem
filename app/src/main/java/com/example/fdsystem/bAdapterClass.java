package com.example.fdsystem;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class bAdapterClass extends RecyclerView.Adapter<bAdapterClass.MyBViewHolder> {
    ArrayList<bData> list;
    public bAdapterClass(ArrayList<bData> mlist){
        list=mlist;
    }
    SpannableString up = new SpannableString("▲");
    SpannableString down = new SpannableString("▼");
    ForegroundColorSpan fcsYello = new ForegroundColorSpan(Color.YELLOW);
    ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
    ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.BLUE);
    ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);

    @NonNull
    @Override
    public MyBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.b_listview,parent,false);

        return new bAdapterClass.MyBViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBViewHolder holder, int position) {
        final bData logData = list.get(position);
        holder.tv_id.setText(list.get(position).getDeviceID());
        holder.tv_area.setText(list.get(position).getDeviceArea());
        holder.tv_wlevel.setText(list.get(position).getLevel());
        holder.tv_upd.setText(list.get(position).getUp_down());
        String up_d = list.get(position).getUp_down();
        if (up_d.equals("▲▲")){
                up.setSpan(fcsRed, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tv_upd.setText(up);
        }
        else if (up_d.equals("▲")){
            up.setSpan(fcsYello, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_upd.setText(up);
        }
        else if(up_d.equals("▼▼")){
            down.setSpan(fcsGreen, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_upd.setText(down);
        }
        else{
            down.setSpan(fcsBlue, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_upd.setText(down);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyBViewHolder extends RecyclerView.ViewHolder{
        TextView tv_id, tv_area,tv_wlevel, tv_upd;
        public MyBViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.area_id);
            tv_area = itemView.findViewById(R.id.area_name);
            tv_wlevel=itemView.findViewById(R.id.wlevel);
            tv_upd=itemView.findViewById(R.id.upd);

        }
    }
}