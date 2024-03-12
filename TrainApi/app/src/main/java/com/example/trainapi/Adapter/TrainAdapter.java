package com.example.trainapi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainapi.Model.TrainResponseModel;
import com.example.trainapi.R;

import java.util.List;

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
//        holder.arrivalTime.setText("Arrival Time: " + trainResponseModelList.get(position).getData().getArriveTime());
//        holder.departureTime.setText("Departure Time: " + trainResponseModelList.get(position).getData().getDepartTime());

    }

    @Override
    public int getItemCount() {
        return trainResponseModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView trainNo, trainName, trainFrom, trainTo, arrivalTime, departureTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trainNo = itemView.findViewById(R.id.trainNo);
            trainName = itemView.findViewById(R.id.trainName);
            trainFrom = itemView.findViewById(R.id.trainFrom);
            trainTo = itemView.findViewById(R.id.trainTo);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
            departureTime = itemView.findViewById(R.id.departureTime);

        }
    }
}
