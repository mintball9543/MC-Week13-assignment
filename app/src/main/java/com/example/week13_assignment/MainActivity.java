package com.example.week13_assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    MapFragment mapFrag;
    GroundOverlayOptions placeMark;
    List<String> lines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("대전 맛집");
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        readCSV();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gMap = map;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //맵타입 변경 가능
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.348398, 127.387686), 13)); // 위도, 경도, 카메라 줌 레벨
        gMap.getUiSettings().setZoomControlsEnabled(true);

        for(String line : lines) {
            String[] tokens = line.split(",");
            double lat = Double.parseDouble(tokens[0]);
            double lon = Double.parseDouble(tokens[1]);
            String restName = tokens[2];

            LatLng point = new LatLng(lat, lon);
            placeMark = new GroundOverlayOptions().image(
                            BitmapDescriptorFactory.fromResource(R.drawable.food))
                    .position(point, 500f, 500f);
            gMap.addGroundOverlay(placeMark);
        }
    }

    public void readCSV() {
        InputStream inputStream = getResources().openRawResource(R.raw.daejeon_restaurants);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line;
            line = reader.readLine(); // 첫 제목행을 읽어서 버림
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (Exception e) {}
    }
}
