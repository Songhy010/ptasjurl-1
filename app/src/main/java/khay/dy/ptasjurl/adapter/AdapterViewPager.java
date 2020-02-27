package khay.dy.ptasjurl.adapter;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterViewPager extends FragmentPagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();

    public AdapterViewPager(FragmentManager manager) {
        super(manager);
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
        return fragments.size();
    }

    public void addFrag(Fragment fragment) {
        try {
            fragments.add(fragment);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }
}