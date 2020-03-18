package khay.dy.ptasjurl.activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterBanner;
import khay.dy.ptasjurl.adapter.AdapterHome;
import khay.dy.ptasjurl.adapter.AdapterPagerDetail;
import khay.dy.ptasjurl.fragment.FragmentDetail;
import khay.dy.ptasjurl.model.ModelLatLng;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityRoomDetail extends ActivityController {

    private ViewPager viewPager;
    private AdapterBanner adapter;
    private Runnable runnable = null;
    private Handler handler = new Handler();


    private AdapterPagerDetail adapterPager;

    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION/*, Manifest.permission.ACCESS_COARSE_LOCATION*/};
    private boolean gps_enabled, googleservice_installed, network_enabled, isCheckGps;
    private LocationManager locationManager;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private double lat = 11.561545, lng = 104.892521;
    private int DEFAULT_ZOOM = 15, CAMERA_ANIMATE = 800;
    private final int PIN_W = 75;
    private final int PIN_H = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
        initView();
    }

    private void initView() {
        findView();
        initToolBar();
        loadDetail();
        initPagerBanner();
        initLocation();
    }
    private void initToolBar() {
        final ImageView iv_back = findViewById(R.id.iv_back);
        final TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.room_detail));
        iv_back.setImageDrawable(getResources().getDrawable(R.drawable.img_arrow));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void findView() {
        viewPager = findViewById(R.id.pager);
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

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 20;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 0, 10, 0);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            dots[i].setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
            dots[current].setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        }
    }
    private void initPagerBanner() {
        final LinearLayout layout_dots = findViewById(R.id.layout_dots);
        final int height = MyFunction.getInstance().getBannerHeight(this);
        viewPager.getLayoutParams().height = height;
        List<String> listImage = new ArrayList<>();
        try {
            adapter = new AdapterBanner(this, listImage,null);
            viewPager.setAdapter(adapter);
            addBottomDots(layout_dots, adapter.getCount(), 0);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDots(layout_dots, adapter.getCount(), position);
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

    private void initLocation() {
        try {
            if (locationManager == null)
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!MyFunction.getInstance().hasPermissions(ActivityRoomDetail.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) ActivityRoomDetail.this, PERMISSIONS, Global.PERMISSION_ALL);
                initMap();
            } else {
                initMap();
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage());
        }
    }
    private void initMap() {
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view));
        final int height = MyFunction.getInstance().getBannerHeight(this);
        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = height-200;
        mapFragment.getView().setLayoutParams(params);
        try {
            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(final GoogleMap map) {

                        if (map != null) {
                            mMap = map;
                            //initialize map
                            final LatLng latLng = new LatLng(lat, lng);

                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.img_pin);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, PIN_W, PIN_H, false);

                            MarkerOptions marker = new MarkerOptions().position(latLng).title("Aide et Action").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                            mMap.addMarker(marker);

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM), CAMERA_ANIMATE, null);
                            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                                @Override
                                public void onCameraMove() {
                                    LatLng latlng = mMap.getCameraPosition().target;
                                    ModelLatLng.getInstance().setLatlng(latlng);
                                }
                            });
                        }
                    }
                });
            } else {
                //new DialogAlertCustom(context, null, getString(R.string.error_occured)).show();
            }


        } catch (Exception e) {
            Log.e("Map ", e.getMessage() + "");
        }
    }
    private void initRelated(JSONArray array){
        final RecyclerView recyclerView = findViewById(R.id.recycler_relate);
        final LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new AdapterHome(null,this));
    }

    private void loadDetail(){
        try{
            JSONObject data =new JSONObject(MyFunction.getInstance().readFileAsset(this, "detail.json"));
            JSONArray array = data.getJSONArray("detail");
            initMenu(array);
            initRelated(null);
        }catch (Exception e){
            Log.e("Err",e.getMessage());
        }
    }
    private void initMenu(JSONArray array) {
        try {
            final String[] titles = {"Detail","Owner"};
            adapterPager = new AdapterPagerDetail(getSupportFragmentManager(),titles);
            adapterPager.addFrag(FragmentDetail.newInstance(array));
            adapterPager.addFrag(new FragmentDetail());
            final ViewPager view_pager = findViewById(R.id.view_pager);
            view_pager.setOffscreenPageLimit(2);
            view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
                @Override
                public void onPageSelected(int position) {

                }
                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            view_pager.setAdapter(adapterPager);
            final TabLayout tab_layout = findViewById(R.id.tab_layout);
            tab_layout.setupWithViewPager(view_pager);

        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }
}
