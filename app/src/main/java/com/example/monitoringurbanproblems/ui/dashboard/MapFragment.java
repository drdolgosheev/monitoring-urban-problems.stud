package com.example.monitoringurbanproblems.ui.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.monitoringurbanproblems.MainActivity;
import com.example.monitoringurbanproblems.Problem;
import com.example.monitoringurbanproblems.R;
import com.example.monitoringurbanproblems.add_problem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Locale;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.startActivities;
import static com.example.monitoringurbanproblems.ui.dashboard.MyLocationListener.imHere;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private mapViewModel mapViewModel;
    MapView mMapView;
    private GoogleMap mGoogleMap;
    View mView;
    LocationManager locationManager;
    String provider;
    private Criteria criteria;
    Location location;
    private double latitude, longitude;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public Marker cur_marker;
    private Problem problem;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel = ViewModelProviders.of(this).get(mapViewModel.class);
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map_need);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        MyLocationListener.SetUpLocationListener(getContext());
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        provider = String.valueOf(locationManager.getBestProvider(criteria, true));
        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        LocationListener locationListener = new MyLocationListener();
        while (location == null) {
            location = imHere;
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = imHere.getLatitude();
                longitude = imHere.getLongitude();
                Toast.makeText(getContext(), "latitude:" + latitude + " longitude:"
                        + longitude, Toast.LENGTH_SHORT).show();
            } else {
                //This is what you need:
                locationManager.requestLocationUpdates(provider, 1000, 0,
                        locationListener);
                Log.e("GPS: ", "No location yet");
            }
        }
        LatLng test = new LatLng(latitude, longitude);

        cur_marker = mGoogleMap.addMarker(new MarkerOptions().position(test).title("Test").draggable(true));
        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.setPosition(marker.getPosition());
            }
        });
        CameraPosition cp = CameraPosition.builder().target(test).zoom(15).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                try {
                    double lat = cur_marker.getPosition().latitude;
                    double lon = cur_marker.getPosition().longitude;
                    Toast.makeText(getContext(), getAddressForLocation(lat, lon).getAddressLine(0),
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), add_problem.class);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Address getAddressForLocation(double lat, double lon) throws IOException {
        Geocoder gc = new Geocoder(getContext(), Locale.getDefault());
        return gc.getFromLocation(lat, lon, 1).get(0);
    }

    private void addProblem() {
        myRef.child("counters").child("prob_id").getKey();
        myRef.child("problems").child(String.valueOf(problem.getId())).setValue(problem, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            }
        });
    }
}