package khay.dy.ptasjurl.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivityFilterLocation;
import khay.dy.ptasjurl.activity.ActivityNotification;
import khay.dy.ptasjurl.activity.ActivityProfile;
import khay.dy.ptasjurl.adapter.AdapterBanner;
import khay.dy.ptasjurl.adapter.AdapterItem;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.model.ModelItem;
import khay.dy.ptasjurl.model.ModelHome;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;

public class FragmentHome extends Fragment implements AdapterItem.OnLoadMoreListener {

    private final String TAG = "Fr Home";
    private AppBarLayout app_bar_layout;
    private View root_view;
    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private AdapterBanner adapter;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    private ViewPager viewPager;
    private SwipeRefreshLayout swipe;

    private AdapterItem mAdapter;
    private ArrayList<ModelItem> modelItemList;

    private int index = 5;

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
        swipe = root_view.findViewById(R.id.swipe);
        recycler = root_view.findViewById(R.id.recycler);
    }

    private void initView() {
        try {
            findView();
            initHome();
            onClick();
            initSwipe();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void initSwipe() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHome();
            }
        });
    }

    private void initHome() {
        try {
            modelItemList = new ArrayList<>();
            final JSONObject object = ModelHome.getInstance().getObjHome();
            final JSONArray arrHome = object.getJSONArray(Global.arData[6]);
            final JSONArray arrBanner = object.getJSONArray(Global.arData[12]);
            initPagerBanner(arrBanner);
            initRecyclerView();
            mAdapter.addAll(Data(arrHome));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    public void onClick() {
        root_view.findViewById(R.id.iv_noti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(root_view.getContext(), ActivityNotification.class);
            }
        });
        root_view.findViewById(R.id.iv_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(root_view.getContext(), ActivityProfile.class);
            }
        });
        root_view.findViewById(R.id.search_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(root_view.getContext(), ActivityFilterLocation.class);
            }
        });
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
            adapter = new AdapterBanner(root_view.getContext(), listImage, array);
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
        mAdapter = new AdapterItem(root_view.getContext(), this);
        recycler.setAdapter(mAdapter);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (dy > 0 && llManager.findLastCompletelyVisibleItemPosition() == (mAdapter.getItemCount() - 2)) {
                    mAdapter.showLoading();
                }
            }
        });
    }

    private List<ModelItem> Data(JSONArray array){
        try{
            modelItemList.clear();
            for (int i = 0; i < array.length(); i++) {
                final JSONObject obj = array.getJSONObject(i);
                if (!obj.getString(Global.arData[7]).equals("3"))
                    modelItemList.add(new ModelItem(obj.getString(Global.arData[11]), obj.getString(Global.arData[44]), obj.getString(Global.arData[7]), obj.getString(Global.arData[69]), obj.getString(Global.arData[10])));
                else
                    modelItemList.add(new ModelItem(obj.getString(Global.arData[11]), obj.getString(Global.arData[44]), obj.getString(Global.arData[7]), "", ""));
            }
            return modelItemList;
        }catch (Exception e){
            Log.e("Err",e.getMessage()+"");
        }
        return null;
    }


    @Override
    public void onLoadMore() {
        loadHomeMore();
    }


    private void loadHomeMore() {
        final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[3], Global.arData[5]);
        final HashMap<String, String> param = new HashMap<>();
        param.put(Global.arData[88], index + "");
        MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    index = index + 5;
                    mAdapter.dismissLoading();
                    final JSONObject object = new JSONObject(response);
                    final JSONArray arrHome = object.getJSONArray(Global.arData[6]);
                    mAdapter.addItemMore(Data(arrHome));
                    mAdapter.setMore(true);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage()+"");
                }
            }

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage() + "");
                loadHomeMore();
            }
        });
    }

    private void loadHome() {
        final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[3], Global.arData[5]);
        MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    index = 5;
                    final JSONObject object = new JSONObject(response);
                    ModelHome.getInstance().setObjHome(object);
                    initHome();
                    swipe.setRefreshing(false);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage() + "");
                loadHome();
            }
        });
    }
}
