package khay.dy.ptasjurl.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import khay.dy.ptasjurl.R;
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

    private ImageView iv_thum, iv_bed, iv_bath, iv_kit;
    private TextView tv_address;

    private final String TAG = "FragmentAdd";
    private View root_view;
    private final int PICK_THUM = 1;
    private final int PICK_BED = 2;
    private final int PICK_BATH = 3;
    private final int PICK_KIT = 4;
    private byte[] pathThum;
    private byte[] pathBed;
    private byte[] pathBath;
    private byte[] pathKit;
    private uploadImage asyncUpload;

    private String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE/*, Manifest.permission.ACCESS_COARSE_LOCATION*/};

    private void checkLocationPermission() {
        if (!MyFunction.getInstance().hasPermissions(root_view.getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) root_view.getContext(), PERMISSIONS, Global.PERMISSION_ALL);
        }
    }

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
            tv_address.setText(getAddress(model_latlg.getInstance().getLatlng()));

    }

    private void findView() {
        iv_thum = root_view.findViewById(R.id.iv_thum);
        iv_bed = root_view.findViewById(R.id.iv_bed);
        iv_bath = root_view.findViewById(R.id.iv_bath);
        iv_kit = root_view.findViewById(R.id.iv_kit);
        tv_address = root_view.findViewById(R.id.tv_address);

    }

    private String getAddress(LatLng latLng) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(root_view.getContext(), Locale.getDefault());

            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            return address;
        } catch (Exception e) {
            Log.e("Errr", e.getMessage());
        }
        return "";
    }

    private void initView() {
        findView();
        initToolBar();
        initPinMap();
        initPickImage();
        initUpload();
    }

    private void initUpload() {
        root_view.findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFileServer();
            }
        });
    }

    private void initPickImage() {
        iv_thum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPickImage(PICK_THUM);
            }
        });
        iv_bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPickImage(PICK_BED);
            }
        });
        iv_bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPickImage(PICK_BATH);
            }
        });
        iv_kit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPickImage(PICK_KIT);
            }
        });
    }

    private void initPickImage(final int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    private void initPinMap() {
        root_view.findViewById(R.id.layout_pin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(root_view.getContext(), ActivitySelectMap.class);
            }
        });
    }

    private void initToolBar() {
        final TextView tv_title = root_view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.add_house));
    }

    private String getRealPathFromURI(Uri contentURI) {
        String filePath;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            filePath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }

    private byte[] strFromPick(Intent data, ImageView iv) {
        try {
            final Uri imageUri = data.getData();
            Bitmap bitImg = null;
            String realPath = PathUtil.getPath(root_view.getContext(), imageUri);

            final File file = new File(realPath);
            Log.e("Uri", file.toString());

            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            if(selectedImage.getWidth() > 3500)
                bitImg = Bitmap.createScaledBitmap(selectedImage, (int) (selectedImage.getWidth() * 0.3), (int) (selectedImage.getHeight() * 0.3), true);
            else if (selectedImage.getWidth() > 2000 && selectedImage.getWidth() < 3500)
                bitImg = Bitmap.createScaledBitmap(selectedImage, (int) (selectedImage.getWidth() * 0.5), (int) (selectedImage.getHeight() * 0.5), true);
            else
                bitImg = Bitmap.createScaledBitmap(selectedImage, (int) (selectedImage.getWidth() * 0.8), (int) (selectedImage.getHeight() * 0.8), true);
            iv.setImageBitmap(bitImg);

            return MyFunction.getInstance().getBytesFromBitmap(bitImg);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case PICK_THUM:
                    pathThum = strFromPick(data, iv_thum);
                    break;
                case PICK_BED:
                    pathBed = strFromPick(data, iv_bed);
                    break;
                case PICK_BATH:
                    pathBath = strFromPick(data, iv_bath);
                    break;
                case PICK_KIT:
                    pathKit = strFromPick(data, iv_kit);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(root_view.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
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
                multipartBody.addFormDataPart("avatar", "img2.png",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                pathBed));
                multipartBody.addFormDataPart("avatar", "img3.png",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                pathBath));
                multipartBody.addFormDataPart("avatar", "img4.png",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                pathKit));
                RequestBody body = multipartBody.build();

                Request request = new Request.Builder()
                        .url("http://192.168.1.6:3000/api/client/upload")
                        .method("POST", body)
                        .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------339941246508838218583894")
                        .build();
                Response response = client.newCall(request).execute();
                Log.e("Response", response.toString());
            } catch (Exception e) {
                Log.e("Err", e.getMessage());
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
