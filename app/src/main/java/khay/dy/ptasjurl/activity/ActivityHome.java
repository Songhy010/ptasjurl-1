package khay.dy.ptasjurl.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterViewPager;
import khay.dy.ptasjurl.fragment.FragmentAdd;
import khay.dy.ptasjurl.fragment.FragmentHome;
import khay.dy.ptasjurl.fragment.FragmentHouse;
import khay.dy.ptasjurl.fragment.FragmentMap;
import khay.dy.ptasjurl.fragment.FragmentRoom;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.ConstantStatus;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;

public class ActivityHome extends ActivityController {

    private final String TAG = "Ac Home";
    private TabLayout tab_layout;
    private ViewPager view_pager;
    private final int TAB_COUNT = 5;
    private final Drawable[] drawables = new Drawable[TAB_COUNT];
    private final Bitmap[] bitmap = new Bitmap[TAB_COUNT];
    private final int[] icons = {
            R.drawable.ic_home, R.drawable.ic_room, R.drawable.ic_house,
            R.drawable.ic_map, R.drawable.ic_add
    };
    private final TextView[] tv_custom_tab = new TextView[TAB_COUNT];

    private ColorFilter filter;
    private ColorFilter filter_origin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 4)
                view_pager.setCurrentItem(0);
            else if (requestCode == ConstantStatus.ActivityMoreDesc)
                view_pager.setCurrentItem(0);
        }
    }

    private void findView() {
        view_pager = findViewById(R.id.view_pager);
    }

    private void initView() {
        findView();
        initViewPager();
        initTabLayout();
        registerNotification();
    }

    private void initViewPager() {
        view_pager.setOffscreenPageLimit(0);
        AdapterViewPager adapter = new AdapterViewPager(getSupportFragmentManager());
        adapter.addFrag(new FragmentHome());
        adapter.addFrag(new FragmentRoom());
        adapter.addFrag(new FragmentHouse());
        adapter.addFrag(new FragmentMap());
        adapter.addFrag(new FragmentAdd());
        view_pager.setAdapter(adapter);
    }

    private void initTabLayout() {
        try {
            filter = new LightingColorFilter(this.getResources().getColor(R.color.colorPrimary), 0);
            filter_origin = new LightingColorFilter(this.getResources().getColor(R.color.grey_4), 0);
            tab_layout = findViewById(R.id.tab_layout);
            tab_layout.setupWithViewPager(view_pager);
            tab_layout.addOnTabSelectedListener(onTabSelected);
            initTabIcons();
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    private void resetTabLayout() {
        try {
            for (int i = 0; i < TAB_COUNT; i++) {
                drawables[i].setColorFilter(filter_origin);
                tv_custom_tab[i].setCompoundDrawablesWithIntrinsicBounds(null, drawables[i], null, null);
                tv_custom_tab[i].setTextColor(getResources().getColor(R.color.grey_4));
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    private void setSelectedTab(Drawable drawable, TextView customTab) {
        try {
            drawable.setColorFilter(filter);
            customTab.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            customTab.setTextColor(getResources().getColor(R.color.colorPrimary));
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    private TabLayout.OnTabSelectedListener onTabSelected = new TabLayout.OnTabSelectedListener() {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            try {
                resetTabLayout();
                switch (tab.getPosition()) {
                    case 0:
                        setSelectedTab(drawables[0], tv_custom_tab[0]);
                        break;
                    case 1:
                        setSelectedTab(drawables[1], tv_custom_tab[1]);
                        break;
                    case 2:
                        setSelectedTab(drawables[2], tv_custom_tab[2]);
                        break;
                    case 3:
                        setSelectedTab(drawables[3], tv_custom_tab[3]);
                        break;
                    case 4:
                        if (MyFunction.getInstance().isHistory(ActivityHome.this))
                            setSelectedTab(drawables[4], tv_custom_tab[4]);
                        else
                            MyFunction.getInstance().openActivityForResult(ActivityHome.this, ActivityLogin.class, null, 4);
                        break;
                }
            } catch (Exception e) {
                Log.e("Err", e.getMessage() + "");
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void initTabIcons() {
        try {
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final int width = getResources().getDimensionPixelSize(R.dimen.iv_height_20);
            final String[] titles = {getString(R.string.home), getString(R.string.room), getString(R.string.house), getString(R.string.map), getString(R.string.add_house)};

            /*default selected */
            drawables[0] = ContextCompat.getDrawable(ActivityHome.this, icons[0]);
            assert drawables[0] != null;
            bitmap[0] = ((BitmapDrawable) drawables[0]).getBitmap();
            drawables[0] = new BitmapDrawable(this.getResources(), Bitmap.createScaledBitmap(bitmap[0], width, width, true));
            drawables[0].setColorFilter(filter);
            tv_custom_tab[0] = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, tab_layout, false);
            tv_custom_tab[0].setText(titles[0]);
            tv_custom_tab[0].setTextColor(getResources().getColor(R.color.colorPrimary));
            tv_custom_tab[0].setCompoundDrawablesWithIntrinsicBounds(null, drawables[0], null, null);
            tv_custom_tab[0].setLayoutParams(params);
            TabLayout.Tab tab = tab_layout.getTabAt(0);
            if (tab != null)
                tab.setCustomView(tv_custom_tab[0]);

            /* other tab  */
            for (int i = 1; i < TAB_COUNT; i++) {
                drawables[i] = ContextCompat.getDrawable(ActivityHome.this, icons[i]);
                assert drawables[i] != null;
                bitmap[i] = ((BitmapDrawable) drawables[i]).getBitmap();
                drawables[i] = new BitmapDrawable(this.getResources(), Bitmap.createScaledBitmap(bitmap[i], width, width, true));
                drawables[i].setColorFilter(filter_origin);
                tv_custom_tab[i] = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, tab_layout, false);
                tv_custom_tab[i].setText(titles[i]);
                tv_custom_tab[i].setTextColor(getResources().getColor(R.color.grey_4));
                tv_custom_tab[i].setCompoundDrawablesWithIntrinsicBounds(null, drawables[i], null, null);
                tv_custom_tab[i].setLayoutParams(params);
                tab = tab_layout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(tv_custom_tab[i]);
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }


    private void registerNotification() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("task", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        final String deviceId = MyFunction.getInstance().getAndroidID(ActivityHome.this);
                        Log.e("Device ID", deviceId);
                        final String token = task.getResult().getToken();
                        Log.e("Token", token);
                        sendTokenToServer(token, deviceId);
                    }
                });
    }

    private void sendTokenToServer(final String token, final String deviceId) {
        try {
            final JSONObject info = new JSONObject(MyFunction.getInstance().getText(this, Global.INFO_FILE));
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[39], Global.arData[5]);
            final HashMap<String, String> param = new HashMap<>();
            param.put(Global.arData[41], deviceId);
            param.put(Global.arData[42], token);
            param.put(Global.arData[33], info.getString(Global.arData[33]));
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        final int code = Integer.parseInt(response);
                        if (code == 3) {
                            Log.e("Success", response);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e(TAG, e.getMessage() + "");
                    sendTokenToServer(token, deviceId);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
