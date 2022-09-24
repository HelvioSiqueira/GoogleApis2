package com.example.hotel.repository.imagefiles

import com.example.hotel.model.Hotel

interface FindHotelPicture {
    fun pictureFile(hotel: Hotel): PictureToUpload
}