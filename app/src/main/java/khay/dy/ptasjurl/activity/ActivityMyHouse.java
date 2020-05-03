package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterHome;
import khay.dy.ptasjurl.adapter.AdapterItem;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.model.ModelItem;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityMyHouse extends ActivityController implements AdapterItem.OnLoadMoreListener {

    private final String TAG = "Ac MyHouse";
    private int index = 3;
    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private AdapterItem mAdapter;
    private ArrayList<ModelItem> modelItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_house);
        try {
            Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
            initView();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void initView() {
        initToolbar();
        loadMyHouse();
    }

    private void initToolbar() {
        final TextView tv_title = findViewById(R.id.tv_title);
        final ImageView iv_back = findViewById(R.id.iv_back);
        tv_title.setText(getString(R.string.my_house));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initRecyclerView() {
        manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(manager);
        mAdapter = new AdapterItem(this, this);
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
                modelItemList.add(new ModelItem(obj.getString(Global.arData[11]), obj.getString(Global.arData[44]), obj.getString(Global.arData[7]), obj.getString(Global.arData[69]), obj.getString(Global.arData[10])));
            }
            return modelItemList;
        }catch (Exception e){
            Log.e("Err",e.getMessage()+"");
        }
        return null;
    }

    private void loadMyHouse() {
        try {
            modelItemList = new ArrayList<>();
            showDialog();
            final JSONObject object = new JSONObject(MyFunction.getInstance().getText(this, Global.INFO_FILE));
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[32], Global.arData[5]);
            final HashMap<String, String> param = new HashMap<>();
            param.put(Global.arData[33], object.getString(Global.arData[33]));
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(TAG, response);
                        index = 3;
                        final JSONObject object = new JSONObject(response);
                        final JSONArray array = object.getJSONArray(Global.arData[6]);
                        initRecyclerView();
                        mAdapter.addAll(Data(array));
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    loadMyHouse();
                    Log.e("Err", e.getMessage() + "");
                    hideDialog();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void loadMyHouseMore() {
        try {
            final JSONObject object = new JSONObject(MyFunction.getInstance().getText(this, Global.INFO_FILE));
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[32], Global.arData[5]);
            final HashMap<String, String> param = new HashMap<>();
            param.put(Global.arData[33], object.getString(Global.arData[33]));
            param.put(Global.arData[88], index + "");
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(TAG, response);
                        index = index + 3;
                        mAdapter.dismissLoading();
                        final JSONObject object = new JSONObject(response);
                        final JSONArray array = object.getJSONArray(Global.arData[6]);
                        mAdapter.addItemMore(Data(array));
                        mAdapter.setMore(true);
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    loadMyHouseMore();
                    Log.e("Err", e.getMessage() + "");
                    hideDialog();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    @Override
    public void onLoadMore() {
        loadMyHouseMore();
    }
}