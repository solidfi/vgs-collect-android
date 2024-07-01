package com.solidfi.vgscollectsample

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.solidfi.vgscollectsample.databinding.ActivitySetAtmPinBinding
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse

class SetATMPinActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetAtmPinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAtmPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    //initialize the UI for the user to enter the required parameters
    private fun initUI() {
        binding.environment.setText(getString(R.string.sandbox))
        binding.environment.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.environment)
            popupMenu.menu.add(getString(R.string.sandbox))
            popupMenu.menu.add(getString(R.string.production))
            popupMenu.setOnMenuItemClickListener { item ->
                binding.environment.setText(item.title)
                true
            }
            popupMenu.show()
        }

        binding.submitButton.setOnClickListener {
            if (validate()) {
                callVGSCollect()
            }
        }
    }

    //function used to validate the user entered parameters
    private fun validate(): Boolean {
        return (binding.vgsVaultId.text.toString().isNotEmpty()
                && binding.cardId.text.toString().isNotEmpty()
                && binding.debitCardToken.text.toString().isNotEmpty()
                && binding.lastFourDigitsCard.text.toString().isNotEmpty()
                && binding.expiryMonth.text.toString().isNotEmpty()
                && binding.expiryYear.text.toString().isNotEmpty()
                && binding.fourDigitPin.text.toString().isNotEmpty())
    }

    //call the vgs collect api to set the ATM pin
    private fun callVGSCollect() {
        showProgress()
        // initialize VGS collect
        val vgsForm = VGSCollect(this, binding.vgsVaultId.text.toString(), getVGSEnvString())

        // setting sd-pin-token as a custom header
        val header = HashMap<String, String>()
        header["sd-pin-token"] = binding.debitCardToken.text.toString()
        vgsForm.setCustomHeaders(header)

        // setting custom data using the user entered parameters
        val data = HashMap<String, String>()
        data["pin"] = binding.fourDigitPin.text.toString()
        data["expiryMonth"] = binding.expiryMonth.text.toString()
        data["expiryYear"] = binding.expiryYear.text.toString()
        data["last4"] = binding.lastFourDigitsCard.text.toString()
        vgsForm.setCustomData(data)

        // call vgs collect submit api
        vgsForm.asyncSubmit("v2/card/" + binding.cardId.text.toString() + "/pin", HTTPMethod.POST)
        vgsForm.addOnResponseListeners(object : VgsCollectResponseListener {
            override fun onResponse(response: VGSResponse?) {
                hideProgress()
                when (response?.code) {
                    200 -> showSuccessPopup()
                    else -> handleError()
                }
            }
        })
    }

    //function used to show the progress while api call
    private fun showProgress() {
        binding.submitButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    //function used to hide the progress after api call
    private fun hideProgress() {
        binding.submitButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    //function used to show the error message to the user if the pin setting is failed
    private fun handleError() {
        MaterialAlertDialogBuilder(this@SetATMPinActivity)
            .setCancelable(false)
            .setTitle(null)
            .setMessage(getString(R.string.pin_set_fail))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->

            }
            .show()
    }

    //function used to show the success message to the user if the pin setting is success
    private fun showSuccessPopup() {
        MaterialAlertDialogBuilder(this@SetATMPinActivity)
            .setCancelable(false)
            .setTitle(null)
            .setMessage(getString(R.string.pin_set_success))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                finish()
            }
            .show()
    }

    //function used to get the environment based on the user selection (live or sandbox)
    private fun getVGSEnvString(): String {
        return if (binding.environment.text.toString() == getString(R.string.production)) {
            "live"
        } else {
            "sandbox"
        }
    }
}
