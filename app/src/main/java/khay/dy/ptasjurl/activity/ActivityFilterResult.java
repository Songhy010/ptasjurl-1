package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterHome;
import khay.dy.ptasjurl.listener.SelectedListener;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.ConstantStatus;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityFilterResult extends ActivityController {

    private final String TAG = "Ac Filter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);
        try {
            Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
            initView();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void initView() {
        initToolbar();
        loadFilter();
    }

    private void initToolbar() {
        final TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.search));
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initRecyclerView(JSONArray array) {
        final LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        final RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new AdapterHome(array, this));

    }

    private void loadFilter() {
        try {
            final HashMap<String, String> param = MyFunction.getInstance().getIntentHashMap(getIntent());
            showDialog();
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[78], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        final JSONObject object = new JSONObject(response);
                        final JSONArray array = object.getJSONArray(Global.arData[6]);
                        initRecyclerView(array);
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e(TAG, e.getMessage() + "");
                    loadFilter();
                    hideDialog();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            hideDialog();
        }
    }
}
