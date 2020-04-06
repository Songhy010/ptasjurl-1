package khay.dy.ptasjurl.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;

public class FragmentDetail extends Fragment {

    private final String TAG = "FragmentHouse";
    private View root_view;
    private static JSONObject data;

    public static FragmentDetail newInstance(JSONObject array) {
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
        MyFont.getInstance().setFont(root_view.getContext(), root_view, 2);
    }


    private void initView() {
        initDetail();
    }

    @SuppressLint("SetTextI18n")
    private void initDetail() {
        final TextView tv_type = root_view.findViewById(R.id.tv_type);
        final TextView tv_price = root_view.findViewById(R.id.tv_price);
        final TextView tv_floor = root_view.findViewById(R.id.tv_floor);
        final TextView tv_member = root_view.findViewById(R.id.tv_member);

        final TextView tv_electric = root_view.findViewById(R.id.tv_electric);
        final TextView tv_water = root_view.findViewById(R.id.tv_water);
        final TextView tv_park = root_view.findViewById(R.id.tv_park);
        final TextView tv_other = root_view.findViewById(R.id.tv_other);

        final TextView tv_available = root_view.findViewById(R.id.tv_available);
        final TextView tv_viewer = root_view.findViewById(R.id.tv_viewer);
        final TextView tv_close = root_view.findViewById(R.id.tv_close);
        final TextView tv_size = root_view.findViewById(R.id.tv_size);
        final TextView tv_desc = root_view.findViewById(R.id.tv_desc);

        final CheckBox check_aircon = root_view.findViewById(R.id.check_aircon);
        final CheckBox check_table = root_view.findViewById(R.id.check_table);
        final CheckBox check_secu_cam = root_view.findViewById(R.id.check_secu_cam);
        final CheckBox check_secu_man = root_view.findViewById(R.id.check_secu_man);

        try {
            final int type = Integer.parseInt(data.getString(Global.arData[7]));
            if (type == 1)
                tv_type.setText(getString(R.string.house));
            else
                tv_type.setText(getString(R.string.room));
            tv_price.setText(data.getString(Global.arData[10])+"$");
            tv_floor.setText(getString(R.string.floor) + " " + data.getString(Global.arData[53]));
            tv_member.setText(data.getString(Global.arData[54]) + " member");
            tv_electric.setText(data.getString(Global.arData[55]));
            tv_water.setText(data.getString(Global.arData[56]));
            tv_park.setText(data.getString(Global.arData[57]));
            tv_other.setText(data.getString(Global.arData[58]));
            final int available = Integer.parseInt(data.getString(Global.arData[61]));
            if (available == 1)
                tv_available.setText(getString(R.string.available));
            else {
                tv_available.setText(getString(R.string.busy));
                tv_available.setTextColor(root_view.getResources().getColor(R.color.colorAccent));
            }
            tv_viewer.setText(/*data.getString(Global.arData[59])*/"0");
            tv_close.setText(data.getString(Global.arData[62]));
            tv_size.setText(String.format("%sx%s", data.getString(Global.arData[60]), data.getString(Global.arData[66])));
            tv_desc.setText(data.getString(Global.arData[9]));

            final JSONArray array = data.getJSONArray(Global.arData[63]);
            for (int i = 0 ; i<array.length();i++){
                final JSONObject obj = array.getJSONObject(i);
                final int id = Integer.parseInt(obj.getString(Global.arData[44]));
                switch (id){
                    case 0:
                        check_secu_man.setChecked(true);
                        break;
                    case 1:
                        check_secu_cam.setChecked(true);
                        break;
                    case 2:
                        check_table.setChecked(true);
                        break;
                    case 3:
                        check_aircon.setChecked(true);
                        break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }
}
