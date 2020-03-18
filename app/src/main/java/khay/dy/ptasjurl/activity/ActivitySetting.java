package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Tools;

public class ActivitySetting extends ActivityController {

    private final String TAG = "Ac Setting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        try{
            Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
            initView();
        }catch (Exception e){
            Log.e(TAG,e.getMessage()+"");
        }
    }

    private void initView() {
        initBack();
    }

    private void initBack(){
        final ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
