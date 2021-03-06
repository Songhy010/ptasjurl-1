package khay.dy.ptasjurl.activity;

import androidx.cardview.widget.CardView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

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
        initBack();
        loadDonate();
    }

    private void initBack() {
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
            final String lang = MyFunction.getInstance().getText(this, Global.LANGUAGE).equals("")? "km":MyFunction.getInstance().getText(this, Global.LANGUAGE);
            final HashMap<String,String> param = new HashMap<>();
            param.put(Global.arData[93],lang);
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[17], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(TAG, response);
                        final JSONObject object = new JSONObject(response);
                        final TextView tv_desc = findViewById(R.id.tv_desc);
                        //tv_desc.setText(object.getString(Global.arData[18]));
                        MyFunction.getInstance().displayHtmlInText(tv_desc,object.getString(Global.arData[18]));
                        final JSONArray array = object.getJSONArray(Global.arData[20]);
                        final TextView tv_account = findViewById(R.id.tv_acc);
                        tv_account.setText(object.getString(Global.arData[19]));
                        final TextView[] tv = {findViewById(R.id.tv_wing), findViewById(R.id.tv_aba)};
                        final CardView[] card = {findViewById(R.id.card_wing), findViewById(R.id.card_aba)};
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject obj = array.getJSONObject(i);
                            tv[i].setText(obj.getString(Global.arData[21]));
                            final int finalI = i;
                            card[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CopyText(tv[finalI]);
                                }
                            });
                        }
                        final RelativeLayout relative = findViewById(R.id.relative);
                        relative.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e("Err", e.getMessage() + "");
                    loadDonate();
                    hideDialog();
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public void CopyText(TextView view) {
        String text = view.getText().toString();
        if (!text.isEmpty()) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("key", text);
            assert clipboardManager != null;
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getApplicationContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show();
        }
    }
}
