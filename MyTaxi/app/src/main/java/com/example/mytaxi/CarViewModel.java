package com.example.mytaxi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mytaxi.model.CarList;
import com.example.mytaxi.repository.CarOperation;

public class CarViewModel {

    private MutableLiveData<CarList> cars = new SingleLiveEvent<>();
    private CarOperation mCarOperation;

    public CarViewModel() {
        this.mCarOperation = CarOperation.getInstance();
    }

    public LiveData<CarList> getCars()
    {
        cars = (SingleLiveEvent<CarList>) mCarOperation.getCars();
        return cars;

    }
}
