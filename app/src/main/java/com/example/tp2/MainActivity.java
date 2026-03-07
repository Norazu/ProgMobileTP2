package com.example.tp2; // Ton package simplifié

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private TextView resultMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultMessage = findViewById(R.id.sensorResultMessage);

        // 1. Récupérer le service
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 2. Tester la présence du capteur de pression (Baromètre)
        // On peut tester d'autres types comme Sensor.TYPE_GYROSCOPE ou TYPE_PROXIMITY
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            // Le capteur est présent
            resultMessage.setText("✅ Capteur de pression détecté !\nLes fonctionnalités météo sont activées.");
            resultMessage.setTextColor(Color.GREEN);
        } else {
            // Le capteur est absent
            resultMessage.setText("❌ Capteur de pression absent.\nLes fonctionnalités liées à l'altitude et à la météo sont indisponibles sur cet appareil.");
            resultMessage.setTextColor(Color.RED);
        }
    }
}