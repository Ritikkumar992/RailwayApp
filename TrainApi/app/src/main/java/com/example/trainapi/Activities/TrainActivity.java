package com.example.trainapi.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainapi.Adapter.TrainAdapter;
import com.example.trainapi.EndPoints.TrainApi;
//import com.example.trainapi.Model.TrainData;
import com.example.trainapi.Model.TrainResponseModel;
import com.example.trainapi.Network.RetrofitClient;
import com.example.trainapi.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TrainAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        progressDialog = new ProgressDialog(TrainActivity.this);
        progressDialog.setMessage("Please Wait 🚉...");
        progressDialog.show();


        Intent intent = getIntent();
        String trainNoName = intent.getStringExtra("Train_no_name");

        // Create request body
        String requestBodyJson = "{\"search\": \"" + trainNoName + "\"}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);


        //==================================== RETROFIT CALL ====================================//
        Call<List<TrainResponseModel>> call = new RetrofitClient()
                .getRetrofitInstance()
                .create(TrainApi.class)
                .searchTrain(requestBody);

        call.enqueue(new Callback<List<TrainResponseModel>>() {
            @Override
            public void onResponse(Call<List<TrainResponseModel>> call, Response<List<TrainResponseModel>> response) {
                progressDialog.dismiss();
                List<TrainResponseModel> trainDataList = response.body();
                if (response.isSuccessful()) {
                    generateTrainDataList(trainDataList);
//                    if (trainDataList != null) {
//                        for (TrainResponseModel trainResponseModel : trainDataList) {
//
////                            trainName.setText(trainResponseModel.getName());
////                            trainNo.setText(String.valueOf(trainResponseModel.getTrain_num()));
////                            trainFrom.setText(trainResponseModel.getTrain_from()+" To ");
////                            trainTo.setText(trainResponseModel.getTrain_to());
//                        }
//                    } else {
//                        Log.e("ERROR", "Empty train data list");
//                        Toast.makeText(TrainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Log.e("ERROR", response.message());
                    Toast.makeText(TrainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<TrainResponseModel>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                Toast.makeText(TrainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateTrainDataList(List<TrainResponseModel> trainResponseModels){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrainAdapter(this, trainResponseModels);
        recyclerView.setAdapter(adapter);
    }
}