package com.example.hotel.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotel.model.Hotel
import com.example.hotel.repository.HotelRepository

class HotelFormViewModel(private val repository: HotelRepository): ViewModel() {
    private val validator by lazy { HotelValidator() }

    val photoUrl = MutableLiveData<String>()

    fun loadHotel(id: Long): LiveData<Hotel>{
        return repository.hotelById(id)
    }

    fun saveHotel(hotel: Hotel): Boolean{
        return validator.validate(hotel)
            .also { valideted->
                if(valideted) repository.save(hotel)
            }
    }
}