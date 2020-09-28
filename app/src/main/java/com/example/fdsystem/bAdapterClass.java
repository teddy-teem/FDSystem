package com.example.fdsystem;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class bAdapterClass extends RecyclerView.Adapter<bAdapterClass.MyBViewHolder> implements Filterable {
    ArrayList<bData> list, mylist;
    SpannableString up = new SpannableString("▲");
    SpannableString down = new SpannableString("▼");
    ForegroundColorSpan fcsYello = new ForegroundColorSpan(Color.MAGENTA);
    ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
    ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.BLUE);
    ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
    private BAdapterClickListner listner;


    public bAdapterClass(ArrayList<bData> mlist, BAdapterClickListner listner){
        this.list=mlist;
        this.listner = listner;
        mylist=new ArrayList<>(mlist);
    }
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
        holder.tv_wlevel.setText(String.valueOf(list.get(position).getLevel())+list.get(position).getUnit_h());
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

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<bData> filterlist = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filterlist.addAll(mylist);
            }
            else{
                for (bData x: mylist){
                    if (x.DeviceID.toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filterlist.add(x);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterlist;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends bData>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public interface BAdapterClickListner{
        void onClick(View v, int position);
    }

    public class MyBViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_id, tv_area,tv_wlevel, tv_upd;

        public MyBViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.area_id);
            tv_area = itemView.findViewById(R.id.area_name);
            tv_wlevel=itemView.findViewById(R.id.wlevel);
            tv_upd=itemView.findViewById(R.id.upd);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listner.onClick(view,getAdapterPosition());
        }
    }



}