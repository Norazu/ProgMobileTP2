package com.example.tp2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private ImageView proximityImage;
    private TextView proximityValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        proximityImage = findViewById(R.id.proximityImage);
        proximityValue = findViewById(R.id.proximityValue);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];
            proximityValue.setText("Distance : " + distance + " cm");

            // La plupart des capteurs renvoient 0 pour "proche"
            // et une valeur > 0 pour "loin"
            if (distance < proximitySensor.getMaximumRange()) {
                // OBJET PROCHE
                proximityImage.setImageResource(android.R.drawable.presence_online);
                // Note : Tu peux utiliser tes propres images dans res/drawable
            } else {
                // OBJET LOIN
                proximityImage.setImageResource(android.R.drawable.presence_invisible);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}