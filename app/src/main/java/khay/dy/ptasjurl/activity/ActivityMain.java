package khay.dy.ptasjurl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        if (MyFunction.getInstance().isHistory(ActivityMain.this)) {
            loadHome();
        } else {
            MyFunction.getInstance().openActivity(ActivityMain.this, ActivityLogin.class);
            MyFunction.getInstance().finishActivity(ActivityMain.this);
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("task", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        /*String token = task.getResult().getToken();
                        Log.e("Token", token);*/
                    }
                });
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
}
