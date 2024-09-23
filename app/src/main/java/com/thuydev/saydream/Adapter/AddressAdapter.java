package com.thuydev.saydream.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.thuydev.saydream.databinding.ItemListHangBinding;

import java.util.List;

public class AddressAdapter extends BaseAdapter {
    List<String> list;
    Context context;

    public AddressAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemListHangBinding view = ItemListHangBinding.inflate(((Activity)context).getLayoutInflater(),parent,false);
        view.tvTenHang.setText(list.get(position));
        return view.getRoot();
    }
}
