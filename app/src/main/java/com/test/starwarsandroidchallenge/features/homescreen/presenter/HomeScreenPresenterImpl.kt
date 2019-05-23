package com.test.starwarsandroidchallenge.features.homescreen.presenter

import android.content.Context
import com.test.starwarsandroidchallenge.dataholder.PeopleData
import com.test.starwarsandroidchallenge.features.homescreen.view.HomeScreenActivity
import com.test.starwarsandroidchallenge.features.homescreen.view.HomeScreenView
import com.test.starwarsandroidchallenge.model.PeopleList
import com.test.starwarsandroidchallenge.network.RetrofitHelper
import com.test.starwarsandroidchallenge.util.Constants
import com.test.starwarsandroidchallenge.util.NetworkHelper
import com.test.starwarsandroidchallenge.util.PreferenceManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeScreenPresenterImpl : HomeScreenPresenter {
    private var homeScreenView: HomeScreenView? = null

    override fun searchCharacterByNameOrBirthday(query: String, peopleDataList: ArrayList<PeopleData>) {
        val queryData = query.toLowerCase()
        val resultDataList: ArrayList<PeopleData> = ArrayList()
        val iterator = peopleDataList.listIterator()
        for (item in iterator) {
            if (item.name?.toLowerCase()?.startsWith(queryData)!! || item.birth_year?.toLowerCase()?.startsWith(queryData)!!) {
                resultDataList.add(item)
            }
        }
        if (homeScreenView != null) {
            homeScreenView?.updateRecyclerViewForSearch(resultDataList)
        }
    }

    override fun attachView(homeScreenView: HomeScreenView) {
        this.homeScreenView = homeScreenView
    }

    override fun detachView() {
        this.homeScreenView = null
    }

    override fun sendPeopleListRequest(url: String) {
        if (homeScreenView == null) {
            return
        }
        val context = homeScreenView as Context
        if (!NetworkHelper.isNetworkConnectionAvailable(context)) {
            (homeScreenView as HomeScreenActivity).showNetworkErrorDialog()
            return
        }

        val observer = getPeopleListObserver()
        val observable = RetrofitHelper.getAPIService()?.getPeopleList(url)

        observable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(observer)
    }

    private fun getPeopleListObserver(): Observer<PeopleList> {
        var disposable: Disposable?= null
        return object : Observer<PeopleList> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(s: PeopleList) {
                when (s.next) {
                    null -> { PreferenceManager.putString(Constants.NEXT_URL_PEOPLE_LIST, "") }
                    else -> {
                        PreferenceManager.putString(Constants.NEXT_URL_PEOPLE_LIST, s.next.toString())
                    }
                }

                val peoples : ArrayList<PeopleData> = ArrayList()
                val iterator = s.results?.listIterator()
                if (iterator != null) {
                    for (item in iterator) {
                        peoples.add(PeopleData(item))
                    }

                    if (homeScreenView != null) {
                        homeScreenView?.updateRecyclerView(peoples)
                    }
                }
            }

            override fun onError(e: Throwable) {
                if (homeScreenView == null) {
                    return
                }
                (homeScreenView as HomeScreenActivity).showDataFetchingErrorDialog()
            }

            override fun onComplete() {
                disposable?.dispose()
            }
        }
    }
}