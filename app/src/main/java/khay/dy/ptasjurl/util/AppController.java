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
        Global.arData[0] = "http://chekromlek.com/";
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
        Global.arData[22] = "about";
        Global.arData[23] = "contact";
        Global.arData[24] = "email";
        Global.arData[25] = "facebook";
        Global.arData[26] = "phone";
        Global.arData[27] = "login";
        Global.arData[28] = "username";
        Global.arData[29] = "password";
        Global.arData[30] = "first_name";
        Global.arData[31] = "last_name";
        Global.arData[32] = "owner_house";
        Global.arData[33] = "uid";
        Global.arData[34] = "notification";
        Global.arData[35] = "map";
        Global.arData[36] = "push_date";
        Global.arData[37] = "message";
        Global.arData[38] = "register";
        Global.arData[39] = "check_device";
        Global.arData[40] = "role";
        Global.arData[41] = "device_id";
        Global.arData[42] = "device_token";
        Global.arData[43] = "status";
        Global.arData[44] = "id";
        Global.arData[45] = "user";
        Global.arData[46] = "change_password";
        Global.arData[47] = "old_password";
        Global.arData[48] = "new_password";
        Global.arData[49] = "address";
        Global.arData[50] = "edit_profile";
        Global.arData[51] = "detail";
        Global.arData[52] = "owner";
        Global.arData[53] = "floor";
        Global.arData[54] = "people";
        Global.arData[55] = "e_price";
        Global.arData[56] = "w_price";
        Global.arData[57] = "parking";
        Global.arData[58] = "other_price";
        Global.arData[59] = "viewer";
        Global.arData[60] = "width";
        Global.arData[61] = "available";
        Global.arData[62] = "close_time";
        Global.arData[63] = "accessories";
        Global.arData[64] = "gallery";
        Global.arData[65] = "Authorization";
        Global.arData[66] = "length";
        Global.arData[67] = "add_basic_info";
        Global.arData[68] = "owner_name";
        Global.arData[69] = "phone1";
        Global.arData[70] = "phone2";
        Global.arData[71] = "flat_price";
        Global.arData[72] = "latitude";
        Global.arData[73] = "longitude";
        Global.arData[74] = "add_advance_info";
        Global.arData[75] = "room_price";
        Global.arData[76] = "name";
        Global.arData[77] = "related";
        Global.arData[78] = "filter";
        Global.arData[79] = "district";
        Global.arData[80] = "commune";
        Global.arData[81] = "village";
        Global.arData[82] = "province_id";
        Global.arData[83] = "district_id";
        Global.arData[84] = "commune_id";
        Global.arData[85] = "village_id";



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
        StringBuilder result = new StringBuilder();
        final int[] st = {100, 97, 116, 97};
        for (int value : st) {
            result.append((char) value);
        }
        return result.toString();
    }
}
