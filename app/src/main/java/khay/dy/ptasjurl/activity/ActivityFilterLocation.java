package khay.dy.ptasjurl.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.SelectedListener;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityFilterLocation extends ActivityController {

    private TextView tv_province;
    private TextView tv_commune;
    private TextView tv_district;
    private TextView tv_village;
    private TextView tv_type;
    private String[] type = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_location);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        initView();
    }

    private void initView() {
        initToolbar();
        initLocation();
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

    private void initLocation() {
        type[0] = getString(R.string.room);
        type[1] = getString(R.string.house);
        tv_province = findViewById(R.id.tv_province);
        tv_commune = findViewById(R.id.tv_commune);
        tv_district = findViewById(R.id.tv_district);
        tv_village = findViewById(R.id.tv_village);
        tv_type = findViewById(R.id.tv_type);
        final RelativeLayout relative_province = findViewById(R.id.relative_province);
        final RelativeLayout relative_commune = findViewById(R.id.relative_commune);
        final RelativeLayout relative_district = findViewById(R.id.relative_district);
        final RelativeLayout relative_village = findViewById(R.id.relative_village);
        final RelativeLayout relative_type = findViewById(R.id.relative_type);
        MyFunction.getInstance().initSelectItem(this, relative_province, tv_province, type, 1, new SelectedListener() {
            @Override
            public void onSelected(int str) {
                Toast.makeText(ActivityFilterLocation.this, "" + str, Toast.LENGTH_SHORT).show();
            }
        });
        MyFunction.getInstance().initSelectItem(this, relative_commune, tv_commune, type, 1, new SelectedListener() {
            @Override
            public void onSelected(int str) {
                Toast.makeText(ActivityFilterLocation.this, "" + str, Toast.LENGTH_SHORT).show();
            }
        });
        MyFunction.getInstance().initSelectItem(this, relative_district, tv_district, type, 1, new SelectedListener() {
            @Override
            public void onSelected(int str) {
                Toast.makeText(ActivityFilterLocation.this, "" + str, Toast.LENGTH_SHORT).show();
            }
        });
        MyFunction.getInstance().initSelectItem(this, relative_village, tv_village, type, 1, new SelectedListener() {
            @Override
            public void onSelected(int str) {
                Toast.makeText(ActivityFilterLocation.this, "" + str, Toast.LENGTH_SHORT).show();
            }
        });
        MyFunction.getInstance().initSelectItem(this, relative_type, tv_type, type, 1, new SelectedListener() {
            @Override
            public void onSelected(int str) {
                Toast.makeText(ActivityFilterLocation.this, "" + str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
