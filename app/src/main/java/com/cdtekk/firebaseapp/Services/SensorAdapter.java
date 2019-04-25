package com.cdtekk.firebaseapp.Services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdtekk.firebaseapp.Model.SensorData;
import com.cdtekk.firebaseapp.R;

import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {

    private Context mCtx;
    private List<SensorData> sensorDataList;

    public SensorAdapter(Context mCtx, List<SensorData> sensorDataList) {
        this.mCtx = mCtx;
        this.sensorDataList = sensorDataList;
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.layout_sensordata, null);

        return new SensorViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder sensorViewHolder, int i) {
        SensorData sensorData = sensorDataList.get(i);

        sensorViewHolder.textViewDate.setText(sensorData.getTime());
        sensorViewHolder.textViewTemperature.setText(String.format("%.2f", sensorData.getTemperature()) + " °C");
        sensorViewHolder.textViewHumidity.setText(String.format("%.2f", sensorData.getHumidity()) + " %");
        sensorViewHolder.textViewFan1State.setText(sensorData.getFan1() == 0 ? "FAN1:OFF" : "FAN1:ON");
        sensorViewHolder.textViewFan2State.setText(sensorData.getFan2() == 0 ? "FAN2:OFF" : "FAN2:ON");
    }

    @Override
    public int getItemCount() {
        return sensorDataList.size();
    }

    class SensorViewHolder extends RecyclerView.ViewHolder{

        TextView textViewFan1State;
        TextView textViewFan2State;
        TextView textViewHumidity;
        TextView textViewTemperature;
        TextView textViewDate;

        @SuppressLint("CutPasteId")
        SensorViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFan1State = itemView.findViewById(R.id.txtFan1State);
            textViewFan2State = itemView.findViewById(R.id.txtFan2State);
            textViewHumidity = itemView.findViewById(R.id.txtHumid0);
            textViewTemperature = itemView.findViewById(R.id.txtTemp0);
            textViewDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
