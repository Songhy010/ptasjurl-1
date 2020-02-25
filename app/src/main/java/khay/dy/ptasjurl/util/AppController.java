package khay.dy.ptasjurl.util;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends com.android.volley.my.AppController {
    private static AppController mInstance;
    private static RequestQueue queue;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


       /* final String global = MyFunction.getInstance().getDecrypted(this, MyFunction.getInstance().readFileAsset(this, getFilename()));
        Global.arData = global.split(";");*/

        Global.arData = new String[100];
        Global.arData[0] = "https://admin.khmersmartlibrary.org/";
        Global.arData[1] = "wp-api/auth?";
        Global.arData[2] = "api_id=%s&type=%s";
        Global.arData[3] = "user";
        Global.arData[4] = "push";
        Global.arData[5] = "check";
        Global.arData[6] = "pull";
        Global.arData[7] = "first_name";
        Global.arData[8] = "last_name";

        Global.arData[9] = "language";
        Global.arData[10] = "en";
        Global.arData[11] = "km";
        Global.arData[12] = "cn";

        Global.arData[13] = "gender";
        Global.arData[14] = "biz";
        Global.arData[15] = "Khm3rL!br@ry";
        Global.arData[16] = "fid";
        Global.arData[17] = "ftoken";
        Global.arData[18] = "avatar";
        Global.arData[19] = "servey";
        Global.arData[20] = "location";
        Global.arData[21] = "position";
        Global.arData[22] = "answer";
        Global.arData[23] = "library";
        Global.arData[24] = "home";


/*
        String s = "";
        for (int i = 0; i < Global.arData.length; i++) {
            s += Global.arData[i] + ";";
        }
        final String encrypted = MyFunction.getInstance().getEncrypted(this, s);
        Log.e("Encrypted", encrypted + "");
*/

        queue = Volley.newRequestQueue(this.getApplicationContext());

    }

    public <T> void addToRequestQueue(Request<T> req) {
        queue.add(req);
    }


    private String getFilename() {
        String result = "";
        final int st[] = {100, 97, 116, 97};
        for (int i = 0; i < st.length; i++) {
            result += (char) st[i];
        }
        return result;
    }
}
