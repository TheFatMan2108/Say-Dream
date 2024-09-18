package com.thuydev.saydream.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.thuydev.saydream.Fragment.PhotoFragment;
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
        return PhotoFragment.newInstance(photo);
    }

    @Override
    public int getItemCount() {
        return mListPhoto != null ? mListPhoto.size() : 0;
    }
}
