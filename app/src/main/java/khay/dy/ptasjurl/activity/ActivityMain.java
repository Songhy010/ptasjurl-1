package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.MyFunction;

public class ActivityMain extends ActivityController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MyFunction.getInstance().openActivity(ActivityMain.this,ActivityHome.class);
                MyFunction.getInstance().finishActivity(ActivityMain.this);
            }
        }, 3000);
    }
}
