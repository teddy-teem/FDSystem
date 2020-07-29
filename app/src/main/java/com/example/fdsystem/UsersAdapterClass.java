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

public class UsersAdapterClass extends RecyclerView.Adapter<UsersAdapterClass.myViewHolder>  {
    ArrayList<UsersData> list;



    public UsersAdapterClass(ArrayList<UsersData> mlist){
        list=mlist;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userslist,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_email.setText(list.get(position).getEmail());
        holder.tv_mob.setText(list.get(position).getMobile());
        holder.tv_pass.setText(list.get(position).getPassword());
        String s = list.get(position).getStatus(); //// For verified id count.......... later will be improved
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name, tv_email, tv_mob, tv_pass;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name= itemView.findViewById(R.id.d_name_);
            tv_email = itemView.findViewById(R.id.d_emil);
            tv_mob = itemView.findViewById(R.id.d_mob);
            tv_pass = itemView.findViewById(R.id.d_pass);
        }

    }


}
