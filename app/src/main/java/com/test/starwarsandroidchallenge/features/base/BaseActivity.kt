package com.test.starwarsandroidchallenge.features.base

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.test.starwarsandroidchallenge.R

open class BaseActivity : AppCompatActivity(), BaseView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    override fun showNetworkErrorDialog() {
        showErrorDialog(getString(R.string.network_connection_error))
    }

    override fun showDataFetchingErrorDialog() {
        showErrorDialog(getString(R.string.data_fetching_error))
    }

    private fun showErrorDialog(errorMessage: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(errorMessage)
            .setCancelable(true)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.show()
    }

}
