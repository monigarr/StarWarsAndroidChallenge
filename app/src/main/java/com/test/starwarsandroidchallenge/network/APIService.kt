package com.test.starwarsandroidchallenge.network;

import com.test.starwarsandroidchallenge.model.Film
import com.test.starwarsandroidchallenge.model.PeopleList;
import com.test.starwarsandroidchallenge.model.Planet
import com.test.starwarsandroidchallenge.model.Species
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

interface APIService {
    @GET
    fun getPeopleList(@Url url : String) : Observable<PeopleList>

    @GET
    fun getFilmInfo(@Url url : String) : Observable<Film>

    @GET
    fun getSpeciesInfo(@Url url : String) : Observable<Species>

    @GET
    fun getPlanetInfo(@Url url : String) : Observable<Planet>
}
