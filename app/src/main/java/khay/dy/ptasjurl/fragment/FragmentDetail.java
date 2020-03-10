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

import org.json.JSONArray;
import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterDetail;
import khay.dy.ptasjurl.adapter.AdapterHome;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;

public class FragmentDetail extends Fragment {

    private final String TAG = "FragmentHouse";
    private View root_view;
    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private static JSONArray data;

    public static FragmentDetail newInstance(JSONArray array){
        FragmentDetail myFragment = new FragmentDetail();
        data = array;

        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail, container, false);
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
        recycler  = root_view.findViewById(R.id.recycler);
    }

    private void initView(){
        findView();
        initRecycler();
    }

    private void initRecycler() {
        try{
            manager = new LinearLayoutManager(root_view.getContext(),RecyclerView.VERTICAL,false);
            recycler.setLayoutManager(manager);
            recycler.setAdapter(new AdapterDetail(root_view.getContext(),data));
        }catch (Exception e){
            Log.e("Err",e.getMessage());
        }
    }
}
