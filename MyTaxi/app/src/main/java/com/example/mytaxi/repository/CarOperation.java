package com.example.mytaxi.repository;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mytaxi.SingleLiveEvent;
import com.example.mytaxi.model.CarList;
import com.example.mytaxi.rest.IcarServices;
import com.example.mytaxi.rest.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarOperation {


    private MutableLiveData<CarList> cars = new SingleLiveEvent<>();
    //api calls
    //database operations

    private static CarOperation mCarOperations;


    private CarOperation() {
    }

    public static synchronized CarOperation getInstance() {
        if (mCarOperations == null) {
            mCarOperations = new CarOperation();
        }
        return mCarOperations;
    }

    public LiveData<CarList> getCars(){
        IcarServices apiService = RestClient.getClient().create(IcarServices.class);
        Call<CarList> calling = apiService.getCars( "30.129610","30.864947","29.824722","31.519720");

        calling.enqueue(new Callback<CarList>() {
            @Override
            public void onResponse(Call<CarList> call, Response<CarList> response) {
                if (!response.isSuccessful()) {
                    Log.d("TAGG", "ERROR :  " + response.raw().body().toString());

                } else if (response.isSuccessful()) {
                    cars.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CarList> call, Throwable t) {
                Log.e("TAG", "Got error : " + t.getLocalizedMessage());
            }
        });
        return cars;
    }
}