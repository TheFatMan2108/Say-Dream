package com.thuydev.saydream.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thuydev.saydream.Fragment.Fragment_frg_Home;
import com.thuydev.saydream.Model.Photo;

import java.util.List;

public class PhotoAdapter extends FragmentStateAdapter {

    private List<Photo> mListPhoto;

    public PhotoAdapter(@NonNull FragmentActivity fragmentActivity, List<Photo> list) {
        super(fragmentActivity);
        this.mListPhoto = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Photo photo = mListPhoto.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_photo", photo);

        Fragment_frg_Home fragmentFrgHome = new Fragment_frg_Home();
        fragmentFrgHome.setArguments(bundle);


        return fragmentFrgHome;
    }

    @Override
    public int getItemCount() {
        if (mListPhoto!=null){
            return mListPhoto.size();
        }
        return 0;
    }
}
