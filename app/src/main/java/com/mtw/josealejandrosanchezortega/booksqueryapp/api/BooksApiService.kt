package com.mtw.josealejandrosanchezortega.booksqueryapp.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// DEBE SER ESTE Observable Y NO EL DE OTRO PAQUETE
import io.reactivex.Observable

// TODO 12: Crear interfase
interface BooksApiService {
    //URL DE LA API: https://www.googleapis.com/books/v1/volumes?q=Android&maxResults=10&printType=books&projection=lite&key={YOUR_API_KEY}
    @GET("volumes?maxResults=10&printType=books&projection=lite")

    // SI SE REQUIEREN MÁS PARÁMETROS SOLO SE SEPARAN POR COMAS:
    // @Query("q") query:String, @Query("otrocriterio") query: String ....
    fun searchBooks(@Query("q") query : String) : Observable<BooksModel.BooksApiResponse>

    // METODO ESTATICO PARA EL LLAMADO DEL WS
    companion object {
        fun create() : BooksApiService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://www.googleapis.com/books/v1/")
                    .build()
            return retrofit.create(BooksApiService::class.java)
        }
    }
}