package com.example.tp2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor linearAccel;
    private TextView directionText, debugValues;

    // Seuil très bas car la gravité est déjà supprimée !
    private static final float SEUIL = 5.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        directionText = findViewById(R.id.directionText);
        debugValues = findViewById(R.id.debugValues);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            // Utilisation du capteur LINEAR_ACCELERATION (sans gravité)
            linearAccel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (linearAccel != null) {
            sensorManager.registerListener(this, linearAccel, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = event.values[0];
            float y = event.values[1];

            debugValues.setText(String.format("Linéaire x: %.2f | y: %.2f", x, y));

            if (Math.abs(x) > Math.abs(y)) {
                if (x > SEUIL) directionText.setText("⬅️ GAUCHE");
                else if (x < -SEUIL) directionText.setText("➡️ DROITE");
            } else {
                if (y > SEUIL) directionText.setText("⬇️ BAS");
                else if (y < -SEUIL) directionText.setText("⬆️ HAUT");
            }

            // On reset le texte si le mouvement s'arrête
            if (Math.abs(x) < 0.5f && Math.abs(y) < 0.5f) {
                directionText.setText("IMMOBILE");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}