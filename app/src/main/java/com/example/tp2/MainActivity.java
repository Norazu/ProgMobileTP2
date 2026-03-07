package com.example.tp2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private CameraManager cameraManager;
    private String cameraId;
    private TextView statusText;

    private boolean isFlashOn = false;

    // Paramètres pour la détection du secouement (Shake)
    private static final float SHAKE_THRESHOLD = 20.0f; // Force de la secousse
    private long lastUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);

        // Initialisation capteurs
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialisation caméra pour le flash
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0]; // Généralement la caméra arrière
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // On calcule l'accélération globale en retirant la gravité environ
            double gForce = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

            // Si la force dépasse le seuil
            if (gForce > SHAKE_THRESHOLD) {
                long currentTime = System.currentTimeMillis();
                // On limite la détection à une fois toutes les 500ms pour éviter les clignotements fous
                if (currentTime - lastUpdate > 500) {
                    lastUpdate = currentTime;
                    toggleFlash();
                }
            }
        }
    }

    private void toggleFlash() {
        try {
            isFlashOn = !isFlashOn;
            cameraManager.setTorchMode(cameraId, isFlashOn);

            if (isFlashOn) {
                statusText.setText("🔦 LAMPE ALLUMÉE");
            } else {
                statusText.setText("🌑 LAMPE ÉTEINTE");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}