package com.example.hotel.common

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.example.hotel.R
import com.example.hotel.details.HotelDetailsActivity
import com.example.hotel.details.HotelDetailsFragment
import com.example.hotel.form.HotelFormFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.activity_hotel.*

import com.example.hotel.list.HotelListFragment
import com.example.hotel.list.HotelListViewModel
import com.example.hotel.model.Hotel

class HotelActivity :BaseActivity(),
    HotelListFragment.OnHotelClickListener,

    //Permite tratar o campo de busca na action bar
    SearchView.OnQueryTextListener,

    //É utilizado para saber quando a ação da action bar expandiu e quando voltou ao normal
    MenuItem.OnActionExpandListener {

    private val viewModel: HotelListViewModel by viewModel()

    private var searchView: SearchView? = null

    private val listFragment: HotelListFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentList) as HotelListFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel)

        fabAdd.setOnClickListener{
            listFragment.hideDeleteMode()
            HotelFormFragment.newInstance().open(supportFragmentManager)
        }

    }

    override fun onHotelClick(hotel: Hotel) {
        if (isTablet()) {
            viewModel.hotelIdSelected = hotel.id
            showDetailsFragment(hotel.id)
        } else if (isSmartphone()) {
            showDetailsActivity(hotel.id)
        }
    }

    private fun isTablet() = resources.getBoolean(R.bool.tablet)
    private fun isSmartphone() = resources.getBoolean(R.bool.smartphone)

    private fun showDetailsFragment(hotelId: Long) {
        searchView?.setOnQueryTextListener(null)

        val fragment = HotelDetailsFragment.newInstance(hotelId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.details, fragment, HotelDetailsFragment.TAG_DETAILS)
            .commit()
    }

    private fun showDetailsActivity(hotelId: Long) {
        HotelDetailsActivity.open(this, hotelId)
    }

    //Cria as opções de menu bar e carrega o layout definido
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.hotel, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setOnActionExpandListener(this)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.hint_search)
        searchView?.setOnQueryTextListener(this)

        if(viewModel.getSearchTerm()?.value?.isNotEmpty() == true){
            Handler().post{
                val query = viewModel.getSearchTerm()?.value
                searchItem.expandActionView()
                searchView?.setQuery(query, true)
                searchView?.clearFocus()
            }
        }

        return true
    }

    //Aqui é definida cada ação que os itens de menu executarão
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){
            R.id.action_info ->
                AboutDialogFragment().show(supportFragmentManager, "sobre")
        }

        return super.onOptionsItemSelected(item)
    }

    //onQueryTextSubmit() é chamado quando o botão de busca do teclado virtual é pressionado
    //O booleano retornado indica se a ação deve ser realizada ou não
    override fun onQueryTextSubmit(query: String?) = true

    //onQueryTextChange() É disparado a cada caractere que é digitado na caixa de texto
    //Nesse caso ele realiza uma busca a cada caractere digitado
    override fun onQueryTextChange(newText: String?): Boolean {
        listFragment.search(newText ?: "")
        return true
    }

    //É chamado quando quando a ação de pesquisa expandiu
    override fun onMenuItemActionExpand(item: MenuItem?) = true

    //É chamado quando a ação de pesquisa é fechada
    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
        listFragment.search()
        return true
    }

}