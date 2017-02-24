package vn.com.z11.z11app.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import vn.com.z11.z11app.Tabfragment.Tab1Fragment;
import vn.com.z11.z11app.Tabfragment.Tab2fragment;
import vn.com.z11.z11app.Tabfragment.Tab3Fragment;

/**
 * Created by kienlv58 on 12/2/16.
 */
public class pagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Category", "Lession", "My Folder" };
    private Context context;

    public pagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Tab1Fragment tab1Fragment = new Tab1Fragment();
                return tab1Fragment;
            case 1:
                Tab2fragment tab2Fragment = new Tab2fragment();
                return tab2Fragment;
            case 2:
                Tab3Fragment tab3Fragment = new Tab3Fragment();
                return tab3Fragment;
            default:return new Tab1Fragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
