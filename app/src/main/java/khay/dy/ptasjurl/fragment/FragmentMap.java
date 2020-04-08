package khay.dy.ptasjurl.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivityRoomDetail;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFont;
import khay.dy.ptasjurl.util.MyFunction;

public class FragmentMap extends Fragment implements OnMapReadyCallback {

    private final String TAG = "Fr Home";
    private View root_view;

    private GoogleMap mMap;

    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Marker userMaker;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final int PIN_W = 75;
    private final int PIN_H = 75;
    private Bitmap smallMarker;

    private HashMap<Marker,JSONObject> hashMarker = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_map, container, false);
        }
        return root_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        MyFont.getInstance().setFont(root_view.getContext(), root_view, 2);
    }

    private void setSupportMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initView() {
        try {

            Dexter.withActivity((Activity) root_view.getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    getRequest();
                    getLocation();
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(root_view.getContext());
                    setSupportMap();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {
                    Toast.makeText(root_view.getContext(), "You should allow permission location for map view", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                }
            }).check();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    private void getRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(50000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    private void getLocation() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (mMap != null) {


                    try {
                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.img_location);
                        Bitmap b = bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, PIN_W, PIN_H, false);

                        if (userMaker != null) userMaker.remove();
                        userMaker = mMap.addMarker(new MarkerOptions().position(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude())).title("You").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userMaker.getPosition(), 12f));
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + "");
                    }
                }
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        loadMap();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                try{
                    final JSONObject object = hashMarker.get(marker);
                    final HashMap<String,String> map = new HashMap<>();
                    map.put(Global.arData[7],object.getString(Global.arData[7]));
                    map.put(Global.arData[44],object.getString(Global.arData[44]));
                    MyFunction.getInstance().openActivity(root_view.getContext(), ActivityRoomDetail.class,map);
                }catch (Exception e){
                    Log.e("Err",e.getMessage()+"");
                }

                //Toast.makeText(root_view.getContext(), hashMarker.get(marker), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMap() {
        try {
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[35], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, null, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        final ArrayList<LatLng> latLng = new ArrayList<>();
                        final JSONObject obj = new JSONObject(response);
                        final JSONArray array = obj.getJSONArray(Global.arData[35]);
                        try {
                            for (int i = 0; i < array.length(); i++) {

                                final JSONObject object = array.getJSONObject(i);
                                try {
                                    latLng.add(new LatLng(object.getDouble("lat"), object.getDouble("long")));
                                } catch (Exception e) {
                                    Log.e("LatLong", "Error");
                                }
                                final int type = object.getInt(Global.arData[7]);
                                final String phone = object.getString(Global.arData[26]);
                                final String price = object.getString(Global.arData[10]);
                                if (type == 1) {
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.img_pin1);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    smallMarker = Bitmap.createScaledBitmap(b, PIN_W, PIN_H, false);
                                } else {
                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.img_pin);
                                    Bitmap b = bitmapdraw.getBitmap();
                                    smallMarker = Bitmap.createScaledBitmap(b, PIN_W, PIN_H, false);
                                }

                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(latLng.get(i))
                                        .title("Price: " + price + "$")
                                        .snippet("Phone: " + phone)
                                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                                marker.showInfoWindow();
                                hashMarker.put(marker,object);
                                // MarkerOptions marker = new MarkerOptions().position(latLng.get(i)).title("Phone: "+price+"\nPrice: "+phone).icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                                //mMap.addMarker(marker);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage() + "");
                        }


                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    loadMap();
                    Log.e("Err", e.getMessage() + "");
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    @Override
    public void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    private void accessLocation() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(root_view.getContext());

// Setting Dialog Title
            alertDialog.setTitle("GPS is settings");
// Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
// On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

// on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
// Showing Alert Message
            alertDialog.show();
        }
    }
}
