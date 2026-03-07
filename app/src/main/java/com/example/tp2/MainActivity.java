package com.example.tp2;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private RelativeLayout mainLayout;
    private TextView accelValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.mainLayout);
        accelValues = findViewById(R.id.accelValues);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // On enregistre l'écouteur pour l'accéléromètre
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Très important : arrêter l'écoute pour économiser la batterie
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Calcul de la norme du vecteur (intensité du mouvement)
            double acceleration = Math.sqrt(x * x + y * y + z * z);

            accelValues.setText(String.format("Valeur : %.2f", acceleration));

            // Logique des couleurs demandée
            // Au repos, l'accélération est d'environ 9.81 (pesanteur)
            if (acceleration < 12) {
                mainLayout.setBackgroundColor(Color.GREEN); // Valeurs inférieures (repos/lent)
            } else if (acceleration < 20) {
                mainLayout.setBackgroundColor(Color.BLACK); // Valeurs moyennes (mouvement modéré)
            } else {
                mainLayout.setBackgroundColor(Color.RED);   // Valeurs supérieures (secousse)
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Non utilisé ici
    }
}