package com.solidfi.vgscollectsample

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.solidfi.vgscollectsample.databinding.ActivityMainBinding
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

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

    private fun validate(): Boolean {
        return (binding.vgsVaultId.text.toString().isNotEmpty()
                && binding.cardId.text.toString().isNotEmpty()
                && binding.debitCardToken.text.toString().isNotEmpty()
                && binding.lastFourDigitsCard.text.toString().isNotEmpty()
                && binding.expiryMonth.text.toString().isNotEmpty()
                && binding.expiryYear.text.toString().isNotEmpty()
                && binding.fourDigitPin.text.toString().isNotEmpty())
    }

    private fun callVGSCollect() {
        showProgress()
        // initialize VGS collect
        val vgsForm = VGSCollect(this, binding.vgsVaultId.text.toString(), getVGSEnvString())
        val header = HashMap<String, String>()
        header["sd-pin-token"] = binding.debitCardToken.text.toString()
        vgsForm.setCustomHeaders(header)
        val data = HashMap<String, String>()
        data["pin"] = binding.fourDigitPin.text.toString()
        data["expiryMonth"] = binding.expiryMonth.text.toString()
        data["expiryYear"] = binding.expiryYear.text.toString()
        data["last4"] = binding.lastFourDigitsCard.text.toString()
        vgsForm.setCustomData(data)
        vgsForm.asyncSubmit("v1/card/" + binding.cardId.text.toString() + "/pin", HTTPMethod.POST)
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

    private fun showProgress() {
        binding.submitButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.submitButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun handleError() {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setCancelable(false)
            .setTitle(null)
            .setMessage(getString(R.string.pin_set_fail))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->

            }
            .show()
    }

    private fun showSuccessPopup() {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setCancelable(false)
            .setTitle(null)
            .setMessage(getString(R.string.pin_set_success))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                finish()
            }
            .show()
    }

    private fun getVGSEnvString(): String {
        return if (binding.environment.toString() == getString(R.string.production)) {
            "live"
        } else {
            "sandbox"
        }
    }
}