package com.example.tp2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private TextView latText, lonText;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latText = findViewById(R.id.latText);
        lonText = findViewById(R.id.lonText);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 1. Vérifier si on a déjà la permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Sinon, la demander à l'utilisateur
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            // Si on l'a déjà, on lance la détection
            demarrerGeoloc();
        }
    }

    // 2. Cette méthode est appelée AUTOMATIQUEMENT quand l'utilisateur clique sur Autoriser/Refuser
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission accordée !
                demarrerGeoloc();
            } else {
                Toast.makeText(this, "Permission GPS refusée", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void demarrerGeoloc() {
        try {
            // On essaie d'afficher la dernière position connue pour ne pas attendre le "fix"
            Location lastKnown = null;
            List<String> providers = locationManager.getProviders(true);
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l != null && (lastKnown == null || l.getAccuracy() < lastKnown.getAccuracy())) {
                    lastKnown = l;
                }
            }
            if (lastKnown != null) {
                onLocationChanged(lastKnown);
            }

            // On demande des mises à jour en temps réel (GPS et Réseau)
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, this);
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 2, this);
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Mise à jour de l'interface
        latText.setText("Latitude : " + location.getLatitude());
        lonText.setText("Longitude : " + location.getLongitude());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // On arrête d'écouter pour ne pas vider la batterie
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(@NonNull String provider) {}
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(this, "Veuillez activer le GPS", Toast.LENGTH_SHORT).show();
    }
}