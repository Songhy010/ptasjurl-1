package khay.dy.ptasjurl.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivityNotification;
import khay.dy.ptasjurl.activity.ActivityProfile;
import khay.dy.ptasjurl.adapter.AdapterBanner;
import khay.dy.ptasjurl.adapter.AdapterHome;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;

public class FragmentHome extends Fragment {

    private final String TAG = "Fr Home";
    private AppBarLayout app_bar_layout;
    private View root_view;
    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private AdapterBanner adapter;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    private ViewPager viewPager;
    private ImageView iv_noti;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        }
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
        try{
            findView();
            loadHome();
            onClick();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    private void loadHome() {
        final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[3], Global.arData[5]);
        MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                try{
                    final JSONObject object = new JSONObject(response);
                    final JSONArray arrHome = object.getJSONArray(Global.arData[6]);
                    final JSONArray arrBanner = object.getJSONArray(Global.arData[12]);
                    initPagerBanner(arrBanner);
                    initRecyclerView(arrHome);
                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG,e.getMessage()+"");
                loadHome();
            }
        });
    }

    public void onClick(){
       root_view.findViewById(R.id.iv_noti).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               MyFunction.getInstance().openActivity(root_view.getContext(),ActivityNotification.class);
           }
       });
       root_view.findViewById(R.id.iv_profile).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               MyFunction.getInstance().openActivity(root_view.getContext(), ActivityProfile.class);
           }
       });
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

    private void initPagerBanner(JSONArray array) {
        app_bar_layout = root_view.findViewById(R.id.app_bar_layout);
        final int height = MyFunction.getInstance().getBannerHeight(root_view.getContext());
        app_bar_layout.getLayoutParams().height = height;
        List<String> listImage = new ArrayList<>();
        try {
            adapter = new AdapterBanner(root_view.getContext(), listImage,array);
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

    private void initRecyclerView(JSONArray array) {
        manager = new LinearLayoutManager(root_view.getContext(), RecyclerView.VERTICAL, false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new AdapterHome(array, root_view.getContext(), R.layout.item_room));
    }
}
