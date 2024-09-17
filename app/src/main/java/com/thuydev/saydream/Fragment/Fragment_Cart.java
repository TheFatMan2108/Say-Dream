package com.thuydev.saydream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thuydev.saydream.databinding.FragmentCuahangBinding;
import com.thuydev.saydream.databinding.FragmentGioHangBinding;

public class Fragment_Cart extends Fragment {
    FragmentGioHangBinding view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = FragmentGioHangBinding.inflate(getLayoutInflater());
        return view.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
