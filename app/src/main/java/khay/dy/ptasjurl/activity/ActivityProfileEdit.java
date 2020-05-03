package khay.dy.ptasjurl.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.my.MyImageLoader;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.fragment.FragmentAdd;
import khay.dy.ptasjurl.listener.AlertListenner;
import khay.dy.ptasjurl.listener.OkhttpListenner;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.model.ModelLatLng;
import khay.dy.ptasjurl.util.ConstantStatus;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.PathUtil;
import khay.dy.ptasjurl.util.Tools;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ActivityProfileEdit extends ActivityController {

    private final String TAG = "Ac Edit";
    private byte[] pathThum;
    private UploadImage asyncUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        try {
            Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
            initView();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pathThum = byteFromPick(data);
        uploadFileServer();
    }

    private void initView() {
        initBack();
        initProfile();
        initSelectImage();
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

    private void initProfile() {
        try {
            final JSONObject object = new JSONObject(MyFunction.getInstance().getText(this, Global.INFO_FILE));
            final EditText edt_fname = findViewById(R.id.edt_fname);
            final EditText edt_lname = findViewById(R.id.edt_lname);
            final EditText edt_email = findViewById(R.id.edt_email);
            final EditText edt_address = findViewById(R.id.edt_address);
            final TextView tv_name = findViewById(R.id.tv_name);
            final ImageView iv_profile = findViewById(R.id.iv_profile);

            final String email_dis = object.getString(Global.arData[24]).equals("null") ? "" : object.getString(Global.arData[24]);
            final String address_dis = object.getString(Global.arData[49]).equals("null") ? "" : object.getString(Global.arData[49]);
            final String lname_dis = object.getString(Global.arData[31]).equals("null") ? "" : object.getString(Global.arData[31]);
            final String fname_dis = object.getString(Global.arData[30]).equals("null") ? "" : object.getString(Global.arData[30]);

            edt_fname.setText(fname_dis);
            edt_lname.setText(lname_dis);
            edt_email.setText(email_dis);
            edt_address.setText(address_dis);
            tv_name.setText(String.format("%s %s",fname_dis,lname_dis));

            MyImageLoader.getInstance().setImage(iv_profile, object.getString(Global.arData[87]), null, 0, 0, -1, R.drawable.img_loading, R.drawable.img_loading);

            findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String fname = edt_fname.getText().toString();
                    final String lname = edt_lname.getText().toString();
                    final String email = edt_email.getText().toString();
                    final String address = edt_address.getText().toString();
                    initSave(fname, lname, email, address);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void initSave(final String fName, final String lName, final String email, final String address) {
        try {
            final JSONObject obj = new JSONObject(MyFunction.getInstance().getText(ActivityProfileEdit.this, Global.INFO_FILE));
            final HashMap<String, String> param = new HashMap<>();
            param.put(Global.arData[33], obj.getString(Global.arData[33]));
            param.put(Global.arData[30], fName);
            param.put(Global.arData[31], lName);
            param.put(Global.arData[24], email);
            param.put(Global.arData[49], address);
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[45], Global.arData[50]);
            showDialog();
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        if (MyFunction.getInstance().isValidJSON(response)) {
                            MyFunction.getInstance().saveText(ActivityProfileEdit.this, Global.INFO_FILE, response);
                            finish();
                        } else {
                            MyFunction.getInstance().alertMessage(ActivityProfileEdit.this, getString(R.string.information), getString(R.string.ok), "Server Error", 1);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + "");
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e(TAG, e.getMessage() + "");
                    initSave(fName, lName, email, address);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            hideDialog();
        }
    }

    private void initSelectImage() {
        findViewById(R.id.iv_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(1);
            }
        });
    }

    private void pickImage(final int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    private byte[] byteFromPick(Intent data) {
        final ImageView iv = findViewById(R.id.iv_profile);
        try {
            final Uri imageUri = data.getData();
            String realPath = PathUtil.getPath(this, imageUri);
            Log.e(TAG, realPath);
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            final Bitmap newBitmap = MyFunction.getInstance().createScaledBit(selectedImage);
            iv.setImageBitmap(newBitmap);
            return MyFunction.getInstance().getBytesFromBitmap(newBitmap);
        } catch (Exception e) {
            iv.setImageDrawable(getResources().getDrawable(R.drawable.img_sample));
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    private void uploadFileServer() {
        showDialog();
        new Thread(new Runnable() {
            public void run() {
                if (asyncUpload != null) {
                    asyncUpload.cancel(true);
                }
                try {
                    //String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[15], Global.arData[67]);
                    final String url = "http://chekromlek.com/wp-api/auth.php?api_id=user&type=change_profile";
                    final JSONObject object = new JSONObject(MyFunction.getInstance().getText(ActivityProfileEdit.this, Global.INFO_FILE));
                    MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    multipartBody.addFormDataPart("profile", "img.png",
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    pathThum));
                    multipartBody.addFormDataPart(Global.arData[33], object.getString(Global.arData[33]));
                    asyncUpload = new UploadImage(url, multipartBody);
                    asyncUpload.execute();
                } catch (Exception e) {
                    Log.e("Err", e.getMessage() + "");
                }
            }
        }).start();
    }

    public class UploadImage extends AsyncTask<Void, Integer, String> {

        private String url;
        private MultipartBody.Builder multipartBody;

        public UploadImage(String url, MultipartBody.Builder multipartBody) {
            this.url = url;
            this.multipartBody = multipartBody;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                MyFunction.getInstance().okhttpSendRequest(ActivityProfileEdit.this, url, multipartBody, new OkhttpListenner() {
                    @Override
                    public void onSuccess(final String response) {
                        if (MyFunction.getInstance().isValidJSON(response)) {
                            Log.e("Response", response);
                            final HashMap<String, String> map = new HashMap<>();
                            map.put("data", response);
                            MyFunction.getInstance().alertMessage(ActivityProfileEdit.this, getString(R.string.update_profile), new AlertListenner() {
                                @Override
                                public void onSubmit() {
                                    MyFunction.getInstance().saveText(ActivityProfileEdit.this, Global.INFO_FILE, response);
                                    MyFunction.getInstance().alertMessage(ActivityProfileEdit.this, getString(R.string.register_success), "", new AlertListenner() {
                                        @Override
                                        public void onSubmit() {
                                            finish();
                                        }
                                    }, 1);
                                }
                            }, 1);
                        } else {
                            MyFunction.getInstance().alertMessage(ActivityProfileEdit.this, getString(R.string.information), getString(R.string.ok), getString(R.string.server_error), 1);
                        }
                        hideDialog();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("Error", error);
                        hideDialog();
                        MyFunction.getInstance().alertMessage(ActivityProfileEdit.this, getString(R.string.information), getString(R.string.ok), getString(R.string.server_error), 1);
                    }
                });

            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Cache", "error");
                        hideDialog();
                        Log.e(TAG, e.getMessage() + "");
                    }
                });
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
