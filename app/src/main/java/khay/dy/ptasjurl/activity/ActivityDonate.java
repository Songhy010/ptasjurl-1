package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityDonate extends ActivityController {

    private String TAG = "Ac Donate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        initView();
    }

    private void initView() {
        findView();
        initBack();
        loadDonate();
    }

    private void findView() {

    }

    private void initBack(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().finishActivity(ActivityDonate.this);
            }
        });
    }

    private void loadDonate() {
        try {
            showDialog();
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[17], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(TAG, response);
                        final JSONObject object = new JSONObject(response);
                        final TextView tv_desc = findViewById(R.id.tv_desc);
                        tv_desc.setText(object.getString(Global.arData[18]));
                        final JSONArray array = object.getJSONArray(Global.arData[20]);
                        final TextView[] tv = {findViewById(R.id.tv_wing),findViewById(R.id.tv_aba)};
                        for (int i = 0; i < array.length(); i++){
                            final JSONObject obj = array.getJSONObject(i);
                            tv[i].setText(obj.getString(Global.arData[21]));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e("Err", e.getMessage() + "");
                    hideDialog();
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.e(TAG, "" + e.getMessage());
        }
    }
}
