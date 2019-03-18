package com.foxconn.matthew.recyclerviewtest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Matthew on 2017/7/28.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ListItem> list;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    public RecyclerAdapter(List<ListItem> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_ltem, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                ListItem listItem = list.get(position);
                Toast.makeText(v.getContext(), "Click the view"+position+listItem.getNumber(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                ListItem listItem = list.get(position);
                Toast.makeText(v.getContext(), "Click the image"+position+listItem.getNumber(), Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = list.get(position);
        holder.imageView.setImageResource(listItem.getImageId());
        holder.textView.setText(listItem.getNumber() + "");
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
