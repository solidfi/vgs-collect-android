package com.solidfi.vgscollectsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.solidfi.vgscollectsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setAtmPinButton.setOnClickListener {
            startActivity(Intent(this, SetATMPinActivity::class.java))
        }

        binding.linkCardButton.setOnClickListener {
            startActivity(Intent(this, LinkCardActivity::class.java))
        }
    }
}