package com.example.apiexample1.EndPoints;

import com.example.apiexample1.Model.RetroPhotoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("/photos") // to get all photos.
    Call<List<RetroPhotoResponse>> getAllPhotos();

    @GET("/photos") // to get only a particular photos on the basis of id.
    Call<List<RetroPhotoResponse>> getPhotosById(
            @Query("id") int id
    );
    @GET("/photos") // to get only a particular photos on the basis of albumId.
    Call<List<RetroPhotoResponse>> getPhotosByAlbumId(
            @Query("albumId") int albumId
    );

    @GET("/photos") //// to get only a particular photos on the basis of id and albumId.
    Call<List<RetroPhotoResponse>> getPhotosByIdAndAlbumId(
            @Query("id") int id,
            @Query("albumId") int albumId
    );
}