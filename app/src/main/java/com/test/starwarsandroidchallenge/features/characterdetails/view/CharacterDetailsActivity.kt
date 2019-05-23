package com.test.starwarsandroidchallenge.features.characterdetails.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.test.starwarsandroidchallenge.R
import com.test.starwarsandroidchallenge.broadcastreceivers.ConnectivityReceiver
import com.test.starwarsandroidchallenge.dataholder.PeopleData
import com.test.starwarsandroidchallenge.features.base.BaseActivity
import com.test.starwarsandroidchallenge.features.characterdetails.presenter.CharacterDetailsPresenterImpl
import com.test.starwarsandroidchallenge.features.homescreen.presenter.CharacterDetailsPresenter
import com.test.starwarsandroidchallenge.util.Constants
import kotlinx.android.synthetic.main.activity_character_details.*

class CharacterDetailsActivity : BaseActivity(), CharacterDetailsView, ConnectivityReceiver.ConnectivityReceiverListener {
    private var peopleData: PeopleData?= null
    private var characterDetailsPresenter: CharacterDetailsPresenter? = null
    private var isReceivedFilmData: Boolean = false
    private var isReceivedPlanetData: Boolean = false
    private var isReceivedSpeciesData: Boolean = false
    private var connectivityReceiver: ConnectivityReceiver?= null
    private var isOnNetworkConnectionChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)
        supportActionBar?.title = getString(R.string.character_details)

        if (intent != null) {
            peopleData = intent.getParcelableExtra(Constants.KEY_PEOPLE_DATA) as PeopleData
        }

        connectivityReceiver = ConnectivityReceiver(this)
        initViews()
        initPresenter()
        Snackbar.make(tv_details_name, getString(R.string.loading_please_wait), Snackbar.LENGTH_SHORT).show()
        sendFilmDataRequest()
        sendSpeciesDataRequest()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectivityReceiver)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && isOnNetworkConnectionChanged) {
            if (!isReceivedFilmData || !isReceivedPlanetData || !isReceivedPlanetData) {
                Snackbar.make(tv_details_name, getString(R.string.loading_please_wait), Snackbar.LENGTH_SHORT)
                    .show()
                if (!isReceivedFilmData) {
                    sendFilmDataRequest()
                }
                if (!isReceivedPlanetData || !isReceivedPlanetData) {
                    sendSpeciesDataRequest()
                }
            }
        }
        //Added for unwanted initial call at startup
        isOnNetworkConnectionChanged = true
    }

    private fun initPresenter() {
        characterDetailsPresenter = CharacterDetailsPresenterImpl()
        characterDetailsPresenter?.attachView(this)
    }

    private fun initViews() {
        tv_details_name.text = peopleData?.name
        tv_details_birth_year.text = peopleData?.birth_year
        tv_details_height.text = peopleData?.height
    }

    override fun onDestroy() {
        super.onDestroy()
        characterDetailsPresenter?.detachView()
    }

    private fun sendFilmDataRequest() {
        characterDetailsPresenter?.sendFilmDataRequest(peopleData?.filmsArray)
    }

    private fun sendSpeciesDataRequest() {
        peopleData?.speciesArray?.get(0)?.let { characterDetailsPresenter?.sendSpeciesDataRequest(it) }
    }

    override fun updateFilmDataView(filmData: String) {
        isReceivedFilmData = true
        tv_details_films.text = filmData
        tv_details_films.invalidate()
    }

    override fun updateSpeciesDataView(name: String, language: String) {
        isReceivedSpeciesData = true
        tv_details_name_species.text = name
        tv_details_language_species.text = language
    }

    override fun updatePlanetDataView(name: String, population: String) {
        isReceivedPlanetData = true
        tv_details_name_planet.text = name
        tv_details_population_planet.text = population
    }
}
