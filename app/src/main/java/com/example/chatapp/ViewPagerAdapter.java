package com.example.chatapp;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatapp.Ui_Fragments.Chats;
import com.example.chatapp.Ui_Fragments.Profile;
import com.example.chatapp.Ui_Fragments.Share;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0 :
                fragment = new Share();
                break;
            case 1 :
                fragment = new Chats();
                break;
            case 2 :
                fragment = new Profile();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :
                return "Share";
            case 1 :
                return "Chats";
            case 2 :
                return "Profile";
        }
        return "";
    }
}
