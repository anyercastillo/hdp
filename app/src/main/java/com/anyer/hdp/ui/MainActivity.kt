package com.anyer.hdp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.anyer.hdp.AppViewModel
import com.anyer.hdp.AppViewModelFactory
import com.anyer.hdp.R

class MainActivity : AppCompatActivity() {
    val appViewModel by viewModels<AppViewModel> {
        AppViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}