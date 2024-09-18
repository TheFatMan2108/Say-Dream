package com.thuydev.saydream.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thuydev.saydream.Model.Photo;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.FragmentFrgHomeBinding;


public class Fragment_frg_Home extends Fragment {

    private View mView;
    FragmentFrgHomeBinding view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = FragmentFrgHomeBinding.inflate(getLayoutInflater());
        return view.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}