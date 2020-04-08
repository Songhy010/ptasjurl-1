package khay.dy.ptasjurl.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.adapter.AdapterAccessories;
import khay.dy.ptasjurl.adapter.AdapterPick;
import khay.dy.ptasjurl.listener.AccesoriesListener;
import khay.dy.ptasjurl.listener.AlertListenner;
import khay.dy.ptasjurl.listener.OkhttpListenner;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.EasyAES;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.PathUtil;
import khay.dy.ptasjurl.util.Tools;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ActivityMoreDesc extends ActivityController {

    private final String TAG = "Ac MoreDesc";

    private ImageView iv_pick;
    private RecyclerView recycler;
    private EditText edt_floor;
    private EditText edt_electric;
    private EditText edt_water;
    private EditText edt_parking;
    private EditText edt_other;
    private EditText edt_width;
    private EditText edt_length;
    private TextView tv_open;
    private TextView tv_close;
    private TextView tv_available;
    private EditText edt_desc;
    private RecyclerView recycler_acc;
    private ArrayList<String> accessories = new ArrayList<>();
    private String accessoriesStr = "";


    private final int PICK_CODE = 1;
    private ArrayList<byte[]> mArrayByte = new ArrayList<byte[]>();
    private ArrayList<Bitmap> mArrayBit = new ArrayList<Bitmap>();
    private String[] available = {"Busy", "Available"};

    private UploadMoredata asyncUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_desc);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == PICK_CODE && resultCode == RESULT_OK && null != data) {
                if (data.getData() != null) {//on Single image selected
                    Uri imageUri = data.getData();
                    String realPath = PathUtil.getPath(ActivityMoreDesc.this, imageUri);
                    Log.e("path", realPath);
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    final Bitmap newBitmap = MyFunction.getInstance().createScaledBit(selectedImage);
                    final byte[] img = MyFunction.getInstance().getBytesFromBitmap(newBitmap);
                    mArrayByte.add(img);
                    iv_pick.setImageBitmap(newBitmap);
                } else {//on multiple image selected
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri imageUri = item.getUri();
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            final Bitmap newBitmap = MyFunction.getInstance().createScaledBit(selectedImage);
                            final byte[] img = MyFunction.getInstance().getBytesFromBitmap(newBitmap);
                            mArrayByte.add(img);
                            mArrayBit.add(newBitmap);
                        }
                        iv_pick.setImageBitmap(mArrayBit.get(0));
                        initRecycler(mArrayBit);
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }


        super.onActivityResult(requestCode, resultCode, data);


    }

    private void initView() {
        findView();
        initToolBar();
        initPickImage();
        initUpload();
        loadAccessoriesData();
        initOpenCloseTime();
        MyFunction.getInstance().initSelectItem(this, tv_available, tv_available, available, 2);
    }

    private void initOpenCloseTime() {
        tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimePicker(tv_open);
            }
        });
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimePicker(tv_close);
            }
        });
    }

    private void initTimePicker(final TextView tv) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                final String hour = selectedHour < 10 ? String.format("0%s", selectedHour) : selectedHour + "";
                final String minute = selectedMinute < 10 ? String.format("0%s", selectedMinute) : selectedMinute + "";
                tv.setText(String.format("%s:%s:00", hour, minute));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void loadAccessoriesData() {
        try {
            showDialog();
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[63], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(TAG, response);
                        final JSONArray array = new JSONArray(response);
                        initAccessories(array);

                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e(TAG, e.getMessage() + "");
                    loadAccessoriesData();
                    hideDialog();
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.e(TAG, "" + e.getMessage());
        }
    }

    private void initAccessories(JSONArray array) {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recycler_acc.setLayoutManager(manager);
        recycler_acc.setAdapter(new AdapterAccessories(array, this, new AccesoriesListener() {
            @Override
            public void onChecked(String id) {
                accessories.add(id);
                accessoriesStr = "";
                for (int i = 0 ;i < accessories.size();i++){
                    accessoriesStr += String.format("%s,",accessories.get(i));
                }
            }

            @Override
            public void onUnCheck(String id) {
                for (int i = 0 ;i < accessories.size();i++){
                    if(accessories.get(i).equals(id)){
                        accessories.remove(i);
                    }
                }
            }
        }));
    }

    private void initUpload() {
        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreData();
            }
        });
    }

    private void initPickImage() {
        final int height = MyFunction.getInstance().getBannerHeight(this);
        iv_pick.getLayoutParams().height = height - 200;
        iv_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(PICK_CODE);
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

    private void initRecycler(ArrayList<Bitmap> mArrayUri) {
        final LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(new AdapterPick(this, mArrayUri));
    }

    private void findView() {
        iv_pick = findViewById(R.id.iv_pick);
        recycler = findViewById(R.id.recycler);

        edt_floor = findViewById(R.id.edt_floor);
        edt_electric = findViewById(R.id.edt_electric);
        edt_water = findViewById(R.id.edt_water);
        edt_parking = findViewById(R.id.edt_parking);
        edt_other = findViewById(R.id.edt_other);
        edt_width = findViewById(R.id.edt_width);
        edt_length = findViewById(R.id.edt_length);
        tv_available = findViewById(R.id.tv_available);
        tv_open = findViewById(R.id.tv_open);
        tv_close = findViewById(R.id.tv_close);
        edt_desc = findViewById(R.id.edt_desc);
        recycler_acc = findViewById(R.id.recycler_acc);
    }

    private void initToolBar() {
        final ImageView iv_back = findViewById(R.id.iv_back);
        final TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.more_desc));
        iv_back.setImageDrawable(getResources().getDrawable(R.drawable.img_arrow));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadMoreData() {
        showDialog();
        new Thread(new Runnable() {
            public void run() {
                if (asyncUpload != null) {
                    asyncUpload.cancel(true);
                }
                try {

                    final HashMap<String, String> map = MyFunction.getInstance().getIntentHashMap(getIntent());
                    final JSONObject object = new JSONObject(map.get("data"));
                    final String id = object.getString("id");
                    final int type = Integer.parseInt(object.getString("type"));

                    final String number = edt_floor.getText().toString().trim();
                    final String width = edt_width.getText().toString().trim();
                    final String length = edt_length.getText().toString().trim();
                    final String electricPrice = edt_electric.getText().toString().trim();
                    final String waterPrice = edt_water.getText().toString().trim();
                    final String parkingPrice = edt_parking.getText().toString().trim();
                    final String otherPrice = edt_other.getText().toString().trim();
                    final String available = tv_available.getText().toString().trim();
                    final String open = tv_open.getText().toString().trim();
                    final String close = tv_close.getText().toString().trim();
                    final String desc = edt_desc.getText().toString().trim();

                    String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[15], Global.arData[74]);
                    if (type == 2) {
                        url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[14], Global.arData[74]);
                    }
                    MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

                    multipartBody.addFormDataPart("id", id);

                    if (type == 1) {
                        multipartBody.addFormDataPart("floor_number", number);
                        multipartBody.addFormDataPart("flat_width", width);
                        multipartBody.addFormDataPart("flat_length", length);
                        if (available.equals("Busy"))
                            multipartBody.addFormDataPart("avail_flat", "0");
                        else
                            multipartBody.addFormDataPart("avail_flat", "1");
                    } else {
                        multipartBody.addFormDataPart("room_number", number);
                        multipartBody.addFormDataPart("room_width", width);
                        multipartBody.addFormDataPart("room_length", length);
                        if (available.equals("Busy"))
                            multipartBody.addFormDataPart("avail_room", "0");
                        else
                            multipartBody.addFormDataPart("avail_room", "1");
                    }
                    multipartBody.addFormDataPart("home-accessories",accessoriesStr);
                    multipartBody.addFormDataPart("elect_price", electricPrice);
                    multipartBody.addFormDataPart("water_price", waterPrice);
                    multipartBody.addFormDataPart("parking_price", parkingPrice);
                    multipartBody.addFormDataPart("other_price", otherPrice);
                    multipartBody.addFormDataPart("open_time", open);
                    multipartBody.addFormDataPart("close_time", close);

                    multipartBody.addFormDataPart("province_id", "1");
                    multipartBody.addFormDataPart("commune_id", "1");
                    multipartBody.addFormDataPart("district_id", "1");
                    multipartBody.addFormDataPart("village_id", "1");
                    multipartBody.addFormDataPart("description", desc);

                    for (int i = 0; i < mArrayByte.size(); i++) {
                        multipartBody.addFormDataPart("gallery[]", "img.png",
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        mArrayByte.get(i)));
                    }

                    showDialog();
                    asyncUpload = new UploadMoredata(multipartBody, url);
                    asyncUpload.execute();
                } catch (Exception e) {
                    Log.e("Err", e.getMessage() + "");
                }
            }
        }).start();
    }

    public class UploadMoredata extends AsyncTask<Void, Integer, String> {

        private MultipartBody.Builder multipartBody;
        private String url;

        public UploadMoredata(MultipartBody.Builder multipartBody, String url) {
            this.multipartBody = multipartBody;
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                MyFunction.getInstance().okhttpSendRequest(ActivityMoreDesc.this, url, multipartBody, new OkhttpListenner() {
                    @Override
                    public void onSuccess(String response) {
                        try{
                            Log.e("Err", response);
                            final int code = Integer.parseInt(response);
                            if (code == 3) {
                                MyFunction.getInstance().alertMessage(ActivityMoreDesc.this, getString(R.string.register_success), new AlertListenner() {
                                    @Override
                                    public void onSubmit() {
                                        finish();
                                    }
                                }, 1);
                            } else {
                                MyFunction.getInstance().alertMessage(ActivityMoreDesc.this, getString(R.string.information), getString(R.string.ok), getString(R.string.server_error), 1);
                            }
                        }catch (Exception e){
                            Log.e("Err",e.getMessage()+"");
                        }
                        hideDialog();
                    }

                    @Override
                    public void onError(String error) {
                        MyFunction.getInstance().alertMessage(ActivityMoreDesc.this, getString(R.string.information), getString(R.string.ok), getString(R.string.server_error), 1);
                        hideDialog();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyFunction.getInstance().alertMessage(ActivityMoreDesc.this, getString(R.string.information), getString(R.string.ok), getString(R.string.server_error), 1);
                        hideDialog();
                    }
                });
            }
            return null;
        }
    }
}
