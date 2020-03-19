package khay.dy.ptasjurl.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityTermAndCondition extends ActivityController {
    private String TAG = "Ac Term";
    private TextView tv_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        setContentView(R.layout.activity_term_and_condition);
        initView();
    }

    private void initView() {
        findView();
        initToolBar();
        loadTerm();
    }

    private void initToolBar() {
        final ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void findView() {
        tv_desc = findViewById(R.id.tv_desc);
    }

    private void loadTerm() {
        try {
            showDialog();
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[16], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(TAG, response);
                        final JSONObject object = new JSONObject(response);
                        tv_desc.setText(object.getString(Global.arData[9]));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tv_desc.setText(Html.fromHtml(object.getString(Global.arData[9]), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            tv_desc.setText(Html.fromHtml(object.getString(Global.arData[9])));
                        }
                        final ScrollView scroll = findViewById(R.id.scroll);
                        scroll.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e("Err", e.getMessage() + "");
                    loadTerm();
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.e(TAG, "" + e.getMessage());
        }
    }
}
