package vn.edu.usth.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import android.os.Bundle;

public class HomeFragmentPaperAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private String[] titles = new String[]{"Hanoi,Vietnam", "Paris,France", "Berlin,Germany"};

    protected HomeFragmentPaperAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() { return PAGE_COUNT; }

    @Override
    public Fragment getItem(int page){
        switch (page){
            case 0:
                return WeatherAndForecastFragment.newInstance(0,"Page #1");
            case 1:
                return WeatherAndForecastFragment.newInstance(1,"Page #2");
            case 2:
                return WeatherAndForecastFragment.newInstance(2,"Page #3");
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }



}
