package com.example.tp2 ; // Vérifie que le package correspond au tien

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private ListView sensorListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorListView = findViewById(R.id.sensorListView);

        // 1. Récupérer le service des capteurs
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 2. Obtenir la liste de tous les capteurs
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // 3. Extraire les noms des capteurs pour l'affichage
        List<String> sensorNames = new ArrayList<>();
        for (Sensor s : sensorList) {
            sensorNames.add(s.getName() + " - " + s.getVendor());
        }

        // 4. Créer un Adapter pour lier la liste au composant graphique
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                sensorNames
        );

        // 5. Afficher la liste
        sensorListView.setAdapter(adapter);
    }
}