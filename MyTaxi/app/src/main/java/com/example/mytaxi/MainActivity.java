package com.example.mytaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytaxi.model.Car;
import com.example.mytaxi.model.CarList;
import com.example.mytaxi.rest.IcarServices;
import com.example.mytaxi.rest.RestClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

public class MainActivity extends FragmentActivity implements ClickListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private CarViewModel mCarViewModel;
    private List<Car> cars = new ArrayList<>();
    private List<Car> carsToDisplay = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,carsToDisplay,this);

    private BottomSheetBehavior mBottomSheetBehavior;
    private LinearLayout expandedLayout;
    private LinearLayout collapsedLayout;
    private Button cancelBotton;
    private RadioGroup mRadioGroup;
    private TextView text;
    private ImageView img;

    private Marker marker;
    private MarkerOptions markerOptions;
    private GoogleMap map;
    private MapView mapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        expandedLayout = findViewById(R.id.expanded_layout);
        collapsedLayout = findViewById(R.id.collapsed_layout);

        cancelBotton = findViewById(R.id.cancel_button);

        mRadioGroup = findViewById(R.id.radio_group);

        text = findViewById(R.id.id_car);
        img = findViewById(R.id.image);

        mRecyclerView = findViewById(R.id.cars_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        markerOptions = new MarkerOptions();

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);


        mCarViewModel = new CarViewModel();
        mCarViewModel.getCars().observe(this, new Observer<CarList>() {
            @Override
            public void onChanged(CarList carList) {
                Log.d("aa",carList.getCars().get(0).getType());
                cars.addAll(carList.getCars());
                mapView.getMapAsync(MainActivity.this);

            }
        });


        bottomSheet();


        cancelBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cancel selecting
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                text.setText("No Vehicle Selected");
                cancelBotton.setVisibility(View.GONE);

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),9));
            }
        });
    }

    // Handling clicking on radio buttons
    public void RadioButtonClicked(View view) {
       boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_all:
                if (checked)
                    // display all
                    carsToDisplay.clear();
                    carsToDisplay.addAll(cars);
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);
                    break;
            case R.id.radio_taxi:
                if (checked)
                    // display taxis
                    carsToDisplay.clear();
                    for(Car car : cars){
                        if(car.getType().equals("TAXI"))
                            carsToDisplay.add(car);
                    }
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);
                    break;
            case R.id.radio_pooling:
                if (checked)
                    // display poolings
                    carsToDisplay.clear();
                    for(Car car : cars){
                        if(car.getType().equals("POOLING"))
                            carsToDisplay.add(car);
                    }
                    mRecyclerView.setAdapter(mRecyclerViewAdapter);
                    break;
        }
    }

    private void bottomSheet(){

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i)
                {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        expandedLayout.setVisibility(View.GONE);
                        collapsedLayout.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mRadioGroup.check(R.id.radio_all);
                        carsToDisplay.clear();
                        carsToDisplay.addAll(cars);
                        mRecyclerView.setAdapter(mRecyclerViewAdapter);
                        expandedLayout.setVisibility(View.VISIBLE);
                        collapsedLayout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        expandedLayout.setVisibility(View.VISIBLE);
                        collapsedLayout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        expandedLayout.setVisibility(View.VISIBLE);
                        collapsedLayout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mBottomSheetBehavior.setState(STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    @Override
    public void clickListen(Car car) {

        Log.d("ddd",car.getId()+"");
        mBottomSheetBehavior.setState(STATE_COLLAPSED);
        text.setText(car.getId()+"");
        if(car.getType().equals("TAXI")){
            img.setImageResource(R.drawable.taxi);
        }
        else if(car.getType().equals("POOLING")){
            img.setImageResource(R.drawable.car_pooling);
        }
        cancelBotton.setVisibility(View.VISIBLE);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(car.getCordinates().getLatitude(),car.getCordinates().getLongitude())
                ,14));    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        for(Car car : cars) {
            LatLng pos = new LatLng(car.getCordinates().getLatitude(), car.getCordinates().getLongitude());

            BitmapDrawable bitmapdraw;
            Bitmap b;
            Bitmap iconCar = null;
            if (car.getType().equals("TAXI"))
            {
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_taxi);
                b = bitmapdraw.getBitmap();
                iconCar = Bitmap.createScaledBitmap(b, 150, 150, false);
        }
            else if(car.getType().equals("POOLING"))
            {
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_car);
                b = bitmapdraw.getBitmap();
                iconCar = Bitmap.createScaledBitmap(b, 150, 150, false);
            }
            marker = map.addMarker(markerOptions.position(pos)
                    .title(car.getType())
                    .snippet(car.getId()+"")
                    .icon(BitmapDescriptorFactory.fromBitmap(iconCar)));
            marker.setRotation((float) car.getHeading());
            marker.setFlat(true);

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,9));
        }

        map.setInfoWindowAdapter(new MarkerHeader(getLayoutInflater()));
        map.setOnInfoWindowClickListener(this);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("sssss",marker.getPosition()+"");
                mBottomSheetBehavior.setState(STATE_COLLAPSED);
                text.setText(marker.getSnippet());

                if(marker.getTitle().equals("TAXI")){
                    img.setImageResource(R.drawable.taxi);
                }
                else if(marker.getTitle().equals("POOLING")){
                    img.setImageResource(R.drawable.car_pooling);
                }

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),14));

                cancelBotton.setVisibility(View.VISIBLE);

                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle("MapViewBundleKey");
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle("MapViewBundleKey", mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }*/
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();

    }
}
