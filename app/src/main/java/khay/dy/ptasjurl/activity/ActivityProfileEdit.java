package khay.dy.ptasjurl.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.PathUtil;
import khay.dy.ptasjurl.util.Tools;

public class ActivityProfileEdit extends ActivityController {

    private final String TAG = "Ac Edit";
    private byte[] pathThum;

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
            edt_fname.setText(object.getString(Global.arData[30]));
            edt_lname.setText(object.getString(Global.arData[31]));
            edt_email.setText(object.getString(Global.arData[24]));
            edt_address.setText(object.getString(Global.arData[49]));
            final String fName = edt_fname.getText().toString();
            final String lName = edt_lname.getText().toString();
            final String email = edt_email.getText().toString();
            final String address = edt_address.getText().toString();
            findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initSave(fName, lName, email, address);
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
                            MyFunction.getInstance().alertMessage(ActivityProfileEdit.this, getString(R.string.information), getString(R.string.ok), getString(R.string.register_success), 1);
                        } else {
                            MyFunction.getInstance().alertMessage(ActivityProfileEdit.this, getString(R.string.information), getString(R.string.ok), "Server Error", 1);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
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
}
