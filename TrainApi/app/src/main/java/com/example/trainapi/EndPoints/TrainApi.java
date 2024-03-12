package com.example.trainapi.EndPoints;

import com.example.trainapi.Model.TrainResponseModel;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TrainApi {
    @Headers({
            "content-type: application/json",
            "X-RapidAPI-Key: c4b13cf282mshee97c9524e093fep190015jsn56d9256ae971",
            "X-RapidAPI-Host: trains.p.rapidapi.com"
    })
    @POST("/")
    Call<List<TrainResponseModel>> searchTrain(@Body RequestBody body);

}
