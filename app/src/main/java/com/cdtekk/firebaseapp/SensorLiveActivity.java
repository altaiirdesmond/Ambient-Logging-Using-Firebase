package com.cdtekk.firebaseapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cdtekk.firebaseapp.Model.SensorData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SensorLiveActivity extends Activity {

    DatabaseReference dbSensorData;
    TextView textViewTempVal;
    TextView textViewHumidVal;
    TextView textViewFanState1;
    TextView textViewFanState2;
    TextView textViewAutoState;
    ImageButton buttonAuto;
    ImageButton buttonFan1;
    ImageButton buttonFan2;
    Button buttonDatalog;

    float temp;
    float humid;
    boolean isAuto;

    @SuppressLint({"SetTextI18n", "ShowToast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_live);

        buttonAuto = findViewById(R.id.btnAuto);
        buttonFan1 = findViewById(R.id.btnFan1);
        buttonFan2 = findViewById(R.id.btnFan2);
        buttonDatalog = findViewById(R.id.btnDataLog);
        textViewHumidVal = findViewById(R.id.humidValue);
        textViewTempVal = findViewById(R.id.tempValue);
        textViewFanState1 = findViewById(R.id.fan1State);
        textViewFanState2 = findViewById(R.id.fan2State);
        textViewAutoState = findViewById(R.id.txtAutoState);

        // Firebase reference
        dbSensorData = FirebaseDatabase.getInstance().getReference("/sensor");

        // Check node values
        dbSensorData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Fans states
                textViewFanState1.setText((long) dataSnapshot.child("Fan1").getValue() > 0 ? "ON" : "OFF");
                textViewFanState2.setText((long) dataSnapshot.child("Fan2").getValue() > 0 ? "ON" : "OFF");

                buttonFan1.setImageDrawable((long) dataSnapshot.child("Fan1").getValue() > 0 ?
                        getDrawable(R.drawable.ic_baseline_toggle_on_24px) : getDrawable(R.drawable.ic_baseline_toggle_off_24px));
                buttonFan2.setImageDrawable((long) dataSnapshot.child("Fan2").getValue() > 0 ?
                        getDrawable(R.drawable.ic_baseline_toggle_on_24px) : getDrawable(R.drawable.ic_baseline_toggle_off_24px));

                // Get auto state
                isAuto = (long) dataSnapshot.child("Auto").getValue() > 0;
                if(isAuto){
                    // Set state
                    if(temp > 30){
                        dbSensorData.child("Fan1").setValue(1);
                        dbSensorData.child("Fan2").setValue(1);
                    }else if(temp < 30){
                        dbSensorData.child("Fan1").setValue(0);
                        dbSensorData.child("Fan2").setValue(0);
                    }
                    textViewAutoState.setText("AUTO");

                    buttonFan1.setEnabled(false);
                    buttonFan2.setEnabled(false);

                    Toast.makeText(getApplicationContext(), "Fan controls are now automatically controlled", Toast.LENGTH_LONG).show();
                }else{
                    textViewAutoState.setText("MANUAL");

                    buttonFan1.setEnabled(true);
                    buttonFan2.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Get last DHT22 log
        dbSensorData.child("dht").orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        humid = Objects.requireNonNull(snapshot.getValue(SensorData.class)).getHumidity();
                        temp = Objects.requireNonNull(snapshot.getValue(SensorData.class)).getTemperature();
                        if(isAuto){
                            if(temp > 30){
                                dbSensorData.child("Fan1").setValue(1);
                                dbSensorData.child("Fan2").setValue(1);
                            }else if(temp < 30){
                                dbSensorData.child("Fan1").setValue(0);
                                dbSensorData.child("Fan2").setValue(0);
                            }
                        }
                        textViewHumidVal.setText(String.format("%.2f", humid) + " %");
                        textViewTempVal.setText(String.format("%.2f", temp) + " °C");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonAuto.setOnClickListener(v -> {
            if(!isAuto){
                dbSensorData.child("Auto").setValue(1);
                buttonAuto.setImageDrawable(getDrawable(R.drawable.ic_baseline_toggle_on_24px));
            }else{
                dbSensorData.child("Auto").setValue(0);
                buttonAuto.setImageDrawable(getDrawable(R.drawable.ic_baseline_toggle_off_24px));
            }
        });

        buttonFan1.setOnClickListener(v -> {
            if(textViewFanState1.getText().toString().equals("ON")){
                dbSensorData.child("Fan1").setValue(0);
            }else{
                dbSensorData.child("Fan1").setValue(1);
            }
        });

        buttonFan2.setOnClickListener(v -> {
            if(textViewFanState2.getText().toString().equals("ON")){
                dbSensorData.child("Fan2").setValue(0);
            }else {
                dbSensorData.child("Fan2").setValue(1);
            }
        });

        buttonDatalog.setOnClickListener(v -> startActivity(new Intent(SensorLiveActivity.this, SensorLogsActivity.class)));
    }
}
