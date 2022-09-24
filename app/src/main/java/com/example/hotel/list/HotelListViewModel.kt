package com.example.hotel.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.hotel.common.SingleLiveEvent
import com.example.hotel.model.Hotel
import com.example.hotel.repository.HotelRepository
import com.example.hotel.repository.http.Status

class HotelListViewModel(private val repository: HotelRepository): ViewModel() {
    var hotelIdSelected: Long = -1

    private val searchTerm = MutableLiveData<String>()

    private val hotels = Transformations.switchMap(searchTerm){term->
        repository.search("%$term%")
    }

    private val inDeleteMode = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val selectedItens = mutableListOf<Hotel>()
    private val selectionCount = MutableLiveData<Int>()
    private val selectedHotels = MutableLiveData<List<Hotel>>().apply {
        value = selectedItens
    }

    private val deletedItens = mutableListOf<Hotel>()
    private val showDeletedMessage = SingleLiveEvent<Int>()
    private val showDetailsCommand = SingleLiveEvent<Hotel>()
    fun isInDeleteMode(): LiveData<Boolean> = inDeleteMode

    fun getSearchTerm(): LiveData<String>? = searchTerm

    fun getHotels(): LiveData<List<Hotel>>? = hotels

    fun selectionCount(): LiveData<Int> = selectionCount

    fun selectedHotels(): LiveData<List<Hotel>> = selectedHotels

    fun showDeletedMessage(): LiveData<Int> = showDeletedMessage

    fun showDetailsCommand(): LiveData<Hotel> = showDetailsCommand

    fun selectHotel(hotel: Hotel){
        if(inDeleteMode.value == true){
            toggleHotelSelected(hotel)

            if(selectedItens.size == 0){
                inDeleteMode.value = false
            } else {
                selectionCount.value = selectedItens.size
                selectedHotels.value = selectedItens
            }
        } else{
            showDetailsCommand.value = hotel
        }
    }

    private fun toggleHotelSelected(hotel: Hotel){
        val existing = selectedItens.find { it.id == hotel.id }

        if(existing == null){
            selectedItens.add(hotel)
        } else {
            selectedItens.removeAll { it.id == hotel.id }
        }
    }

    fun search(term: String = ""){
        searchTerm.value = term
    }

    fun setInDeleteMode(deleteMode: Boolean){
        if(!deleteMode){
            selectionCount.value = 0
            selectedItens.clear()
            selectedHotels.value = selectedItens
            showDeletedMessage.value = selectedItens.size
        }
        inDeleteMode.value = deleteMode
    }

    fun deleteSelected(){

        selectedItens.forEach {
            it.status = Status.DELETE
            repository.update(it)
        }

        deletedItens.clear()
        deletedItens.addAll(selectedItens)
        setInDeleteMode(false)
        showDeletedMessage.value = deletedItens.size
    }

    fun undoDelete(){
        if(deletedItens.isNotEmpty()){
            for(hotel in deletedItens){
                hotel.id = 0L
                repository.save(hotel)
            }
        }
    }
}