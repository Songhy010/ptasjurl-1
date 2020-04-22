package khay.dy.ptasjurl.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivityController;
import khay.dy.ptasjurl.activity.ActivityMoreDesc;
import khay.dy.ptasjurl.activity.ActivitySelectMap;
import khay.dy.ptasjurl.activity.ActivityTermAndCondition;
import khay.dy.ptasjurl.listener.AlertListenner;
import khay.dy.ptasjurl.listener.OkhttpListenner;
import khay.dy.ptasjurl.model.ModelLatLng;
import khay.dy.ptasjurl.util.ConstantStatus;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.PathUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FragmentAdd extends Fragment {

    private CardView card;

    private ImageView iv_thum;
    private TextView tv_address, tv_more_desc;

    private final String TAG = "FragmentAdd";
    private View root_view;
    private final int PICK_THUM = 1;
    private byte[] pathThum;
    private UploadImage asyncUpload;
    private SwipeRefreshLayout swipe;
    private LinearLayout btn_upload ;
    private String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE/*, Manifest.permission.ACCESS_COARSE_LOCATION*/};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add, container, false);
        }
        return root_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkLocationPermission();
        initView();

        /* TODO set font with current language */
        MyFont.getInstance().setFont(root_view.getContext(), root_view, 2);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ModelLatLng.getInstance().getLatlng() != null)
            tv_address.setText(MyFunction.getInstance().getAddress(root_view.getContext(), ModelLatLng.getInstance().getLatlng()));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            switch (requestCode) {
                case PICK_THUM:
                    pathThum = byteFromPick(data, iv_thum);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void initView() {
        findView();
        initToolBar();
        initPinMap();
        initPickImage();
        initUpload();
        initMoreDesc();
        initTerm();
    }

    private void initTerm() {
        final CheckBox checkBox = root_view.findViewById(R.id.check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b) {
                    btn_upload.setEnabled(true);
                    btn_upload.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                else {
                    btn_upload.setEnabled(false);
                    btn_upload.setBackgroundColor(getResources().getColor(R.color.grey));
                }

            }
        });
        root_view.findViewById(R.id.tv_term).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(root_view.getContext(), ActivityTermAndCondition.class);
            }
        });

    }

    private void findView() {
        btn_upload = root_view.findViewById(R.id.btn_upload);
        btn_upload.setEnabled(false);
        btn_upload.setBackgroundColor(getResources().getColor(R.color.grey));
        card = root_view.findViewById(R.id.card);
        iv_thum = root_view.findViewById(R.id.iv_thum);
        tv_more_desc = root_view.findViewById(R.id.tv_more_desc);
        final int height = MyFunction.getInstance().getBannerHeight(root_view.getContext());
        card.getLayoutParams().height = height - 200;
        tv_address = root_view.findViewById(R.id.tv_address);
        swipe = root_view.findViewById(R.id.swipe);
        swipe.setEnabled(false);
        if(MyFunction.getInstance().isHistory(root_view.getContext())){
            swipe.setVisibility(View.VISIBLE);
        }
    }

    private void initToolBar() {
        final TextView tv_title = root_view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.add_house));
    }

    private void checkLocationPermission() {
        if (!MyFunction.getInstance().hasPermissions(root_view.getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) root_view.getContext(), PERMISSIONS, Global.PERMISSION_ALL);
        }
    }

    private void initUpload() {
        root_view.findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_sample);
                pathThum = pathThum == null ? MyFunction.getInstance().getBytesFromBitmap(bitmap) : pathThum;
                uploadFileServer();
            }
        });
    }

    private void initPickImage() {
        iv_thum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(PICK_THUM);
            }
        });
    }

    private void initMoreDesc() {
        tv_more_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(root_view.getContext(), ActivityMoreDesc.class);
            }
        });
    }

    private void initPinMap() {
        root_view.findViewById(R.id.layout_pin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(root_view.getContext(), ActivitySelectMap.class);
            }
        });
    }

    private byte[] byteFromPick(Intent data, ImageView iv) {
        try {
            final Uri imageUri = data.getData();
            String realPath = PathUtil.getPath(root_view.getContext(), imageUri);
            Log.e(TAG, realPath);
            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
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

    private void pickImage(final int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    // Upload to server
    private void uploadFileServer() {
        ((ActivityController)root_view.getContext()).showDialog();
        new Thread(new Runnable() {
            public void run() {
                if (asyncUpload != null) {
                    asyncUpload.cancel(true);
                }
                try {
                    String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[15], Global.arData[67]);
                    final RadioButton rd_room = root_view.findViewById(R.id.rd_room);
                    if (rd_room.isChecked()) {
                        url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[14], Global.arData[67]);
                    }
                    final EditText edt_title = root_view.findViewById(R.id.edt_title);
                    final EditText edt_owner_name = root_view.findViewById(R.id.edt_owner_name);
                    final EditText edt_owner_phone1 = root_view.findViewById(R.id.edt_owner_phone1);
                    final EditText edt_owner_phone2 = root_view.findViewById(R.id.edt_owner_phone2);
                    final EditText edt_price = root_view.findViewById(R.id.edt_price);

                    final JSONObject object = new JSONObject(MyFunction.getInstance().getText(root_view.getContext(), Global.INFO_FILE));
                    final String lat = "" + ModelLatLng.getInstance().getLatlng().latitude;
                    final String lng = "" + ModelLatLng.getInstance().getLatlng().longitude;
                    final String title = edt_title.getText().toString().trim();
                    final String ownerName = edt_owner_name.getText().toString().trim();
                    final String ownerPhone1 = edt_owner_phone1.getText().toString().trim();
                    final String ownerPhone2 = edt_owner_phone2.getText().toString().trim();
                    final String price = edt_price.getText().toString().trim();
                    if(!price.isEmpty()){
                        if(!ownerPhone1.isEmpty()) {
                            MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

                            multipartBody.addFormDataPart(Global.arData[11], "img.png",
                                    RequestBody.create(MediaType.parse("application/octet-stream"),
                                            pathThum));
                            multipartBody.addFormDataPart(Global.arData[68], ownerName);
                            multipartBody.addFormDataPart(Global.arData[69], ownerPhone1);
                            multipartBody.addFormDataPart(Global.arData[70], ownerPhone2);
                            multipartBody.addFormDataPart(Global.arData[8], title);
                            if (rd_room.isChecked()) {
                                multipartBody.addFormDataPart(Global.arData[75], price);
                            } else {
                                multipartBody.addFormDataPart(Global.arData[71], price);
                            }
                            multipartBody.addFormDataPart(Global.arData[72], lat);
                            multipartBody.addFormDataPart(Global.arData[73], lng);
                            multipartBody.addFormDataPart(Global.arData[43], "2");
                            multipartBody.addFormDataPart(Global.arData[33], object.getString(Global.arData[33]));

                            asyncUpload = new UploadImage(url, multipartBody);
                            asyncUpload.execute();
                        }else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyFunction.getInstance().alertMessage(root_view.getContext(), getString(R.string.information), getString(R.string.ok), getString(R.string.required_field), 1);
                                    ((ActivityController)root_view.getContext()).hideDialog();
                                }
                            });
                        }
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyFunction.getInstance().alertMessage(root_view.getContext(), getString(R.string.information), getString(R.string.ok), getString(R.string.required_field), 1);
                                ((ActivityController)root_view.getContext()).hideDialog();
                            }
                        });
                    }
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
                MyFunction.getInstance().okhttpSendRequest(root_view.getContext(), url, multipartBody, new OkhttpListenner() {
                    @Override
                    public void onSuccess(String response) {
                        if (MyFunction.getInstance().isValidJSON(response)) {
                            Log.e("Response", response);
                            final HashMap<String,String> map = new HashMap<>();
                            map.put("data",response);
                            MyFunction.getInstance().alertMessage(root_view.getContext(), getString(R.string.upload_more_desc), new AlertListenner() {
                                @Override
                                public void onSubmit() {
                                    MyFunction.getInstance().openActivityForResult(root_view.getContext(),ActivityMoreDesc.class,map, ConstantStatus.ActivityMoreDesc);
                                }
                            },1);
                        } else {
                            MyFunction.getInstance().alertMessage(root_view.getContext(), getString(R.string.information), getString(R.string.ok), getString(R.string.server_error), 1);
                        }
                        ((ActivityController) root_view.getContext()).hideDialog();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("Error", error);
                        ((ActivityController) root_view.getContext()).hideDialog();
                        MyFunction.getInstance().alertMessage(root_view.getContext(), getString(R.string.information), getString(R.string.ok), getString(R.string.server_error), 1);
                    }
                });

            } catch (final Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Cache", "error");
                        ((ActivityController) root_view.getContext()).hideDialog();
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
