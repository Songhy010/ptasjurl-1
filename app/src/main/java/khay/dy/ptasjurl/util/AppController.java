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
//http://sokhay.tcode.biz/wp-api/auth.php?api_id=home&type=pull
        Global.arData = new String[100];
        Global.arData[0] = "http://sokhay.tcode.biz/";
        Global.arData[1] = "wp-api/auth.php?";
        Global.arData[2] = "api_id=%s&type=%s";
        Global.arData[3] = "home";
        Global.arData[4] = "s0khay_rady";
        Global.arData[5] = "pull";

        Global.arData[6] = "real_estate";
        Global.arData[7] = "type";
        Global.arData[8] = "title";
        Global.arData[9] = "desc";
        Global.arData[10] = "price";
        Global.arData[11] = "thumbnail";
        Global.arData[12] = "branner";
        Global.arData[13] = "banner";
        Global.arData[14] = "room";
        Global.arData[15] = "flat";
        Global.arData[16] = "term_condition";
        Global.arData[17] = "donate";
        Global.arData[18] = "donate_desc";
        Global.arData[19] = "account_deac";
        Global.arData[20] = "account";
        Global.arData[21] = "number";

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
