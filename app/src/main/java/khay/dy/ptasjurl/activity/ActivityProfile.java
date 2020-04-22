package khay.dy.ptasjurl.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.my.MyImageLoader;

import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterProfile;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityProfile extends ActivityController {

    private final String TAG = "Ac Profile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 0){
                MyFunction.getInstance().openActivity(ActivityProfile.this,ActivityProfileView.class);
            }else if(requestCode == 1){
                if(MyFunction.getInstance().isHistory(ActivityProfile.this)){
                    MyFunction.getInstance().openActivity(ActivityProfile.this,ActivityMyHouse.class);
                }
            }
        }
    }

    private void initView(){
        initBack();
        initMenu();
        initProfile();
        initFB();
        initIG();
        initYT();
    }

    private void initProfile(){
        try{
            final JSONObject object = new JSONObject(MyFunction.getInstance().getText(this, Global.INFO_FILE));
            final TextView tv_name = findViewById(R.id.tv_name);
            tv_name.setText(object.getString(Global.arData[30])+" "+object.getString(Global.arData[31]));
            final ImageView iv_profile = findViewById(R.id.iv_profile);
            MyImageLoader.getInstance().setImage(iv_profile, object.getString(Global.arData[87]), null, 0, 0, -1, R.drawable.img_loading, R.drawable.img_loading);

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

    private void initMenu(){
        final String[] menu = {getString(R.string.profile),getString(R.string.my_house),getString(R.string.donating),getString(R.string.about),getString(R.string.contact),getString(R.string.term_condition),getString(R.string.setting)};
        final LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        final RecyclerView recycler  = findViewById(R.id.recycler);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new AdapterProfile(this,menu));
    }

    private void initFB(){
        findViewById(R.id.iv_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLink("1118241991550593","fb");
            }
        });
    }

    private void initYT(){
        findViewById(R.id.iv_tg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLink("tg://resolve?domain=radyphen","tg");
            }
        });
    }

    private void initIG(){
        findViewById(R.id.iv_ig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLink("https://instagram.com/deejaysoda?igshid=uka6kgzpcxo7","ig");
            }
        });
    }

    private void initLink(String link,String title){
        try {
            if(title.equals("fb")){
                link = "fb://page/"+link;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            intent.setPackage(Global.PACKAGE_IG);
            try {
                startActivity(intent);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(link)));
            }
        } catch(Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }
}
