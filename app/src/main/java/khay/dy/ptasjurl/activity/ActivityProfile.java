package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterProfile;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityProfile extends AppCompatActivity {

    private String[] menu = {"Profile","My House","Donation","About Us","Contact Us","Term Condition","Setting"};
    private RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
        initView();
    }

    private void findView(){
        recycler = findViewById(R.id.recycler);
    }

    private void initView(){
        findView();
        initBack();
        initMenu(menu);
    }

    private void initBack() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().finishActivity(ActivityProfile.this);
            }
        });
    }

    private void initMenu(String[] menu){
        final LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new AdapterProfile(this,menu));
    }
}
