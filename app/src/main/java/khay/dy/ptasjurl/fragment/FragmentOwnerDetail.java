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

import org.json.JSONArray;
import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFont;

public class FragmentOwnerDetail extends Fragment {

    private final String TAG = "FragmentHouse";
    private View root_view;
    private static JSONObject data;

    public static FragmentOwnerDetail newInstance(JSONObject array) {
        FragmentOwnerDetail myFragment = new FragmentOwnerDetail();
        data = array;

        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_owner_detail, container, false);
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
        initData();
    }

    private void initData() {
        try {
            final TextView tv_name = root_view.findViewById(R.id.tv_name);
            final TextView tv_phone = root_view.findViewById(R.id.tv_phone);
            final TextView tv_phone1 = root_view.findViewById(R.id.tv_phone1);

            final String name = data.getString(Global.arData[76]).equals("null") ? "N/A" : data.getString(Global.arData[76]);
            tv_name.setText(name);
            final String phone = data.getString(Global.arData[69]).equals("null")? "N/A" : data.getString(Global.arData[69]);
            tv_phone.setText(phone);
            final String phone1 = data.getString(Global.arData[70]).equals("null")? "N/A" : data.getString(Global.arData[70]);
            tv_phone1.setText(phone1);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }
}
