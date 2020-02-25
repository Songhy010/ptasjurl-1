package khay.dy.ptasjurl.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.ToolbarCallBack;
import khay.dy.ptasjurl.util.MyFunction;


public class ActivityController extends AppCompatActivity {

    private Dialog dialog;
    private RelativeLayout layoutLoading;
    private ProgressDialog progressdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MyFunction.getInstance().setLanguage(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //MyFunction.getInstance().setLanguage(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MyFunction.getInstance().setLanguage(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyFunction.getInstance().finishActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                MyFunction.getInstance().finishActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void hideDialog() {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
            if (layoutLoading != null) {
                layoutLoading.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("Err: ", e.getMessage() + "");
        }
    }
    public void showDialog() {
        try {
            hideDialog();
            if (dialog == null) {
                dialog = new Dialog(this, android.R.style.Theme_Panel);
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface di, int keyCode, KeyEvent event) {
                        if (dialog.isShowing()) {
                            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                hideDialog();
                            }
                            return false;
                        }
                        return true;
                    }
                });
            }
            dialog.show();
            if (layoutLoading == null) {
                /*TextView textView = new TextView(this);

                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setGravity(Gravity.CENTER);
                textView.setText("loading...");*/

                ProgressBar pbLoading = new ProgressBar(this, null, android.R.attr.progressBarStyle);
                pbLoading.setIndeterminate(true);
                pbLoading.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
                pbLoading.setLayoutParams(rlp);

                //layoutLoading.addView(textView);

                layoutLoading = new RelativeLayout(this);
                layoutLoading.addView(pbLoading);
                ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).addView(layoutLoading);
            }
            layoutLoading.setVisibility(View.VISIBLE);
            layoutLoading.bringToFront();
        } catch (Exception e) {
            Log.e("error show dialog", e.getMessage() + "-");
        }
    }

    protected void initTopMenu(final String text, final ToolbarCallBack listener) {
        try {
            final TextView tv_title = findViewById(R.id.tv_title);
            tv_title.setText(text);
            findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   listener.onBack();
                }
            });
            findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSearch();
                }
            });
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }
}
