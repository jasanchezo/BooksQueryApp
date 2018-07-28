package com.mtw.josealejandrosanchezortega.booksqueryapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.mtw.josealejandrosanchezortega.booksqueryapp.api.BooksApiService
import com.mtw.josealejandrosanchezortega.booksqueryapp.api.BooksModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BooksAdapter.OnItemClickListener {

    // TODO 13
    private var disposable : Disposable? = null

    // SE INSTANCÍA HASTA QUE SE USA Y SÓLO SUCEDE UNA VEZ. SI SE VUELVE A USAR SE REGRESA EL OBJETO
    // YA INSTANCIADO
    private val booksApiService by lazy {
        BooksApiService.create()
    }

    private fun searchBooks(query : String) {
        // EJECUTAR LA LLAMADA A LA API. ESPECIFFICAMENTE EJECUTAR EL MÉTODO SearchBooks
        disposable = booksApiService.searchBooks(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    result ->
                    // MOSTRAR LA LISTA DE LIBROS REGRESADOS EN LA LLAMADA
                    viewAdapter.setBooks(result.items)
                    // MOSTRAR MENSAJE DE DIALOGO
                    Toast.makeText(this@MainActivity, "búsqueda realizada", Toast.LENGTH_LONG).show()
                }, {
                    error ->
                    showAlert("Error", error.message!!)
                })
    }

    // METODO PARA CREAR UN DIALOGO DE TIPO MESSAGEBOX
    private fun showAlert(title : String, message : String) {
        AlertDialog.Builder(this@MainActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .create().show()
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun onItemClick(bookItem: BooksModel.BookItem) {

    }

    // TODO: DECLARAMOS VARIABLES NECESARIAS
    private lateinit var viewAdapter: BooksAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // TODO: CODIGO PARA QUE FUNCIONES EL RECYCLERVIEW
        viewManager = LinearLayoutManager(this)
        viewAdapter = BooksAdapter(arrayListOf(),this)

        // EL IMPORT ES EL DEL MAIN DE KOTLIN
        rvBooks.apply {
            //setHasFixedSize(true)
            // Set the layout for the RecyclerView to be a linear layout, which measures and
            // positions items within a RecyclerView into a linear list
            layoutManager = viewManager

            // Initialize the adapter and attach it to the RecyclerView
            adapter = viewAdapter

            itemAnimator = DefaultItemAnimator()
        }


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        //TODO (6): Agregar al componente Material SearchView el evento Listener OnQueryTextListener en MainActivity OnCreate
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //Do some magic
                searchBooks(query)
                Toast.makeText(this@MainActivity,"Query: "+ query, Toast.LENGTH_LONG).show()
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                //Do some magic
                return false
            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            //TODO (7): Cerrar de forma segura Material SearchView en evento OnBackPressed MainActivity
            if (search_view.isSearchOpen){
                search_view.closeSearch()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        // TODO (7.1): CODIGO PARA EL EVENTO DEL BOTON DE BUSQUEDA
        val item = menu.findItem(R.id.action_search)
        search_view.setMenuItem(item)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
