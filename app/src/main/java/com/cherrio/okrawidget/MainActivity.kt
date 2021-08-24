package com.cherrio.okrawidget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cherrio.okra.OkraWidget
import com.cherrio.okrawidget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val okra = OkraWidget{

        }

        binding.button.setOnClickListener {
            okra.show(supportFragmentManager,"OKRA")
        }
    }
}