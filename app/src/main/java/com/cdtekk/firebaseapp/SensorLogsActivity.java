package com.cdtekk.firebaseapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import com.cdtekk.firebaseapp.Model.SensorData;
import com.cdtekk.firebaseapp.Services.SensorAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SensorLogsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SensorAdapter sensorAdapter;
    DatabaseReference dbSensorData;
    SearchView searchViewSensorData;

    List<SensorData> sensorDataList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_logs);

        sensorDataList = new ArrayList<>();

        searchViewSensorData = findViewById(R.id.searchViewSensorData);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorAdapter = new SensorAdapter(this, sensorDataList);
        recyclerView.setAdapter(sensorAdapter);

        dbSensorData = FirebaseDatabase.getInstance().getReference("/sensor/dht");
        dbSensorData.addValueEventListener(valueEventListener);

        // Defaults search text to last recorded time
        dbSensorData.orderByChild("time").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SensorData> data = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SensorData sensorData = snapshot.getValue(SensorData.class);
                        data.add(sensorData);
                    }

                    searchViewSensorData.setQuery(data.get(0).getTime().substring(0, 10),false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchViewSensorData.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dbSensorData
                        .orderByChild("time")
                        .startAt(query)
                        .endAt(query + "\uf8ff")
                        .addListenerForSingleValueEvent(valueEventListener);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    FirebaseDatabase
                            .getInstance()
                            .getReference("/sensor/dht")
                            .addListenerForSingleValueEvent(valueEventListener);
                }else{
                    dbSensorData
                            .orderByChild("time")
                            .startAt(newText)
                            .endAt(newText + "\uf8ff")
                            .addListenerForSingleValueEvent(valueEventListener);
                }
                return false;
            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            sensorDataList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SensorData sensorData = snapshot.getValue(SensorData.class);
                    sensorDataList.add(sensorData);
                }
                sensorAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
