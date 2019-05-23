package com.test.starwarsandroidchallenge.features.homescreen.view

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.test.starwarsandroidchallenge.R
import com.test.starwarsandroidchallenge.adapters.CharacterListAdapter
import com.test.starwarsandroidchallenge.broadcastreceivers.ConnectivityReceiver
import com.test.starwarsandroidchallenge.dataholder.PeopleData
import com.test.starwarsandroidchallenge.features.base.BaseActivity
import com.test.starwarsandroidchallenge.features.characterdetails.view.CharacterDetailsActivity
import com.test.starwarsandroidchallenge.features.homescreen.presenter.HomeScreenPresenter
import com.test.starwarsandroidchallenge.features.homescreen.presenter.HomeScreenPresenterImpl
import com.test.starwarsandroidchallenge.util.Constants
import com.test.starwarsandroidchallenge.util.PreferenceManager
import kotlinx.android.synthetic.main.activity_home_screen.*


class HomeScreenActivity : BaseActivity(), HomeScreenView, CharacterListAdapter.Listener, ConnectivityReceiver.ConnectivityReceiverListener {
    private var homeScreenPresenter: HomeScreenPresenter? = null
    private var isLoadingData: Boolean = false
    private var lastVisibleItemPosition: Int = 0
    private var characterListAdapter: CharacterListAdapter? = null
    private var connectivityReceiver: ConnectivityReceiver?= null
    private var isOnNetworkConnectionChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        supportActionBar?.title = getString(R.string.home_page_time)

        connectivityReceiver = ConnectivityReceiver(this)
        initSharedPreferences()
        initViews()
        initRecyclerView()
        initPresenter()
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
        if (isConnected && isLoadingData && isOnNetworkConnectionChanged) {
            Snackbar.make(character_list_recycler_view, getString(R.string.loading_please_wait), Snackbar.LENGTH_SHORT)
                .show()
            PreferenceManager.getNetworkCalliingUrl(Constants.NEXT_URL_PEOPLE_LIST)?.let {
                homeScreenPresenter?.sendPeopleListRequest(
                    it
                )
            }
        }
        //Added for unwanted initial call at startup
        isOnNetworkConnectionChanged = true
    }

    private fun initSharedPreferences() {
        PreferenceManager.init(this)
        PreferenceManager.removeString(Constants.NEXT_URL_PEOPLE_LIST)
    }

    private fun initPresenter() {
        homeScreenPresenter = HomeScreenPresenterImpl()
        homeScreenPresenter?.attachView(this)
        isLoadingData = true
        et_search_box.isEnabled = false
        Snackbar.make(character_list_recycler_view, getString(R.string.loading_please_wait), Snackbar.LENGTH_SHORT)
            .show()
        PreferenceManager.getNetworkCalliingUrl(Constants.NEXT_URL_PEOPLE_LIST)?.let {
            homeScreenPresenter?.sendPeopleListRequest(
                it
            )
        }
    }

    private fun initViews() {
        et_search_box.addTextChangedListener(afterTextChanged = { text: Editable? ->
            run {
                if (text.isNullOrEmpty()) {
                    characterListAdapter?.restoreAdapterToInitialState()
                } else {
                    characterListAdapter?.getActualAdapterDataList()?.let {
                        homeScreenPresenter?.searchCharacterByNameOrBirthday(
                            text.toString().trim(),
                            it
                        )
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        character_list_recycler_view.layoutManager = layoutManager
        characterListAdapter = CharacterListAdapter(this)
        character_list_recycler_view.adapter = characterListAdapter
        character_list_recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        character_list_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (et_search_box.text.trim().length == 0 && !isLoadingData &&
                    totalItemCount == lastVisibleItemPosition + 1
                    && PreferenceManager.getNetworkCalliingUrl(Constants.NEXT_URL_PEOPLE_LIST)?.isNotEmpty()!!
                ) {
                    PreferenceManager.getString(Constants.NEXT_URL_PEOPLE_LIST)?.let {
                        homeScreenPresenter?.sendPeopleListRequest(
                            it
                        )
                    }
                    Snackbar.make(
                        character_list_recycler_view,
                        getString(R.string.loading_please_wait),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    isLoadingData = true
                    et_search_box.isEnabled = false
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        homeScreenPresenter?.detachView()
    }

    override fun updateRecyclerView(peopleDataList: ArrayList<PeopleData>) {
        (character_list_recycler_view.adapter as CharacterListAdapter).updateAdapter(peopleDataList)
        isLoadingData = false
        Snackbar.make(character_list_recycler_view, getString(R.string.loading_successful), Snackbar.LENGTH_SHORT)
            .show()
        et_search_box.isEnabled = true
    }

    override fun updateRecyclerViewForSearch(peopleDataList: ArrayList<PeopleData>) {
        (character_list_recycler_view.adapter as CharacterListAdapter).updateAdapterForSearch(peopleDataList)
        character_list_recycler_view.adapter = characterListAdapter
    }

    override fun onItemClick(peopleData: PeopleData) {
        val peopleDataIntent = Intent(this, CharacterDetailsActivity::class.java)
        peopleDataIntent.putExtra(Constants.KEY_PEOPLE_DATA, peopleData)
        startActivity(peopleDataIntent)
    }
}
