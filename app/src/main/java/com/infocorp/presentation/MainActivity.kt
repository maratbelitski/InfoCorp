package com.infocorp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.infocorp.R
import com.infocorp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomMenu.background = null
        binding.bottomMenu.menu.getItem(3).isEnabled = false

    }
}