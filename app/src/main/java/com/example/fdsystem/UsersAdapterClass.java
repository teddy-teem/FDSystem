package com.example.fdsystem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsersAdapterClass extends RecyclerView.Adapter<UsersAdapterClass.myViewHolder>{
    ArrayList<Users> list;

    public UsersAdapterClass(ArrayList<Users> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new UsersAdapterClass.myViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.tv_name.setText(list.get(position).getName());
        holder.tv_Email.setText(list.get(position).getEmail());
        holder.tv_Mob.setText(list.get(position).getMobile());
        holder.tv_Pass.setText(list.get(position).getPassword());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name, tv_Email, tv_Mob, tv_Pass;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.d_name);
            tv_Email = itemView.findViewById(R.id.d_emil);
            tv_Mob = itemView.findViewById(R.id.d_mob);
            tv_Pass = itemView.findViewById(R.id.d_pass);
        }

    }
}
