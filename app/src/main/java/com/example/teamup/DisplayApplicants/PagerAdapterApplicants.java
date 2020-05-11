package com.example.teamup.DisplayApplicants;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class PagerAdapterApplicants extends FragmentPagerAdapter {

    private int numberOfTabs;

    public PagerAdapterApplicants(@NonNull FragmentManager fm, int behavior) {
        super(fm);
        this.numberOfTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ApplicantDisplayFragment();
            case 1:
                return new AllMembersFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
