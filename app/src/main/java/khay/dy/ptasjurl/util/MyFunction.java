package khay.dy.ptasjurl.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;


import org.myjson.JSONArray;
import org.myjson.JSONException;
import org.myjson.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.AlertListenner;
import khay.dy.ptasjurl.listener.VolleyCallback;

public class MyFunction {
    private static final MyFunction ourInstance = new MyFunction();

    public static MyFunction getInstance() {
        return ourInstance;
    }

    private MyFunction() {
    }

    public void finishActivity(final Context context) {
        try {
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }
    public void finishActivityForResult(Activity activity, final HashMap<Integer, String> hashMap) {
        try {
            Intent i = new Intent();
            if (hashMap != null) {
                i.putExtra("map", hashMap);
                activity.setResult(Activity.RESULT_OK, i);
            }
            ((Activity) activity).finish();
            ((Activity) activity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void openActivity(final Context context, final Class<?> frmClass, final HashMap<String, String> hashMap) {
        openActivity(context, frmClass, hashMap, false, false, false);
    }

    public void openActivityFromThread(final Context context, final Class<?> frmClass, final HashMap<String, String> hashMap) {
        openActivity(context, frmClass, hashMap, false, false, true);
    }

    public void openActivity(final Context context, final Class<?> frmClass, final HashMap<String, String> hashMap, final boolean clearTop, final boolean clearAll, final boolean fromThread) {
        try {
            Intent i = new Intent(context, frmClass);
            if (clearTop) {
                if (getAppVersion(context) >= 11) {
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    i = Intent.makeRestartActivityTask(i.getComponent());
                }
            }
            if (clearAll) {
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ((Activity) context).finish();
            }
            if (fromThread) {
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            if (hashMap != null) {
                i.putExtra("map", hashMap);
            }
            context.startActivity(i);
            ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
        }
    }

    public void openActivityForResult(final Context context, final Class<?> frmClass, final HashMap<String, String> hashMap, int code) {
        try {
            Intent i = new Intent(context, frmClass);
            if (hashMap != null) {
                i.putExtra("map", hashMap);
            }
            ((Activity) context).startActivityForResult(i, code);
            ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public void openActivityForResult(final Context context, final Class<?> frmClass, int code) {
        openActivityForResult(context, frmClass, null, code);
    }

    public void openActivity(final Context context, final Class<?> frmClass) {
        openActivity(context, frmClass, null);
    }

    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @SuppressWarnings("unchecked")
    public HashMap<String, String> getIntentHashMap(final Intent intent) {
        try {
            if (intent != null) {
                return (HashMap<String, String>) intent.getSerializableExtra("map");
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public HashMap<Integer, String> getIntentHashMapDynamic(final Intent intent) {
        try {
            if (intent != null) {
                return (HashMap<Integer, String>) intent.getSerializableExtra("map");
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return null;
    }

    public int getAppVersion(final Context context) {
        int version = 0;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return version;
    }

    public String getAppVersionName(final Context context) {
        String version = "";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return version;
    }

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public void saveText(final Context context, final String filename, final String text) {
        try {
            File file = getFile(context, filename);
            if (file != null) {
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                out.write(encryptJSONAES(text, generatePassword(context)));
                out.close();
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "-");
        }
    }
    public String getText(final Context context, String filename) {
        String text = "";
        try {
            File file = getFile(context, filename);
            if (file != null && file.exists()) {
                FileInputStream fin = new FileInputStream(file);
                Reader reader = new InputStreamReader(fin, "UTF-8");
                BufferedReader br = new BufferedReader(reader);
                final StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line.trim());
                }
                br.close();
                fin.close();
                text = sb.toString();
                text = decryptJSONAES(text, generatePassword(context));
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return text;
    }

    private File getFile(final Context context, final String filename) {
        if (filename != null) {
            File file = new File(context.getFilesDir(), filename.hashCode() + "");
            return file;
        }
        return null;
    }

    /* decrypt android, ios, php */
    public String decryptJSONAES(String text, String key) {
        try {
            return AES256Cipher.AES_Decode(text, key);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return "";
    }

    /* encrypt android, ios, php */
    public String encryptJSONAES(String text, String key) {
        try {
            return AES256Cipher.AES_Encode(text, key);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return "";
    }

    public String generatePassword(Context context) {
        String password = "";
        for (int i = 0; i < 4; i++) {
            password += context.getPackageName().hashCode() + "";
        }
        return password.substring(0, 32);
    }
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        return stream.toByteArray();
    }
    public boolean isValidJSON(String data) {
        try {
            new JSONObject(data);
        } catch (JSONException ex) {
            try {
                new JSONArray(data);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public void saveTextToSdCard(String file_name, String text) {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File file = new File(sdCard, file_name);
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(text);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public String readFileAsset(Context ctx, String fileName) {
        String mLine = "", index = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(ctx.getAssets().open(fileName), "UTF-8"));
            while ((mLine = reader.readLine()) != null) {
                index += mLine;
            }
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage() + "");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("Error: ", e.getMessage() + "");
                }
            }
        }
        return index;
    }

    public String getDateFormat(String dateStr) {
        try {
            if (dateStr != null) {
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).parse(dateStr);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                date = sdf.parse(sdf.format(date));
                sdf.setTimeZone(TimeZone.getDefault());
                return sdf.format(date);
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return "";
    }
    public int getHeightNew(final int oldHeight, final int newWidth, final int oldWidth) {
        return (int) (oldHeight * ((double) newWidth / oldWidth));
    }

    public int getWidthNew(final int oldWidth, final int newHeight, final int oldHeight) {
        return (int) (oldWidth * ((double) newHeight / oldHeight));
    }

    public void restartApp(final Context context, final Class<?> mainClass) {
        try {
            openActivity(context, mainClass, null, true, true, false);
            Process.killProcess(Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public String getAndroidID(final Context context) {
        String id = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }
    public void cancelAllNotification(final Context context) {
        try {
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancelAll();
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public String getEncrypted(final Context con, final String text) {
        try {
            final String password = getPassword(con);
            int pass = convertPassword(con, password);
            final char[] chars = text.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                final int position = chars[i];
                final char c = (char) (position + pass);
                pass = convertPassword(con, pass + "");
                chars[i] = c;
            }
            return String.valueOf(chars);
        } catch (Exception e) {
            Log.e("Err getEncrypted: ", e.getMessage() + "");
        }
        return "";
    }

    public String getDecrypted(final Context con, final String text) {
        try {
            final String password = getPassword(con);
            int pass = convertPassword(con, password);
            final char[] chars = text.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                final int position = chars[i];
                final char c = (char) (position - pass);
                pass = convertPassword(con, pass + "");
                chars[i] = c;
            }
            return String.valueOf(chars);
        } catch (Exception e) {
        }
        return "";
    }

    private int convertPassword(final Context con, final String arg) {
        double seed = hashCode(con, arg);
        seed = (seed * getNumber(con, 2, 4) + getNumber(con, 4, 6)) % getNumber(con, 0, 3);
        seed /= getNumber(con, 0, 3);
        return (int) (seed * getNumber(con, 1, 2));
    }

    private long getNumber(final Context con, final int start, final int stop) {
        final String email = con.getPackageName();
        long number = 0;
        for (int i = start; i < email.length(); i++) {
            number += (long) email.charAt(i);
            if (start == stop) {
                break;
            }
        }
        return number;
    }

    private int hashCode(final Context con, final String st) {
        int h = 0;
        final int len = st.length();
        if (h == 0 && len > 0) {
            int off = 0;
            char chars[] = st.toCharArray();
            for (int i = 0; i < len; i++) {
                h = (int) getNumber(con, 2, 4) * h + chars[off++];
            }
        }
        return h;
    }

    private String getPassword(final Context con) {
        final String packagename = con.getPackageName();
        final String password = hashCode(con, packagename) + "";
        return password;
    }

    private void setCurrentLanguage(Context context, String language) {
        try {
            if (!language.equals("")) {
                Locale myLocale = new Locale(language);
                Resources res = context.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    conf.setLocale(myLocale);
                } else {
                    conf.locale = myLocale;
                }
                res.updateConfiguration(conf, dm);
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }


    public void touchAnimation(final View view) {
        try {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            setAlphaView(view, 0.5f);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            setAlphaView(view, 1.0f);
                            break;
                        case MotionEvent.ACTION_UP:
                            setAlphaView(view, 1.0f);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    private void setAlphaView(final View view, final float alpha) {
        try {
            AlphaAnimation aa = new AlphaAnimation(alpha, alpha);
            aa.setDuration(0);
            aa.setFillAfter(true);
            view.startAnimation(aa);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public Bitmap getBitmapFromAsset(String filePath) {
        Bitmap bitmap = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = false;
            final EasyAES e = new EasyAES("123!#@abc*&$(kh)", 128, "123!#@abc*&$(kh)");
            byte[] decryptedData = e.readFileFromDir(filePath);
            bitmap = BitmapFactory.decodeByteArray(decryptedData, 0, decryptedData.length);
        } catch (Exception e) {
            File file = new File(filePath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
            Log.e("Err", e.getMessage() + "");
        }
        return bitmap;
    }

    public void googlePlayMarket(final Context context, final String packagename) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setPackage("com.android.vending");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse("market://details?id=" + packagename));
            context.startActivity(i);
        } catch (Exception e) {
            Log.e("Err: googlePlayMarket", e.getMessage() + "");
        }
    }

    public String getImageKey(final Context ct) {
        int density = ct.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_MEDIUM:
                // return "MDPI";
                return Global.arData[21];
            case DisplayMetrics.DENSITY_HIGH:
                // return "HDPI";
                return Global.arData[21];
            case DisplayMetrics.DENSITY_LOW:
                // return "LDPI";
                return Global.arData[21];
            case DisplayMetrics.DENSITY_XHIGH:
                // return "XHDPI";
                return Global.arData[22];
            case DisplayMetrics.DENSITY_TV:
                // return "TV";
                return Global.arData[22];
            case DisplayMetrics.DENSITY_XXHIGH:
                // return "XXHDPI";
                return Global.arData[22];
            case DisplayMetrics.DENSITY_XXXHIGH:
                // return "XXXHDPI";
                return Global.arData[22];
            default:
                // return "Unknown";
                return Global.arData[22];
        }
    }

    public int getScreenWidth(final Context context) {
        int width = 0;
        try {
            final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            width = metrics.widthPixels;
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return width;
    }

    public int getScreenHeight(final Context context) {
        int height = 0;
        try {
            final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            height = metrics.heightPixels;
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return height;
    }

    public int getScreenWidth(final int orientation, final Context context) {
        //0 = portrait
        return orientation == 0 ? getPortraitWidth(context) : getLandscapeWidth(context);
    }

    public int getScreenOrientation(final Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 1; // Portrait mode
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 2; // Landscape mode
        }
        return 1;
    }

    public int getPortraitWidth(final Context context) {
        int w = getScreenWidth(context);
        int h = getScreenHeight(context);
        return w < h ? w : h;
    }

    public int getPortraitHeight(final Context context) {
        int w = getScreenWidth(context);
        int h = getScreenHeight(context);
        return w > h ? w : h;
    }

    public int getLandscapeWidth(final Context context) {
        int w = getScreenWidth(context);
        int h = getScreenHeight(context);
        return w > h ? w : h;
    }

    public int getLandscapeHeight(final Context context) {
        int w = getScreenWidth(context);
        int h = getScreenHeight(context);
        return w < h ? w : h;
    }
    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
    public void openYouTubeFullScreen(Context context, String link) {
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            intent.putExtra("force_fullscreen", true);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public Location getLastKnownLocation(final LocationManager location_manager) {
        Location bestLocation = null;
        try {
            List<String> providers = location_manager.getProviders(true);
            for (String provider : providers) {
                @SuppressLint("MissingPermission") Location l = location_manager.getLastKnownLocation(provider);
                Log.e("last known", "--");
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    Log.e("found best ", "--");
                    bestLocation = l;
                }
            }
            if (bestLocation == null) {
                return null;
            }
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return bestLocation;
    }

    public Uri getUriFromBitmap(Context context, Bitmap bitmap) {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        final String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }



    public String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public String getCurrencyFormat(final String amount) {
        final DecimalFormat formatter = new DecimalFormat("#,###.00", new DecimalFormatSymbols(Locale.US));
        return formatter.format(Double.parseDouble(amount));
    }

    public void alertMessage(Context ct,String title,String btn, String message,int fontType) {
        try {
            new CustomAlertDialog(ct, title, message, btn, "", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            },fontType).show();
        } catch (Exception e) {
            Log.e("Err: ", e.getMessage() + "");
        }
    }

    public void alertMessage(Context ct, String message, final AlertListenner alertListenner, int fontType) {
        try {
            new CustomAlertDialog(ct, ct.getString(R.string.information),message, ct.getString(R.string.ok), ct.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertListenner.onSubmit();
                }
            }, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            },fontType).show();
        } catch (Exception e) {
            Log.e("Err: ", e.getMessage() + "");
        }
    }



    public StringRequest requestString(final int method, final String url, final HashMap<String, String> params, final VolleyCallback callback) {
        try {
            final StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    callback.onResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError e) {
                    callback.onErrorResponse(e);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    final Map<String, String> params = new HashMap<>();
                    final String cred = String.format("%s:%s", Global.arData[14], Global.arData[15]);
                    final String auth = "Basic " + Base64.encodeToString(cred.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String utf8String = new String(response.data, "UTF-8");
                        return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                    } catch (Exception e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };
            /* Custom timeout volley */
            /*RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);*/
            AppController.getInstance().addToRequestQueue(request);
            return request;
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return null;
    }

    public void showHidePassword(final Context context, final ImageView iv, final EditText edt) {
        try {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    touchAnimation(view);
                    if (iv.getTag() == null) {
                        iv.setTag("1");
                        iv.setImageDrawable(context.getResources().getDrawable(R.drawable.img_eye_1));
                        edt.setTransformationMethod(new HideReturnsTransformationMethod());
                    } else {
                        iv.setTag(null);
                        iv.setImageDrawable(context.getResources().getDrawable(R.drawable.img_eye));
                        edt.setTransformationMethod(new PasswordTransformationMethod());
                    }
                    edt.setSelection(edt.getText().length());
                }
            });
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public boolean isValidEmail(final String text) {
        return !TextUtils.isEmpty(text) && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    public boolean isHistory(final Context context) {
        return getText(context, Global.INFO_FILE).length() > 0;
    }

    public void logout(final Context context) {
        try {
            clearUser(context);
            clearNotification(context);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    private void clearUser(final Context context) {
        saveText(context, Global.INFO_FILE, "");
    }
    private void clearNotification(final Context context) {
        saveText(context, Global.NOTIFICATION, "");
    }

    public void initSelectItem(final Context context, final View view, final View tv, final String[] items, final int fontType) {
        try {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Context wrapper = new ContextThemeWrapper(context, R.style.AppTheme_AppBarOverlay_PopupMenu);
                    final PopupMenu popup = new PopupMenu(wrapper, view);
                    for (int i = 0; i < items.length; i++) {
                        Typeface font = MyFont.getInstance().getFont(context,fontType);
                        CustomTypefaceSpan typefaceSpan = new CustomTypefaceSpan(font);
                        SpannableStringBuilder title = new SpannableStringBuilder(items[i]);
                        title.setSpan(typefaceSpan, 0, title.length(), 0);
                        popup.getMenu().add(0, i, i, title);
                    }
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            ((TextView) tv).setText(item.getTitle());
                            return true;
                        }
                    });
                    popup.show();
                }
            });
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }

    public String getSHA256(final String text) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(text.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFFEE & messageDigest[i]));
            }
            return hexString.toString();
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return "";
    }

    public String getDisplayVersion(Context context) {
        return String.format(context.getString(R.string.version) + " %s", getAppVersionName(context));
    }

    public String getInfo(final Context context, final String key) {
        String info = "";
        try {
            return new JSONObject(getText(context, Global.INFO_FILE)).getString(key);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return info;
    }

    public long getFreeSpace(final String path){
        final StatFs stat = new StatFs(path);
        stat.restat(path);
        final long size = (long)stat.getBlockSize() *(long)stat.getAvailableBlocks();
        return size;
    }
    public boolean isSDCardAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    public int getBannerHeight(final Context ct) {
        try {
            int org_width = 720;
            int org_height = 420;
            int req_width = getScreenWidth(0, ct);
            return getHeightNew(org_height, req_width, org_width);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return 0;
    }

    public String getKeysizeResolution(Context context) {
        String keySize = "";
        int density = context.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                keySize = Global.arData[29];
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                keySize = Global.arData[29];
                break;
            case DisplayMetrics.DENSITY_HIGH:
                keySize = Global.arData[30];
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                keySize = Global.arData[30];
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                keySize = Global.arData[30];
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                keySize = Global.arData[31];
                break;
            default:
                keySize = Global.arData[30];
        }
        return keySize;
    }

    //kh_get image url base on screen resolution
    public String getImageUrl(Context context, JSONObject object) {
        String keySize = "", imgUrl;
        try {

            keySize = getKeysizeResolution(context);
            imgUrl = object.getString(keySize);

            return imgUrl;
        } catch (Exception e) {
        }
        return "";
    }

    public boolean isApplicationInstalledByPackageName(final Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if (packages != null && packageName != null) {
            for (PackageInfo packageInfo : packages) {
                if (packageName.equals(packageInfo.packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void openAppList(Context context, String publisher) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + publisher)));
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:" + publisher)));
        }
    }

    public String getStringImageFacebook(String fid){
        return "https://graph.facebook.com/" + fid + "/picture?type=large";
    }
}
