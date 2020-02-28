package khay.dy.ptasjurl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterHome;
import khay.dy.ptasjurl.util.MyFont;

public class FragmentHouse extends Fragment {

    private final String TAG = "FragmentHouse";
    private View root_view;
    private RecyclerView recycler;
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
        recycler  = root_view.findViewById(R.id.recycler);
    }

    private void initView(){
        findView();
        initToolBar();
        initRecycler();
    }

    private void initToolBar() {
        final TextView tv_title = root_view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.house));
    }

    private void initRecycler() {
        manager = new LinearLayoutManager(root_view.getContext(),RecyclerView.VERTICAL,false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new AdapterHome(null,root_view.getContext(),R.layout.item_house));
    }
}
