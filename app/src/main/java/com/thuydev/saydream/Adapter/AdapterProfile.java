package com.thuydev.saydream.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thuydev.saydream.Fragment.Fragment_BillToMonth;
import com.thuydev.saydream.Fragment.Fragment_Profile;
import com.thuydev.saydream.Interface.ICallBackAction;

public class AdapterProfile extends FragmentStateAdapter {
    int i = 2;
    ICallBackAction action;
    Fragment_Profile fragmentProfile;
    Fragment_BillToMonth fragmentBillToMonth;
    public AdapterProfile(@NonNull FragmentActivity fragmentActivity, ICallBackAction action) {
        super(fragmentActivity);
        this.action = action;
        fragmentProfile = new Fragment_Profile();
        fragmentBillToMonth = new Fragment_BillToMonth();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0)return fragmentProfile;
        return fragmentBillToMonth;
    }

    @Override
    public int getItemCount() {
        return i;
    }
}
