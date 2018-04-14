package com.alchemist.evlivechat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ashish Alton on 3/6/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Data> list;
    private TextView textView;
    private TextView textView2;


    MessageAdapter(Context context, ArrayList<Data> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.message,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        textView.setText(list.get(position).getMessage());
        textView2.setText(list.get(position).getName()+":");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.message);
            textView2=(TextView) itemView.findViewById(R.id.name);
        }
    }
}


