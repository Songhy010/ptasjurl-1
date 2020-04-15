package khay.dy.ptasjurl.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.SelectedListener;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.ConstantStatus;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityFilterLocation extends ActivityController {

    private final String TAG = "Ac Filter";

    private TextView tv_province;
    private TextView tv_commune;
    private TextView tv_district;
    private TextView tv_village;
    private TextView tv_type;
    private String[] type = new String[2];
    private HashMap<String,String> param = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_location);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        initView();
    }

    private void initView() {
        initToolbar();
        loadLocation();
        initLocation();
    }

    private void initToolbar() {
        final TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.search));
        final ImageView iv_search = findViewById(R.id.iv_search);
        iv_search.setVisibility(View.VISIBLE);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivityForResult(ActivityFilterLocation.this,ActivityFilterResult.class,param, ConstantStatus.ActivityFilterResult);
            }
        });
    }

    private void initLocation() {
        type[1] = getString(R.string.room);
        type[0] = getString(R.string.house);
        tv_province = findViewById(R.id.tv_province);
        tv_commune = findViewById(R.id.tv_commune);
        tv_district = findViewById(R.id.tv_district);
        tv_village = findViewById(R.id.tv_village);
        tv_type = findViewById(R.id.tv_type);


        final RelativeLayout relative_type = findViewById(R.id.relative_type);

        MyFunction.getInstance().initSelectItem(this, relative_type, tv_type, type, 1, new SelectedListener() {
            @Override
            public void onSelected(int str) {
                param.put("type",(str+1)+"");
            }
        });
    }

    private void loadLocation() {
        try {
            showDialog();
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[49], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        final JSONArray arrPro = new JSONArray(response);
                        final RelativeLayout relative_province = findViewById(R.id.relative_province);
                        MyFunction.getInstance().initSelectItem(ActivityFilterLocation.this, relative_province, tv_province, getArrStrFromArrJson(Global.arData[76],arrPro), 1, new SelectedListener() {
                            @Override
                            public void onSelected(int str) {
                                try{
                                    final JSONObject objPro = arrPro.getJSONObject(str);
                                    param.put (Global.arData[82],objPro.getString(Global.arData[44]));
                                    final JSONArray arrDis = objPro.getJSONArray(Global.arData[79]);
                                    final RelativeLayout relative_district = findViewById(R.id.relative_district);
                                    MyFunction.getInstance().initSelectItem(ActivityFilterLocation.this, relative_district, tv_district, getArrStrFromArrJson(Global.arData[76],arrDis), 1, new SelectedListener() {
                                        @Override
                                        public void onSelected(int str) {
                                            try {
                                                final JSONObject objDis = arrDis.getJSONObject(str);
                                                param.put (Global.arData[83],objDis.getString(Global.arData[44]));
                                                final JSONArray arrCom = objDis.getJSONArray(Global.arData[80]);
                                                final RelativeLayout relative_commune = findViewById(R.id.relative_commune);
                                                MyFunction.getInstance().initSelectItem(ActivityFilterLocation.this, relative_commune, tv_commune, getArrStrFromArrJson(Global.arData[76],arrCom), 1, new SelectedListener() {
                                                    @Override
                                                    public void onSelected(int str) {
                                                        try{
                                                            final JSONObject objCom = arrCom.getJSONObject(str);
                                                            param.put (Global.arData[84],objCom.getString(Global.arData[44]));
                                                            final JSONArray arrVil = objCom.getJSONArray(Global.arData[81]);
                                                            final RelativeLayout relative_village = findViewById(R.id.relative_village);
                                                            MyFunction.getInstance().initSelectItem(ActivityFilterLocation.this, relative_village, tv_village, getArrStrFromArrJson(Global.arData[76],arrVil), 1, new SelectedListener() {
                                                                @Override
                                                                public void onSelected(int str) {
                                                                    try {
                                                                        final JSONObject objVil = arrVil.getJSONObject(str);
                                                                        param.put (Global.arData[85],objVil.getString(Global.arData[44]));
                                                                    }catch (Exception e){
                                                                        Log.e(TAG,e.getMessage()+"");
                                                                    }
                                                                }
                                                            });
                                                        }catch (Exception e){
                                                            Log.e(TAG,e.getMessage()+"");
                                                        }
                                                    }
                                                });
                                            }catch (Exception e){
                                                Log.e(TAG,e.getMessage()+"");
                                            }

                                        }
                                    });
                                }catch (Exception e){
                                    Log.e(TAG,e.getMessage()+"");
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e(TAG, e.getMessage() + "");
                    loadLocation();
                    hideDialog();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            hideDialog();
        }
    }

    private String[] getArrStrFromArrJson(final String objectName, final JSONArray array) {
        final String[] str = new String[array.length()];
        for (int i = 0; i < array.length(); i++){
            try{
                final JSONObject object = array.getJSONObject(i);
                str[i]= object.getString(objectName);
            }catch (Exception e){
                Log.e(TAG,e.getMessage()+"");
            }
        }
            return str;
    }
}
