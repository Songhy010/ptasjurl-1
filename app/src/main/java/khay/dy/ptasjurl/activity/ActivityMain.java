package khay.dy.ptasjurl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
                if(MyFunction.getInstance().isHistory(ActivityMain.this)){
                    MyFunction.getInstance().openActivity(ActivityMain.this, ActivityHome.class);
                    MyFunction.getInstance().finishActivity(ActivityMain.this);
                }else {
                    MyFunction.getInstance().openActivity(ActivityMain.this, ActivityLogin.class);
                    MyFunction.getInstance().finishActivity(ActivityMain.this);
                }
            }
        }, 3000);

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
}
