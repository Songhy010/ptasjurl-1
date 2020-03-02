package khay.dy.ptasjurl.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import java.io.InputStream;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivityMoreDesc;
import khay.dy.ptasjurl.activity.ActivitySelectMap;
import khay.dy.ptasjurl.model.model_latlg;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.PathUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentAdd extends Fragment {

    private CardView card;

    private ImageView iv_thum;
    private TextView tv_address,tv_more_desc;

    private final String TAG = "FragmentAdd";
    private View root_view;
    private final int PICK_THUM = 1;
    private byte[] pathThum;
    private uploadImage asyncUpload;

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
        if (model_latlg.getInstance().getLatlng() != null)
            tv_address.setText(MyFunction.getInstance().getAddress(root_view.getContext(), model_latlg.getInstance().getLatlng()));

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
    }

    private void findView() {
        card = root_view.findViewById(R.id.card);
        iv_thum = root_view.findViewById(R.id.iv_thum);
        tv_more_desc = root_view.findViewById(R.id.tv_more_desc);
        final int height = MyFunction.getInstance().getBannerHeight(root_view.getContext());
        card.getLayoutParams().height = height-200;
        tv_address = root_view.findViewById(R.id.tv_address);
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
    private void initMoreDesc(){
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

        new Thread(new Runnable() {
            public void run() {
                if (asyncUpload != null) {
                    asyncUpload.cancel(true);
                }
                asyncUpload = new uploadImage();
                asyncUpload.execute();
            }
        }).start();
    }

    public class uploadImage extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

                multipartBody.addFormDataPart("avatar", "img1.png",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                pathThum));
/*                multipartBody.addFormDataPart("avatar", "img2.png",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                pathBed));
                multipartBody.addFormDataPart("avatar", "img3.png",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                pathBath));
                multipartBody.addFormDataPart("avatar", "img4.png",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                pathKit));*/
                RequestBody body = multipartBody.build();

                Request request = new Request.Builder()
                        .url("http://192.168.1.3:3000/api/client/upload")
                        .method("POST", body)
                        .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------339941246508838218583894")
                        .build();
                Response response = client.newCall(request).execute();
                Log.e("Response", response.toString());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
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
