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

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterHome;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;

public class FragmentHouse extends Fragment {

    private final String TAG = "FragmentHouse";
    private View root_view;
    private RecyclerView recycler;
    private SwipeRefreshLayout swipe;
    private LinearLayoutManager manager;


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

    private void loadFlat(){
        swipe.setRefreshing(true);
        final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[15], Global.arData[5]);
        MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                try{
                    final JSONObject object = new JSONObject(response);
                    final JSONArray arrFlat = object.getJSONArray(Global.arData[15]);
                    initRecycler(arrFlat);
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

    private void initRecycler(JSONArray array) {
        manager = new LinearLayoutManager(root_view.getContext(),RecyclerView.VERTICAL,false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new AdapterHome(array,root_view.getContext()));
    }
}
