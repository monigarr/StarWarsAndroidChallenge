package com.test.starwarsandroidchallenge.features.characterdetails.presenter

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.test.starwarsandroidchallenge.R
import com.test.starwarsandroidchallenge.features.characterdetails.view.CharacterDetailsActivity
import com.test.starwarsandroidchallenge.features.characterdetails.view.CharacterDetailsView
import com.test.starwarsandroidchallenge.features.homescreen.presenter.CharacterDetailsPresenter
import com.test.starwarsandroidchallenge.model.Film
import com.test.starwarsandroidchallenge.model.Planet
import com.test.starwarsandroidchallenge.model.Species
import com.test.starwarsandroidchallenge.network.RetrofitHelper
import com.test.starwarsandroidchallenge.util.NetworkHelper
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class CharacterDetailsPresenterImpl : CharacterDetailsPresenter {
    private var characterDetailsView: CharacterDetailsView? = null
    private var filmInfo: StringBuilder = StringBuilder()


    override fun attachView(characterDetailsView: CharacterDetailsView) {
        this.characterDetailsView = characterDetailsView
    }

    override fun detachView() {
        this.characterDetailsView = null
    }

    override fun sendFilmDataRequest(list: Array<String?>?) {
        if (characterDetailsView == null) {
            return
        }
        val context = characterDetailsView as Context
        if (!NetworkHelper.isNetworkConnectionAvailable(context)) {
            (characterDetailsView as CharacterDetailsActivity).showNetworkErrorDialog()
            return
        }

        val observables = Observable.zip(getFilmObservables(list), Arrays::asList)
        val disposable = observables?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { it -> run {
                if (characterDetailsView == null) {
                    return@run
                }
                val iterator = it.listIterator()
                while (iterator.hasNext()) {
                    val film = iterator.next() as Film
                    filmInfo.append(context.getString(R.string.title))
                        .append(String.format("%s ", context.getString(R.string.line_separator)))
                        .append(film.title).append("\n\n")
                    filmInfo.append(context.getString(R.string.opening_crawl))
                        .append(String.format("%s ", context.getString(R.string.line_separator)))
                        .append(film.opening_crawl).append("\n\n")
                    filmInfo.append(context.getString(R.string.release_date))
                        .append(String.format("%s ", context.getString(R.string.line_separator)))
                        .append(film.release_date)
                    if (iterator.hasNext()) {
                        filmInfo.append("\n\n")
                        filmInfo.append(context.getString(R.string.film_separator)).append("\n\n")
                    }
                }
                characterDetailsView?.updateFilmDataView(filmInfo.toString())
            } }
    }


    private fun getFilmObservables(list: Array<String?>?): List<Observable<Film>> {
        val filmObservables: ArrayList<Observable<Film>> = ArrayList()
        val iterator = list?.iterator()
        while (iterator?.hasNext()!!) {
            iterator.next()?.let { RetrofitHelper.getAPIService()?.getFilmInfo(it)?.let { filmObservables.add(it) } }
        }
        return filmObservables
    }

    override fun sendSpeciesDataRequest(url : String) {
        if (characterDetailsView == null) {
            return
        }
        val context = characterDetailsView as Context
        if (!NetworkHelper.isNetworkConnectionAvailable(context)) {
            (characterDetailsView as CharacterDetailsActivity).showNetworkErrorDialog()
            return
        }

        val observer = getSpeciesObserver()
        val observable = RetrofitHelper.getAPIService()?.getSpeciesInfo(url)

        observable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(observer)
    }

    private fun getSpeciesObserver(): Observer<Species> {
        var disposable: Disposable?= null
        return object : Observer<Species> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(s: Species) {
                s.name?.let { s.language?.let { it1 -> characterDetailsView?.updateSpeciesDataView(it, it1) } }
                // Wish I could use flatMap to execute both species data and planet at a time!
                // I tried flatMap. But got the final response of planet but not species response.
                // This nested RxKotlin call looks odd.
                s.homeworld?.let { sendPlanetDataRequest(it) }
            }


            override fun onError(e: Throwable) {
                if (characterDetailsView == null) {
                    return
                }
                (characterDetailsView as CharacterDetailsActivity).showDataFetchingErrorDialog()
            }

            override fun onComplete() {
                disposable?.dispose()
            }
        }
    }


     private fun sendPlanetDataRequest(url : String) {
         if (characterDetailsView == null) {
             return
         }
         val context = characterDetailsView as Context
         if (!NetworkHelper.isNetworkConnectionAvailable(context)) {
             (characterDetailsView as CharacterDetailsActivity).showNetworkErrorDialog()
             return
         }

        val observer = getPlanetObserver()
        val observable = RetrofitHelper.getAPIService()?.getPlanetInfo(url)

        observable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(observer)
    }

    private fun getPlanetObserver(): Observer<Planet> {
        var disposable: Disposable?= null
        return object : Observer<Planet> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(s: Planet) {
                s.name?.let { s.population?.let { it1 -> characterDetailsView?.updatePlanetDataView(it, it1) } }
            }

            override fun onError(e: Throwable) {
                if (characterDetailsView == null) {
                    return
                }
                (characterDetailsView as CharacterDetailsActivity).showDataFetchingErrorDialog()
            }

            override fun onComplete() {
                disposable?.dispose()
            }
        }
    }

}