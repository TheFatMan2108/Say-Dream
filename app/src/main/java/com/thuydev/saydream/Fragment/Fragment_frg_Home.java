package com.thuydev.saydream.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thuydev.saydream.Model.Photo;
import com.thuydev.saydream.R;


public class Fragment_frg_Home extends Fragment {

    private View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frg__home, container, false);

        Bundle bundle = getArguments();
        Photo photo = (Photo) bundle.get("object_photo");

        ImageView imgPhoto = mView.findViewById(R.id.img_photo);
        imgPhoto.setImageResource(photo.getResourceId());
        return mView;
    }


}