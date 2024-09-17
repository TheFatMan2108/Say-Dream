package com.thuydev.saydream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thuydev.saydream.databinding.FragmentGioHangBinding;
import com.thuydev.saydream.databinding.FrangmentChoxacnhanBinding;

public class Fragment_Bill extends Fragment {
    FrangmentChoxacnhanBinding view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = FrangmentChoxacnhanBinding.inflate(getLayoutInflater());
        return view.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
