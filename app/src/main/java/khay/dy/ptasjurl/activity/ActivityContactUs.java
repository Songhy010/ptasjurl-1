package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityContactUs extends ActivityController {

    private final String TAG = "Ac Contact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        try {
            Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
            initView();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void initView() {
        initBack();
        loadContact();
    }

    private void initBack() {
        final ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initLink(String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + link));
            intent.setPackage(Global.PACKAGE_IG);
            try {
                startActivity(intent);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(link)));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    private void loadContact() {
        try {
            showDialog();
            final String lang = MyFunction.getInstance().getText(this, Global.LANGUAGE).equals("")? "kh":MyFunction.getInstance().getText(this, Global.LANGUAGE);
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[23], Global.arData[5]);
            final HashMap<String,String> param = new HashMap<>();
            param.put(Global.arData[93],lang);
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(TAG, response);
                        final JSONObject object = new JSONObject(response);
                        final TextView tv_desc = findViewById(R.id.tv_desc);
                        MyFunction.getInstance().displayHtmlInText(tv_desc, object.getString(Global.arData[9]));
                        final TextView tv_email = findViewById(R.id.tv_email);
                        final TextView tv_fb = findViewById(R.id.tv_facebook);
                        final LinearLayout linear_web = findViewById(R.id.linear_web);
                        linear_web.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                initLink("http://chekromlek.com/");
                            }
                        });
                        tv_fb.setText(object.getString(Global.arData[25]));

                        tv_email.setText(object.getString(Global.arData[24]));
                        tv_email.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent email = new Intent(Intent.ACTION_SEND);
                                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{object.getString(Global.arData[24])});
                                    email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                                    email.putExtra(Intent.EXTRA_TEXT, "");

                                    //need this to prompts email client only
                                    email.setType("message/rfc822");

                                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage() + "");
                                }
                            }
                        });
                        final FlexboxLayout flex = findViewById(R.id.flex);
                        final JSONArray array = object.getJSONArray(Global.arData[26]);
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject obj = array.getJSONObject(i);
                            final View view = LayoutInflater.from(ActivityContactUs.this).inflate(R.layout.custom_tab, null, false);
                            final TextView tv_phone = view.findViewById(R.id.tab);
                            tv_phone.setTextColor(getResources().getColor(R.color.light_blue_50));
                            tv_phone.setTextSize(12);
                            tv_phone.setText("  " + obj.getString(Global.arData[21]));
                            tv_phone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse(String.format("tel:855%s", obj.getString(Global.arData[21]).substring(1))));
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Log.e(TAG, e.getMessage() + "");
                                    }
                                }
                            });
                            flex.addView(view);
                        }
                        final LinearLayout linear = findViewById(R.id.linear);
                        linear.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e("Err", e.getMessage() + "");
                    loadContact();
                    hideDialog();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }
}
