package khay.dy.ptasjurl.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterItem;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.model.ModelItem;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;

public class FragmentHouse extends Fragment implements AdapterItem.OnLoadMoreListener {

    private final String TAG = "FragmentHouse";
    private View root_view;
    private RecyclerView recycler;
    private SwipeRefreshLayout swipe;
    private LinearLayoutManager manager;
    private AdapterItem mAdapter;
    private ArrayList<ModelItem> modelItemList;
    private int index = 3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_house, container, false);
        }
        return root_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        /* TODO set font with current language */
        MyFont.getInstance().setFont(root_view.getContext(),root_view,2);
    }

    private void findView(){
        swipe = root_view.findViewById(R.id.swipe);
        recycler  = root_view.findViewById(R.id.recycler);
    }

    private void initView(){
        try{
            findView();
            initToolBar();
            loadFlat();
            initSwipe();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    private void initSwipe(){
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFlat();
            }
        });
    }

    private void initToolBar() {
        final TextView tv_title = root_view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.house));
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
                modelItemList.add(new ModelItem(obj.getString(Global.arData[11]), obj.getString(Global.arData[44]), obj.getString(Global.arData[7]), obj.getString(Global.arData[69]), obj.getString(Global.arData[10])));
            }
            return modelItemList;
        }catch (Exception e){
            Log.e("Err",e.getMessage()+"");
        }
        return null;
    }

    private void loadFlat(){
        modelItemList = new ArrayList<>();
        swipe.setRefreshing(true);
        final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[15], Global.arData[5]);
        MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                try{
                    index = 3;
                    final JSONObject object = new JSONObject(response);
                    final JSONArray arrFlat = object.getJSONArray(Global.arData[15]);
                    initRecyclerView();
                    mAdapter.addAll(Data(arrFlat));
                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                }
                swipe.setRefreshing(false);
            }

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG,""+e.getMessage());
                swipe.setRefreshing(false);
                loadFlat();
            }
        });
    }
    private void loadFlatMore(){
        final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[15], Global.arData[5]);
        final HashMap<String, String> param = new HashMap<>();
        param.put(Global.arData[88], index + "");
        MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                try{
                    index = index + 3;
                    mAdapter.dismissLoading();
                    final JSONObject object = new JSONObject(response);
                    final JSONArray arrFlat = object.getJSONArray(Global.arData[15]);
                    mAdapter.addItemMore(Data(arrFlat));
                    mAdapter.setMore(true);
                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG,""+e.getMessage());
                loadFlatMore();
            }
        });
    }

    @Override
    public void onLoadMore() {
        loadFlatMore();
    }
}
