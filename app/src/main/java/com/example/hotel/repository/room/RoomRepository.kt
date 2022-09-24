package com.example.hotel.repository.room

import androidx.lifecycle.LiveData
import com.example.hotel.model.Hotel
import com.example.hotel.repository.HotelRepository
import com.example.hotel.repository.http.Status

class RoomRepository(database: HotelDatabase): HotelRepository {
    private val hotelDao = database.hotelDao()

    override fun save(hotel: Hotel) {
        if(hotel.id == 0L){
            hotel.status = Status.INSERT
            val id = insert(hotel)
            hotel.id = id
        } else {
            hotel.status = Status.UPDATE
            update(hotel)
        }
    }

    override fun insert(hotel: Hotel): Long {
        return hotelDao.insert(hotel)
    }

    override fun update(hotel: Hotel) {
         hotelDao.update(hotel)
    }

    override fun hotelByServerId(serverId: Long): Hotel? {
        return hotelDao.hotelByServerId(serverId)
    }

    override fun pending(): List<Hotel> {
        return hotelDao.pending()
    }

    override fun remove(vararg hotels: Hotel) {
        hotelDao.delete(*hotels)
    }

    override fun hotelById(id: Long): LiveData<Hotel> {
        return hotelDao.hotelById(id)
    }

    override fun search(term: String): LiveData<List<Hotel>> {
        return hotelDao.search(term)
    }
}