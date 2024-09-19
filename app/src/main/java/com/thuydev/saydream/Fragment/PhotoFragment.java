package com.thuydev.saydream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thuydev.saydream.Model.Photo;
import com.thuydev.saydream.R;

public class PhotoFragment extends Fragment {

    private static final String ARG_PHOTO = "object_photo";

    public static PhotoFragment newInstance(Photo photo) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO, photo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        if (getArguments() != null) {
            Photo photo = (Photo) getArguments().getSerializable(ARG_PHOTO);
            if (photo != null) {
                imgPhoto.setImageResource(photo.getResourceId());
            }
        }
    }
}
