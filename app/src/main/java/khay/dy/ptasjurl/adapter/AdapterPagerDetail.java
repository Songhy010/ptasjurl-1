package khay.dy.ptasjurl.adapter;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import khay.dy.ptasjurl.R;

public class AdapterPagerDetail extends FragmentPagerAdapter {
    private String[] titles ;//= {getString(R.string.latest_read), getString(R.string.all), getString(R.string.donated)};
    private final List<Fragment> fragments = new ArrayList<>();


    public AdapterPagerDetail(final FragmentManager manager, String[] titles) {
        super(manager);
        this.titles = titles;
    }

    public void addFrag(Fragment fragment) {
        try {
            fragments.add(fragment);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return fragments.get(position);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        try {
            return titles[position];
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return "";
    }
}
