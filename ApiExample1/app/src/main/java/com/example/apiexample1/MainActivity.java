package com.example.apiexample1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.apiexample1.Adapter.CustomAdapter;
import com.example.apiexample1.EndPoints.Api;
import com.example.apiexample1.Model.RetroPhotoResponse;
import com.example.apiexample1.Network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //================ Create Handle for Retrofit Handle Instance ==============================//
        Call<List<RetroPhotoResponse>> call =
                new RetrofitClient()
                .getRetrofitInstance()
                .create(Api.class)
                .getAllPhotos();
//                .getPhotosById(1);
//                .getPhotosByAlbumId(1);
//                .getPhotosByIdAndAlbumId(1,1);

        call.enqueue(new Callback<List<RetroPhotoResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<RetroPhotoResponse>> call, @NonNull Response<List<RetroPhotoResponse>> response) {
                progressDialog.dismiss();
//                List<RetroPhotoResponse> responseBody = response.body();
//                for (RetroPhotoResponse photoResponse : responseBody) {
//                    Log.d("Response Body", photoResponse.toString());
//                }
                generateDataList(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<RetroPhotoResponse>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateDataList(List<RetroPhotoResponse> retroPhotoList){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new CustomAdapter(this, retroPhotoList);
        recyclerView.setAdapter(adapter);
    }
}