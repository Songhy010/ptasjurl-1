package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;

public class ActivitySelectMap extends ActivityController {

    private final String TAG = "Ac Map";

    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION/*, Manifest.permission.ACCESS_COARSE_LOCATION*/};
    private boolean gps_enabled, googleservice_installed, network_enabled, isCheckGps;
    private LocationManager locationManager;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private double lat = 11.561545, lng = 104.892521;
    private int DEFAULT_ZOOM = 15, CAMERA_ANIMATE = 800;
    private final int PIN_W = 75;
    private final int PIN_H = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_map);
        initView();
    }

    private void findView() {
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view));
    }

    private void initView() {
        findView();
        initBack();
        initLocation();
    }

    private void initBack() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initLocation() {
        try {
            if (locationManager == null)
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!MyFunction.getInstance().hasPermissions(ActivitySelectMap.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) ActivitySelectMap.this, PERMISSIONS, Global.PERMISSION_ALL);
                initMap();
            } else {
                initMap();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void initMap() {
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view));
        try {
            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(final GoogleMap map) {

                        if (map != null) {
                            mMap = map;
                            //initialize map
                            LatLng latLng = new LatLng(lat, lng);

                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.img_pin);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, PIN_W, PIN_H, false);

                            MarkerOptions marker = new MarkerOptions().position(latLng).title("Aide et Action").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                            // mMap.addMarker(marker);

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM), CAMERA_ANIMATE, null);
                            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                                @Override
                                public void onCameraMove() {
                                    LatLng lt = mMap.getCameraPosition().target;
                                    Toast.makeText(ActivitySelectMap.this, lt.latitude + " " + lt.longitude + " ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            } else {
                //new DialogAlertCustom(context, null, getString(R.string.error_occured)).show();
            }


        } catch (Exception e) {
            Log.e("Map ", e.getMessage() + "");
        }
    }

}
