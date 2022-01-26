package com.stacon.exoplaysample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stacon.exoplaysample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.btnBasic.setOnClickListener {
            startActivity(Intent(this@MainActivity, BasicActivity::class.java))
        }
        binding.btnCustom.setOnClickListener {
            startActivity(Intent(this@MainActivity, CustomActivity::class.java))
        }
    }
}