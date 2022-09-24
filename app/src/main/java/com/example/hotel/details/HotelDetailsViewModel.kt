package com.example.hotel.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hotel.model.Hotel
import com.example.hotel.repository.HotelRepository

class HotelDetailsViewModel(private val repository: HotelRepository): ViewModel() {

    fun loadHotelDetails(id: Long): LiveData<Hotel>{
        return repository.hotelById(id)
    }
}