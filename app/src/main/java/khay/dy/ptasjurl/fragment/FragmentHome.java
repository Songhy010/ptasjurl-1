package khay.dy.ptasjurl.fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;

import org.myjson.JSONArray;
import org.myjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivityHome;
import khay.dy.ptasjurl.activity.ActivityMain;
import khay.dy.ptasjurl.adapter.BannerAdapter;
import khay.dy.ptasjurl.adapter.HomeAdapter;
import khay.dy.ptasjurl.adapter.ViewPagerAdapter;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;

public class FragmentHome extends Fragment {

    private final String TAG = "Fr Home";
    private AppBarLayout app_bar_layout;
    private View root_view;
    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private BannerAdapter adapter;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        }
        Log.e(TAG,"Test");
        return root_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        /* TODO set font with current language */
        MyFont.getInstance().setFont(root_view.getContext(), root_view, 2);
    }

    private void findView() {
        recycler = root_view.findViewById(R.id.recycler);
    }

    private void initView() {
        findView();
        initRecyclerView();
        initPagerBanner();
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(root_view.getContext());
            int width_height = 20;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 0, 10, 0);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            dots[i].setColorFilter(ContextCompat.getColor(root_view.getContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
            dots[current].setColorFilter(ContextCompat.getColor(root_view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void startAutoSlider(final int count) {
        handler.removeCallbacks(runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

    }

    private void initPagerBanner() {
        app_bar_layout = root_view.findViewById(R.id.app_bar_layout);
        final int height = MyFunction.getInstance().getBannerHeight(root_view.getContext());
        app_bar_layout.getLayoutParams().height = height;
        List<String> listImage = new ArrayList<>();
        try {
            adapter = new BannerAdapter(root_view.getContext(), listImage);
            viewPager = root_view.findViewById(R.id.pager);
            viewPager.setAdapter(adapter);
//            addBottomDots(layout_dots, adapter.getCount(), 0);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
//                    addBottomDots(layout_dots, adapter.getCount(), position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            startAutoSlider(adapter.getCount());
        } catch (Exception e) {
            Log.e("Err ", e.getMessage() + "");
        }
    }

    private void initRecyclerView() {
        manager = new LinearLayoutManager(root_view.getContext(), RecyclerView.VERTICAL, false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new HomeAdapter(null, root_view.getContext(), R.layout.item_home));
    }
}
