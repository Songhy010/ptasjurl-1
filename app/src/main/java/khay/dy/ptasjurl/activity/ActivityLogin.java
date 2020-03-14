package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
        initView();
    }

    private void initView() {
        findView();
        initLogin();
        initCreate();
    }

    private void initLogin() {
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFunction.getInstance().openActivity(ActivityLogin.this,ActivityHome.class);
                MyFunction.getInstance().finishActivity(ActivityLogin.this);
            }
        });
    }

    private void findView() {
    }
    private void initCreate(){
        findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(ActivityLogin.this,ActivityDonate.class);
            }
        });
    }
}
