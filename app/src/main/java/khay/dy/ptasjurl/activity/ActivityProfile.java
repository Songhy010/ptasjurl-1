package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterProfile;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityProfile extends AppCompatActivity {

    private final String TAG = "Ac Profile";
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
        initProfile();
    }

    private void initProfile(){
        try{
            final JSONObject object = new JSONObject(MyFunction.getInstance().getText(this, Global.INFO_FILE));
            final TextView tv_name = findViewById(R.id.tv_name);
            tv_name.setText(object.getString(Global.arData[30])+" "+object.getString(Global.arData[31]));

        }catch (Exception e){
            Log.e(TAG,e.getMessage()+"");
        }
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
