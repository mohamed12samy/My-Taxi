package com.example.mytaxi.rest;



import com.example.mytaxi.model.CarList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IcarServices {

    @GET("/")
    Call<CarList> getCars(@Query("p1Lat")String x,
                          @Query("p1Lon")String y,
                          @Query("p2Lat")String z,
                          @Query("p2Lon")String w
    );

}
