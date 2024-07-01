package com.solidfi.vgscollectsample

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.solidfi.vgscollectsample.databinding.ActivityLinkCardBinding
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse

class LinkCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLinkCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLinkCardBinding.inflate(layoutInflater)
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
                && binding.contactId.text.toString().isNotEmpty()
                && binding.debitCardToken.text.toString().isNotEmpty()
                && binding.cardNumber.text.toString().isNotEmpty()
                && binding.expiryMonth.text.toString().isNotEmpty()
                && binding.expiryYear.text.toString().isNotEmpty())
    }

    //call the vgs collect api to link the debit card
    private fun callVGSCollect() {
        showProgress()
        // initialize VGS collect
        val vgsForm = VGSCollect(this, binding.vgsVaultId.text.toString(), getVGSEnvString())

        // setting sd-pin-token as a custom header
        val header = HashMap<String, String>()
        header["sd-debitcard-token"] = binding.debitCardToken.text.toString()
        vgsForm.setCustomHeaders(header)

        // setting custom data using the user entered parameters
        val data = HashMap<String, HashMap<String, Any>>()
        val debitCard = HashMap<String, Any>()
        debitCard["expiryMonth"] =  binding.expiryMonth.text.toString()
        debitCard["expiryYear"] = binding.expiryYear.text.toString()
        debitCard["cardNumber"] = binding.cardNumber.text.toString()

        //address is hardcoded this will be the address of the contact
        val address = HashMap<String, String?>()
        address["addressType"] = "card"
        address["line1"] = "1250 Waters Pl"
        address["line2"] = ""
        address["city"] = "Bronx"
        address["state"] = "NY"
        address["country"] = "US"
        address["postalCode"] = "10461"
        debitCard["address"] = address
        data["debitCard"] = debitCard
        vgsForm.setCustomData(data)

        // call vgs collect link api
        vgsForm.asyncSubmit("v2/contact/" + binding.contactId.text.toString() + "/debitcard", HTTPMethod.PATCH)
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

    //function used to show the error message to the user if the link card failed
    private fun handleError() {
        MaterialAlertDialogBuilder(this@LinkCardActivity)
            .setCancelable(false)
            .setTitle(null)
            .setMessage(getString(R.string.link_card_fail))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->

            }
            .show()
    }

    //function used to show the success message to the user if the link card success
    private fun showSuccessPopup() {
        MaterialAlertDialogBuilder(this@LinkCardActivity)
            .setCancelable(false)
            .setTitle(null)
            .setMessage(getString(R.string.card_link_success))
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
