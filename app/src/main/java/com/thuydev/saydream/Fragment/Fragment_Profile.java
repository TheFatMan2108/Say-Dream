package com.thuydev.saydream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thuydev.saydream.Activity.ActivityProfile;
import com.thuydev.saydream.databinding.TabThongtincanhanBinding;

public class Fragment_Profile extends Fragment {
    TabThongtincanhanBinding viewFrag;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewFrag = TabThongtincanhanBinding.inflate(getLayoutInflater());
        return viewFrag.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewFrag.llDoimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProfile.instance.ChangePassWord();
            }
        });
        viewFrag.llDangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProfile.instance.SignOut();
            }
        });
        viewFrag.llThongtincanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProfile.instance.UpdateProfile();
            }
        });
        viewFrag.llDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProfile.instance.AddAddress();
            }
        });
        viewFrag.llLichsumua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProfile.instance.HistoryBuy();
            }
        });
        viewFrag.llLichsugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProfile.instance.ShowDepositMoneyBill();
            }
        });
    }
}
