package khay.dy.ptasjurl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.model.ModelHome;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;

public class ActivityMain extends ActivityController {

    private final String TAG = "Ac Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        initData();
        initLocalize();

    }

    private void initData() {
        loadHome();
        loadAddress();
    }


    private void initLocalize() {
        final String lang = MyFunction.getInstance().getText(ActivityMain.this, Global.LANGUAGE);
        if(lang.isEmpty()){
            Locale locale = new Locale("km");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }else{
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
        //lang = MyFunction.getInstance().getText(ActivityMain.this, AirConConstant.LANG);

    }

    private void loadHome() {
        final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[3], Global.arData[5]);
        MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject object = new JSONObject(response);
                    ModelHome.getInstance().setObjHome(object);
                    MyFunction.getInstance().openActivity(ActivityMain.this, ActivityHome.class);
                    MyFunction.getInstance().finishActivity(ActivityMain.this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage() + "");
                loadHome();
            }
        });
    }

    private void loadAddress() {
        final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[49], Global.arData[5]);
        MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    MyFunction.getInstance().saveText(ActivityMain.this,Global.ADDRESS,response);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage() + "");
                loadHome();
            }
        });
    }
}
