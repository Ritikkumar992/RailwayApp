package com.example.trainapi.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainapi.Model.TrainResponseModel;
import com.example.trainapi.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.ViewHolder> {

    Context context;
    private List<TrainResponseModel> trainResponseModelList;
    public TrainAdapter(Context context, List<TrainResponseModel> trainResponseModelList){
        this.context = context;
        this.trainResponseModelList = trainResponseModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.train_row, parent,false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.trainNo.setText(String.valueOf(trainResponseModelList.get(position).getTrain_num()));
        holder.trainName.setText(trainResponseModelList.get(position).getName());
        holder.trainFrom.setText(trainResponseModelList.get(position).getTrain_from()+" -");
        holder.trainTo.setText(trainResponseModelList.get(position).getTrain_to());
        holder.arrivalTime.setText("Arrival Time: " + trainResponseModelList.get(position).getData().getArriveTime());
        holder.departureTime.setText("Departure Time: " + trainResponseModelList.get(position).getData().getDepartTime());

        // Extracting Train Days:
        StringBuilder daysStringBuilder = new StringBuilder();
        Map<String, Integer> daysMap = trainResponseModelList.get(position).getData().getDays();
        for(Map.Entry<String, Integer> entry: daysMap.entrySet()){
            String days = entry.getKey();
            int value  = entry.getValue();
            if (value == 1) {
                daysStringBuilder.append(days).append(", ");
            }
        }
        // Remove the last comma if it exists
        if (daysStringBuilder.length() > 0) {
            daysStringBuilder.deleteCharAt(daysStringBuilder.length() - 2); // Delete the comma and space
        }
        holder.trainDays.setText("Available: "+daysStringBuilder);


        // Extracting the classes value from the JSON
        Object classesObject = trainResponseModelList.get(position).getData().getClasses();
        if (classesObject instanceof String) {
            String classesString = (String) classesObject;
            holder.trainClass.setText("Classes: " + classesString);
        } else if (classesObject instanceof List) {
            List<String> classesList = (List<String>) classesObject;
            // Convert the list to a comma-separated string
            String classesString = TextUtils.join(", ", classesList);
            holder.trainClass.setText("Classes: " + classesString);
        } else {
            holder.trainClass.setText("Classes: Unknown");
        }
    }

    @Override
    public int getItemCount() {
        return trainResponseModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView trainNo, trainName, trainFrom, trainTo, arrivalTime, departureTime, trainDays, trainClass;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trainNo = itemView.findViewById(R.id.trainNo);
            trainName = itemView.findViewById(R.id.trainName);
            trainFrom = itemView.findViewById(R.id.trainFrom);
            trainTo = itemView.findViewById(R.id.trainTo);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
            departureTime = itemView.findViewById(R.id.departureTime);
            trainDays = itemView.findViewById(R.id.trainDays);
            trainClass = itemView.findViewById(R.id.trainClass);
        }
    }
}
