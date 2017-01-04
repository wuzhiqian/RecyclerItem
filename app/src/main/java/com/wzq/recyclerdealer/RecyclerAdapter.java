package com.wzq.recyclerdealer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wzq on 17-1-2.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.DemoViewHolder> implements ItemChangeImp {


    private List<String> datas;

    public RecyclerAdapter() {
        if (datas == null)
            datas = new ArrayList<>();
        datas.clear();
        for (int i = 0; i < 100; i++) {
            datas.add(i + "");
        }
    }

    @Override
    public DemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new DemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DemoViewHolder holder, int position) {
        holder.textView.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public void changePos(int from, int to) {
        Collections.swap(datas, from, to);
        notifyItemMoved(from, to);
    }

    @Override
    public void deletePos(int pos) {
        datas.remove(pos);
        notifyItemRemoved(pos);
    }

    public class DemoViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public DemoViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
